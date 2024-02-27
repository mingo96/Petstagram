package com.example.petstagram.publicacion

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.petstagram.R
import com.example.petstagram.UiData.Post
import com.example.petstagram.cuadroinfo.CuadroInfo
import com.example.petstagram.cuadroinfo.Variacion
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayImage

/**
 * publicacion
 *
 * This composable was generated from the UI Package 'publicacion'.
 * Generated code; do not edit directly
 */
@Composable
fun Publicacion(modifier: Modifier = Modifier, post: Post) {
    TopLevel(modifier = modifier) {
        CuadroInfoInstance(modifier = Modifier.rowWeight(1.0f), post = post)
        ImagenPublicacion(modifier = Modifier.rowWeight(1.0f), post = post)
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
fun ImagenPublicacion(modifier: Modifier = Modifier, post: Post) {
    RelayImage(
        image = painterResource(R.drawable.publicacion_imagen_publicacion),
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxWidth(1.0f).requiredHeight(264.0.dp)
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
