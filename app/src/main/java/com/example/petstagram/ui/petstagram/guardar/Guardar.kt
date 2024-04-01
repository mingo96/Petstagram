package com.example.petstagram.guardar

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

// Design to select for Guardar
enum class SavePressed {
    No, Si
}

/**
 * guardar
 *
 * This composable was generated from the UI Package 'guardar'.
 * Generated code; do not edit directly
 */
@Composable
fun Guardar(
    modifier: Modifier = Modifier,
    savePressed: SavePressed = SavePressed.No
) {
    when (savePressed) {
        SavePressed.No -> TopLevelGuardarPulsadoNo(modifier = modifier) {
            ImagenGuardarGuardarPulsadoNo(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
        SavePressed.Si -> TopLevelGuardarPulsadoSi(modifier = modifier) {
            ImagenGuardarGuardarPulsadoSi(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
    }
}

@Preview(widthDp = 32, heightDp = 32)
@Composable
private fun GuardarGuardarPulsadoNoPreview() {
    MaterialTheme {
        RelayContainer {
            Guardar(
                savePressed = SavePressed.No,
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Preview(widthDp = 32, heightDp = 32)
@Composable
private fun GuardarGuardarPulsadoSiPreview() {
    MaterialTheme {
        RelayContainer {
            Guardar(
                savePressed = SavePressed.Si,
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Composable
fun ImagenGuardarGuardarPulsadoNo(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.guardar_imagen_guardar_modified),
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun TopLevelGuardarPulsadoNo(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 0,
            green = 0,
            blue = 0
        ),
        isStructured = false,
        radius = 4.5,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun ImagenGuardarGuardarPulsadoSi(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.guardar_imagen_guardar_modified),
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun TopLevelGuardarPulsadoSi(
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
        radius = 4.5,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}
