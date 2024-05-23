package com.example.petstagram.barrasuperior

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.petstagram.R
import com.example.petstagram.ui.petstagram.accesoaperfil.AccesoAperfil
import com.example.petstagram.atras.Atras
import com.example.petstagram.ui.theme.Primary
import com.google.relay.compose.CrossAxisAlignment
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayImage

// Design to select for BarraSuperior
enum class Variant {
    Simple,
    WithMenu
}

/**UI representation for the top bar of the app, interacts with [navController] to navigate*/
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    variant: Variant = Variant.Simple,
    navController: NavHostController
) {
    when (variant) {
        Variant.Simple -> TopLevelVarianteSimple(modifier = modifier) {
            AccesoAPerfilAccesoAPerfil(navController = navController)
            ImagenDeslizableVarianteSimple(Modifier
                .clickable { navController.navigate("categorias") })
        }

        Variant.WithMenu ->
            BoxWithConstraints {
                val altomax = maxHeight
                TopLevelVarianteConMenu(modifier = modifier) {
                    AccesoAPerfilAccesoAPerfil(
                        modifier = Modifier.height(altomax.times(0.65f)),
                        navController = navController
                    )
                    IconosVarianteConMenu(modifier = Modifier.height(altomax.times(0.35f))) {
                        ContenedorDeslizarVarianteConMenu(
                            modifier = Modifier
                                .rowWeight(1.0f)
                                .columnWeight(1.0f)
                        ) {
                            ImagenDeslizableVarianteConMenu(Modifier
                                .clickable { navController.navigate("categorias") })
                        }
                        ContenedorAtrasVarianteConMenu(modifier = Modifier.rowWeight(1.0f)) {
                            AtrasAtras(
                                Modifier
                                    .height(altomax.times(0.35f))
                                    .clickable { navController.navigateUp() })
                        }
                    }
                }

            }
    }
}


@Composable
fun AccesoAPerfilAccesoAPerfil(modifier: Modifier = Modifier, navController: NavHostController) {
    AccesoAperfil(modifier = modifier.requiredWidth(360.0.dp), navController = navController)
}

@Composable
fun ImagenDeslizableVarianteSimple(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.barra_superior_imagen_deslizable),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .requiredWidth(32.0.dp)
            .requiredHeight(32.0.dp)
    )
}

@Composable
fun TopLevelVarianteSimple(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Primary,
        padding = PaddingValues(
            start = 0.0.dp,
            top = 0.0.dp,
            end = 0.0.dp,
            bottom = 8.0.dp
        ),
        itemSpacing = 16.0,
        clipToParent = false,
        radius = 6.0,
        content = content,
        modifier = modifier
    )
}

@Composable
fun ImagenDeslizableVarianteConMenu(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.barra_superior_imagen_deslizable),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .requiredWidth(32.0.dp)
            .requiredHeight(32.0.dp)
    )
}

@Composable
fun ContenedorDeslizarVarianteConMenu(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        mainAxisAlignment = MainAxisAlignment.End,
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(
            start = 0.0.dp,
            top = 8.0.dp,
            end = 0.0.dp,
            bottom = 8.0.dp
        ),
        itemSpacing = 10.0,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}

@Composable
fun AtrasAtras(modifier: Modifier = Modifier) {
    Atras(
        modifier = modifier
            .requiredWidth(56.0.dp)
    )
}

@Composable
fun ContenedorAtrasVarianteConMenu(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        crossAxisAlignment = CrossAxisAlignment.End,
        itemSpacing = 10.0,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}

@Composable
fun IconosVarianteConMenu(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        arrangement = RelayContainerArrangement.Row,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}

@Composable
fun TopLevelVarianteConMenu(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Primary,
        itemSpacing = 8.0,
        clipToParent = false,
        radius = 6.0,
        content = content,
        modifier = modifier
    )
}
