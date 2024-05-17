package com.example.petstagram.fotoperfil

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
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
fun FotoPerfilBase(
    modifier: Modifier = Modifier,
    size: Size = Size.Peque,
    added :String= ""
) {
    when (size) {
        Size.Peque -> TopLevelSizePeque(modifier = modifier) {
            ProfilePic(modifier = Modifier
                .rowWeight(1.0f)
                .columnWeight(1.0f), url = added)
        }
        Size.Enorme -> TopLevelSizeEnorme(modifier = modifier) {
            ProfilePic(modifier = Modifier
                .rowWeight(1.0f)
                .columnWeight(1.0f), url = added)
        }
    }
}

@Preview(widthDp = 32, heightDp = 32)
@Composable
private fun FotoPerfilSizePequePreview() {
    MaterialTheme {
        RelayContainer {
            FotoPerfilBase(
                size = Size.Peque,
                modifier = Modifier
                    .rowWeight(1.0f)
                    .columnWeight(1.0f)
            )
        }
    }
}

@Preview(widthDp = 216, heightDp = 216)
@Composable
private fun FotoPerfilSizeEnormePreview() {
    MaterialTheme {
        RelayContainer {
            FotoPerfilBase(
                size = Size.Enorme,
                modifier = Modifier
                    .rowWeight(1.0f)
                    .columnWeight(1.0f)
            )
        }
    }
}

@Composable
fun FotoSizePeque(modifier: Modifier = Modifier, url: String) {
    RelayImage(
        image = painterResource(R.drawable.foto_perfil_foto),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
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
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}

@Composable
fun ProfilePic(modifier: Modifier = Modifier, url: String) {
    if(url == "")
        RelayImage(
            image = painterResource(R.drawable.hacer_clic),
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxWidth(1.0f)
                .fillMaxHeight(1.0f)
                
        )
    else
        AsyncImage(model = url,
            contentDescription = "profilePic",
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
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
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
