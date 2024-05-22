package com.example.petstagram.fotoperfil

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.petstagram.R
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayImage


/**
 * foto de perfil
 *
 * This composable was generated from the UI Package 'foto_perfil'.
 * Generated code; do not edit directly
 */
@Composable
fun FotoPerfilBase(
    modifier: Modifier = Modifier,
    added :String= ""
) {
    TopLevelSizeEnorme(modifier = modifier) {
        ProfilePic(modifier = Modifier
            .rowWeight(1.0f)
            .columnWeight(1.0f), url = added)
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
fun ProfilePic(modifier: Modifier = Modifier, url: String) {
    Row{
        Box{

            this@Row.AnimatedVisibility(visible = url.isBlank(), enter = expandVertically()+ expandHorizontally(), exit = shrinkVertically()+ shrinkHorizontally()) {
                RelayImage(
                    image = painterResource(R.drawable.hacer_clic),
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .fillMaxWidth(1.0f)
                        .fillMaxHeight(1.0f)

                )
            }
            this@Row.AnimatedVisibility(visible = url.isNotBlank(), enter = expandVertically()+ expandHorizontally(), exit = shrinkVertically()+ shrinkHorizontally()) {

                AsyncImage(model = url,
                    contentDescription = "profilePic",
                    modifier = modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            this@Row.AnimatedVisibility(visible = url == "empty", enter = expandVertically()+ expandHorizontally(), exit = shrinkVertically()+ shrinkHorizontally()) {

                RelayImage(
                    image = painterResource(R.drawable.foto_perfil_foto),
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .fillMaxWidth(1.0f)
                        .fillMaxHeight(1.0f)

                )
            }
        }
    }
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
