package com.example.petstagram.ui.petstagram.accesoaperfil

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.petstagram.R
import com.example.petstagram.ui.theme.Primary
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayImage

/**
 * imagenes de barra superior
 *
 *
 * This composable was generated from the UI Package 'acceso_aperfil'.
 * Generated code; do not edit directly
 */
@Composable
fun AccesoAperfil(modifier: Modifier = Modifier, navController: NavHostController) {
    TopLevel(modifier = modifier) {
        ImagenPerfil(Modifier.clickable { navController.navigate("perfilPropio") })
        ImagenGaleria(Modifier.clickable { navController.navigate("guardadas") })
        ImagenPuerta(Modifier.clickable { navController.navigate("login") })
    }
}


@Composable
fun ImagenPerfil(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.acceso_aperfil_imagen_perfil),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .requiredWidth(80.0.dp)
            .requiredHeight(80.0.dp),
    )
}

@Composable
fun ImagenGaleria(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.acceso_aperfil_imagen_galeria),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .requiredWidth(91.0.dp)
            .requiredHeight(80.0.dp),
    )
}

@Composable
fun ImagenPuerta(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.acceso_aperfil_imagen_puerta),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .requiredWidth(80.0.dp)
            .requiredHeight(80.0.dp),
    )
}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Primary,
        mainAxisAlignment = MainAxisAlignment.SpaceBetween,
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(all = 16.0.dp),
        content = content,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}
