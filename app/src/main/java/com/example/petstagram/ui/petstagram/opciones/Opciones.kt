package com.example.petstagram.opciones

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petstagram.R
import com.example.petstagram.ui.theme.Secondary
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayImage

/**
 * version peque de opciones
 *
 * This composable was generated from the UI Package 'opciones'.
 * Generated code; do not edit directly
 */
@Composable
fun Opciones(modifier: Modifier = Modifier) {
    TopLevel(modifier = modifier) {
        ImagenOpciones()
    }
}

@Preview(widthDp = 16, heightDp = 32)
@Composable
private fun OpcionesPreview() {
    MaterialTheme {
        RelayContainer {
            Opciones(
                modifier = Modifier
                    .rowWeight(1.0f)
                    .columnWeight(1.0f)
            )
        }
    }
}

@Composable
fun ImagenOpciones(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.opciones_imagen_opciones),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .requiredWidth(16.0.dp)
            .requiredHeight(32.0.dp)
    )
}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Secondary,
        isStructured = false,
        radius = 6.0,
        strokeWidth = 1.0,
        strokeColor = Color(
            alpha = 255,
            red = 35,
            green = 35,
            blue = 35
        ),
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
