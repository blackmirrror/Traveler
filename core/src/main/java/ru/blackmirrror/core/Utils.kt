package ru.blackmirrror.core

import android.content.Context
import android.net.Uri
import java.io.File

fun uriToFile(context: Context, uri: Uri): File? {
    val inputStream = context.contentResolver.openInputStream(uri) ?: return null
    val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)
    tempFile.outputStream().use { output ->
        inputStream.copyTo(output)
    }
    return tempFile
}
