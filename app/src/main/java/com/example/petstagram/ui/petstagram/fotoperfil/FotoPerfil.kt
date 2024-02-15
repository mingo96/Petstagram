package com.example.petstagram.fotoperfil

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.petstagram.R
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayImage

// Design to select for FotoPerfil
enum class Size {
    Peque,
    Enorme
}

/**
 * foto de perfil
 *
 * This composable was generated from the UI Package 'foto_perfil'.
 * Generated code; do not edit directly
 */
@Composable
fun FotoPerfil(
    modifier: Modifier = Modifier,
    size: Size = Size.Peque
) {
    when (size) {
        Size.Peque -> TopLevelSizePeque(modifier = modifier) {
            FotoSizePeque(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
        Size.Enorme -> TopLevelSizeEnorme(modifier = modifier) {
            FotoSizeEnorme(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
    }
}

@Preview(widthDp = 32, heightDp = 32)
@Composable
private fun FotoPerfilSizePequePreview() {
    MaterialTheme {
        RelayContainer {
            FotoPerfil(
                size = Size.Peque,
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Preview(widthDp = 216, heightDp = 216)
@Composable
private fun FotoPerfilSizeEnormePreview() {
    MaterialTheme {
        RelayContainer {
            FotoPerfil(
                size = Size.Enorme,
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Composable
fun FotoSizePeque(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.foto_perfil_foto),
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun TopLevelSizePeque(
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
        isStructured = false,
        radius = 16.0,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun FotoSizeEnorme(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.foto_perfil_foto),
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun TopLevelSizeEnorme(
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
        isStructured = false,
        radius = 120.0,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}
