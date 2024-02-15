package com.example.petstagram.menuprincipal

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.petstagram.ui.petstagram.barradeselecciondetipodepublicacion.BarraDeSeleccionDeTipoDePublicacion
import com.example.petstagram.barrasuperior.BarraSuperior
import com.example.petstagram.categorias.Categorias
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.ScrollAnchor

/**
 * menu principal
 *
 * This composable was generated from the UI Package 'menu_principal'.
 * Generated code; do not edit directly
 */
@Composable
fun MenuPrincipal(modifier: Modifier = Modifier) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val mayorAltura = maxHeight
        TopLevel(modifier = modifier) {
            BarraSuperiorInstance(modifier = Modifier.rowWeight(1.0f).requiredHeight(mayorAltura.times(0.22f)))
            BarraTipoNotificacion(modifier = Modifier.rowWeight(1.0f).requiredHeight(mayorAltura.times(0.07f)))
            CategoriasInstance(modifier = Modifier.rowWeight(1.0f).requiredHeight(mayorAltura.times(0.84f)))
        }
    }

}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun MenuPrincipalPreview() {
    MaterialTheme {
        RelayContainer {
            MenuPrincipal(modifier = Modifier
                .rowWeight(1.0f)
                .columnWeight(1.0f))
        }
    }
}

@Composable
fun BarraSuperiorInstance(modifier: Modifier = Modifier) {
    BarraSuperior(modifier = modifier.fillMaxWidth(1.0f))
}

@Composable
fun BarraTipoNotificacion(modifier: Modifier = Modifier) {
    BarraDeSeleccionDeTipoDePublicacion(modifier = modifier.fillMaxWidth(1.0f))
}

@Composable
fun CategoriasInstance(modifier: Modifier = Modifier) {

    Categorias(modifier = modifier
        .fillMaxWidth()
    )

}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 35,
            green = 35,
            blue = 35
        ),
        mainAxisAlignment = MainAxisAlignment.Center,
        scrollAnchor = ScrollAnchor.Start,
        scrollable = true,
        itemSpacing = 8.0,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
