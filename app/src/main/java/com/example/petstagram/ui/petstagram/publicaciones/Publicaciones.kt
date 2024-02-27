package com.example.petstagram.publicaciones

import android.util.Log
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.example.petstagram.UiData.Post
import com.example.petstagram.ViewModels.PostsViewModel
import com.example.petstagram.publicacion.Publicacion
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope

/**
 * publicaciones
 *
 * This composable was generated from the UI Package 'publicaciones'.
 * Generated code; do not edit directly
 */
@Composable
fun Publicaciones(modifier: Modifier = Modifier, viewModel: PostsViewModel) {
    BoxWithConstraints {
        val posts by viewModel.posts.collectAsState()
        val anchoMax = maxWidth
        TopLevel(modifier = modifier.width(maxWidth)) {
            for (i in posts){
                Log.i(i.first, i.second.id)
                PublicacionInstance(ancho = anchoMax, post = i.second, url = i.first)
            }
        }
    }
}

@Composable
fun PublicacionInstance(
    modifier: Modifier = Modifier,
    ancho: Dp,
    post: Post,
    url: String
) {
    Publicacion(modifier = modifier
        .fillMaxWidth(1.0f)
        .width(ancho),
        post = post,
        imageUrl = url)
}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 224,
            green = 164,
            blue = 0
        ),
        mainAxisAlignment = MainAxisAlignment.Start,
        scrollable = true,
        itemSpacing = 8.0,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
