package com.example.petstagram.publicacion

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.petstagram.UiData.Post
import com.example.petstagram.ViewModels.PostsViewModel
import com.example.petstagram.cuadroinfo.CuadroInfo
import com.example.petstagram.cuadroinfo.Variacion
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope

/**
 * publicacion
 *
 * This composable was generated from the UI Package 'publicacion'.
 * Generated code; do not edit directly
 */
@Composable
fun Publicacion(modifier: Modifier = Modifier, post: Post, imageUrl: String) {
    TopLevel(modifier = modifier) {
        CuadroInfoInstance(modifier = Modifier.rowWeight(1.0f), post = post)
        ImagenPublicacion(modifier = Modifier.rowWeight(1.0f), post = post, imageUrl = imageUrl)
        BotonesPublicacion(modifier = Modifier.rowWeight(1.0f), post = post)
    }
}

@Composable
fun CuadroInfoInstance(modifier: Modifier = Modifier, post: Post) {
    CuadroInfo(
        variacion = Variacion.Superior,
        modifier = modifier.fillMaxWidth(1.0f).requiredHeight(48.0.dp),
        added = post
    )
}

@Composable
fun ImagenPublicacion(modifier: Modifier = Modifier, post: Post, imageUrl: String) {
    AsyncImage(
        model = imageUrl,
        contentDescription = post.title,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun BotonesPublicacion(modifier: Modifier = Modifier, post: Post) {
    CuadroInfo(
        modifier = modifier.fillMaxWidth(1.0f).requiredHeight(48.0.dp),
        variacion = Variacion.Inferior,
        added = post
    )
}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 217,
            green = 217,
            blue = 217
        ),
        mainAxisAlignment = MainAxisAlignment.Start,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}
