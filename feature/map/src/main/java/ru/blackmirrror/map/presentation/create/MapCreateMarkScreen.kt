package ru.blackmirrror.map.presentation.create

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import ru.blackmirrror.component.R
import ru.blackmirrror.component.ui.TextFieldMultiline
import ru.blackmirrror.component.ui.TextFieldOneLine
import ru.blackmirrror.core.state.ScreenState
import ru.blackmirrror.core.uriToFile
import ru.blackmirrror.map.data.MarkDto
import ru.blackmirrror.map.domain.Category
import ru.blackmirrror.map.domain.toCategory
import ru.blackmirrror.map.presentation.DragHandleBar
import ru.blackmirrror.map.presentation.RatingStars
import ru.blackmirrror.map.presentation.TypeSelection

@Composable
fun MapCreateMarkScreen() {

    val vm: CreateMarkViewModel = hiltViewModel()
    val state by vm.state.collectAsState()

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = uri
            val file = uriToFile(context, it)
            if (file != null) {
                vm.processEvent(CreateMarkEvent.EditImageFile(file))
            }
        }
    }

    MapCreateMarkContent(
        state = state,
        onIntent = { vm.processEvent(it) },
        imageUri = imageUri,
        onPickImage = { launcher.launch("image/*") }
    )
}

@Composable
fun MapCreateMarkContent(
    state: ScreenState<MarkDto>,
    onIntent: (CreateMarkEvent) -> Unit,
    imageUri: Uri?,
    onPickImage: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        DragHandleBar()

        Spacer(modifier = Modifier.height(8.dp))

        Text(stringResource(R.string.map_create_mark_new), fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)

        Text("${state.data?.latitude} ${state.data?.longitude}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)

        Spacer(modifier = Modifier.height(8.dp))

        ImagePickerCard(
            imageUri = imageUri,
            onClick = onPickImage
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextFieldOneLine(
            value = state.data?.title ?: "",
            onValueChange = { onIntent(CreateMarkEvent.EditTitle(it)) },
            label = stringResource(R.string.map_create_mark_title)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextFieldMultiline(
            value = state.data?.description ?: "",
            onValueChange = { onIntent(CreateMarkEvent.EditDescription(it)) },
            label = stringResource(R.string.map_create_mark_description)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(stringResource(R.string.map_create_mark_the_categories_of_place), fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(16.dp))

        TypeSelection(state.data?.categories?.map { it.toCategory() }?.toSet() ?: setOf(Category.CAMPING)) { onIntent(CreateMarkEvent.CategorySelected(it)) }

        Spacer(modifier = Modifier.height(16.dp))

        Text(stringResource(R.string.map_create_mark_rating), fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)

        RatingStars(state.data?.averageRating?.toInt() ?: 3) {
            onIntent(
                CreateMarkEvent.EditRating(
                    it
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onIntent(CreateMarkEvent.Create) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(stringResource(R.string.map_create_mark_btn_save), fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun ImagePickerCard(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_camera),
                contentDescription = "Выбрать фото",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
