package com.example.petstagram.publicaciones

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
fun Publicaciones(modifier: Modifier = Modifier) {
    BoxWithConstraints {
        val anchoMax = maxWidth
        TopLevel(modifier = modifier.width(maxWidth)) {
            PublicacionInstance(modifier = Modifier.rowWeight(1.0f),anchoMax)
            PublicacionInstance(modifier = Modifier.rowWeight(1.0f),anchoMax)
            PublicacionInstance(modifier = Modifier.rowWeight(1.0f),anchoMax)
            PublicacionInstance(modifier = Modifier.rowWeight(1.0f),anchoMax)
            PublicacionInstance(modifier = Modifier.rowWeight(1.0f),anchoMax)
        }
    }
}

@Preview(widthDp = 344, heightDp = 752)
@Composable
private fun PublicacionesPreview() {
    MaterialTheme {
        RelayContainer {
            Publicaciones(modifier = Modifier
                .rowWeight(1.0f)
                .columnWeight(1.0f))
        }
    }
}

@Composable
fun PublicacionInstance(modifier: Modifier = Modifier, ancho : Dp) {
    Publicacion(modifier = modifier
        .fillMaxWidth(1.0f).width(ancho))
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
