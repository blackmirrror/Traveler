package ru.blackmirrror.chats.data

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import ru.blackmirrror.core.BuildConfig
import ru.blackmirrror.core.provider.AccountProvider
import ru.blackmirrror.core.provider.AuthProvider
import ru.blackmirrror.core.provider.NetworkProvider
import ru.blackmirrror.core.state.ResultState
import javax.inject.Inject

class ChatWebSocketRepository @Inject constructor(
    private val authProvider: AuthProvider,
    private val accountProvider: AccountProvider,
    private val networkProvider: NetworkProvider
) {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    private val _messageFlow = MutableSharedFlow<ResultState<SocketEvent>>(replay = 0)
    val messageFlow: SharedFlow<ResultState<SocketEvent>> = _messageFlow.asSharedFlow()

    fun connect() {
        val request = Request.Builder()
            .url("ws://${BuildConfig.BACKEND_HOST}/ws/chat?userId=${getUserId()}")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {}

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val event = parseSocketEvent(text)
                    _messageFlow.tryEmit(ResultState.Success(event))
                } catch (e: Exception) {
                    _messageFlow.tryEmit(ResultState.Error(e))
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _messageFlow.tryEmit(ResultState.Error(Exception(t)))
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
            }
        })
    }

    fun disconnect() {
        webSocket?.close(1000, "Client closing")
    }

    fun sendMessage(event: SocketEvent.SendMessage) {
        val json = JSONObject().apply {
            put("type", "message")
            put("chatId", event.chatId)
            put("senderId", getUserId())
            put("text", event.text)
        }.toString()
        webSocket?.send(json)
    }

    private fun parseSocketEvent(json: String): SocketEvent {
        val obj = JSONObject(json)
        return when (obj.getString("type")) {
            "new_message" -> SocketEvent.NewMessage(
                chatId = obj.getLong("chatId"),
                senderId = obj.getLong("senderId"),
                text = obj.getString("message")
            )
            "typing" -> SocketEvent.Typing(
                chatId = obj.getLong("chatId"),
                userId = obj.getLong("userId")
            )
            "message_read" -> SocketEvent.MessageRead(
                messageId = obj.getLong("messageId"),
                userId = obj.getLong("userId")
            )
            else -> throw IllegalArgumentException("Unknown event type")
        }
    }

    private fun getUserId(): Long {
        return accountProvider.getUser()?.id ?: 0L
    }
}
