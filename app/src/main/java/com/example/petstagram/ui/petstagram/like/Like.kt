package com.example.petstagram.like

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
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayImage

// Design to select for Like
enum class Pressed {
    False,
    True
}

/**
 * like
 *
 * This composable was generated from the UI Package 'like'.
 * Generated code; do not edit directly
 */
@Composable
fun Like(
    modifier: Modifier = Modifier,
    pressed: Pressed = Pressed.False
) {
    when (pressed) {
        Pressed.False -> TopLevelPulsadoFalse(modifier = modifier) {
            ImagenLikePulsadoFalse()
        }
        Pressed.True -> TopLevelPulsadoTrue(modifier = modifier) {
            ImagenLikePulsadoTrue()
        }
    }
}

@Preview(widthDp = 32, heightDp = 32)
@Composable
private fun LikePulsadoFalsePreview() {
    MaterialTheme {
        RelayContainer {
            Like(
                pressed = Pressed.False,
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Preview(widthDp = 32, heightDp = 32)
@Composable
private fun LikePulsadoTruePreview() {
    MaterialTheme {
        RelayContainer {
            Like(
                pressed = Pressed.True,
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Composable
fun ImagenLikePulsadoFalse(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.like_imagen_like),
        contentScale = ContentScale.Crop,
        modifier = modifier.requiredWidth(32.0.dp).requiredHeight(32.0.dp)
    )
}

@Composable
fun TopLevelPulsadoFalse(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 60,
            green = 60,
            blue = 60
        ),
        isStructured = false,
        radius = 16.0,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun ImagenLikePulsadoTrue(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.like_imagen_like),
        contentScale = ContentScale.Crop,
        modifier = modifier.requiredWidth(32.0.dp).requiredHeight(32.0.dp)
    )
}

@Composable
fun TopLevelPulsadoTrue(
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
        isStructured = false,
        radius = 16.0,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}
