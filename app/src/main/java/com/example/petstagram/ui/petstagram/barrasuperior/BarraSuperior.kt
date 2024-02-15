package com.example.petstagram.barrasuperior

import androidx.compose.foundation.layout.PaddingValues
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
import com.example.petstagram.accesoaperfil.AccesoAperfil
import com.example.petstagram.atras.Atras
import com.google.relay.compose.CrossAxisAlignment
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayImage

// Design to select for BarraSuperior
enum class Variante {
    Simple,
    ConMenu
}

/**
 * This composable was generated from the UI Package 'barra_superior'.
 * Generated code; do not edit directly
 */
@Composable
fun BarraSuperior(
    modifier: Modifier = Modifier,
    variante: Variante = Variante.Simple
) {
    when (variante) {
        Variante.Simple -> TopLevelVarianteSimple(modifier = modifier) {
            AccesoAPerfilAccesoAPerfil()
            ImagenDeslizableVarianteSimple()
        }
        Variante.ConMenu -> TopLevelVarianteConMenu(modifier = modifier) {
            AccesoAPerfilAccesoAPerfil()
            IconosVarianteConMenu(modifier = Modifier.rowWeight(1.0f)) {
                ContenedorDeslizarVarianteConMenu(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)) {
                    ImagenDeslizableVarianteConMenu()
                }
                ContenedorAtrasVarianteConMenu(modifier = Modifier.rowWeight(1.0f)) {
                    AtrasAtras()
                }
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 168)
@Composable
private fun BarraSuperiorVarianteSimplePreview() {
    MaterialTheme {
        BarraSuperior(variante = Variante.Simple)
    }
}

@Preview(widthDp = 360, heightDp = 168)
@Composable
private fun BarraSuperiorVarianteConMenuPreview() {
    MaterialTheme {
        BarraSuperior(variante = Variante.ConMenu)
    }
}

@Composable
fun AccesoAPerfilAccesoAPerfil(modifier: Modifier = Modifier) {
    AccesoAperfil(modifier = modifier.requiredWidth(360.0.dp))
}

@Composable
fun ImagenDeslizableVarianteSimple(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.barra_superior_imagen_deslizable),
        contentScale = ContentScale.Crop,
        modifier = modifier.requiredWidth(32.0.dp).requiredHeight(32.0.dp)
    )
}

@Composable
fun TopLevelVarianteSimple(
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
        modifier = modifier.requiredWidth(32.0.dp).requiredHeight(32.0.dp)
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
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun AtrasAtras(modifier: Modifier = Modifier) {
    Atras(modifier = modifier.requiredWidth(56.0.dp).requiredHeight(48.0.dp))
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
        modifier = modifier.fillMaxWidth(1.0f).requiredHeight(48.0.dp)
    )
}

@Composable
fun TopLevelVarianteConMenu(
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
        itemSpacing = 8.0,
        clipToParent = false,
        radius = 6.0,
        content = content,
        modifier = modifier
    )
}
