package com.example.petstagram.perfil

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petstagram.barrasuperior.BarraSuperior
import com.example.petstagram.barrasuperior.Variante
import com.example.petstagram.cuadrotexto.CuadroTexto
import com.example.petstagram.fotoperfil.FotoPerfil
import com.example.petstagram.fotoperfil.Size
import com.example.petstagram.opcionperfil.OpcionPerfil
import com.example.petstagram.publicaciones.Publicaciones
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope

/**
 * perfil ajeno
 *
 * This composable was generated from the UI Package 'perfil'.
 * Generated code; do not edit directly
 */
@Composable
fun Perfil(modifier: Modifier = Modifier) {
    TopLevel(modifier = modifier) {
        BarraSuperiorInstance(modifier = Modifier.rowWeight(1.0f))
        CuadroTextoInstance(modifier = Modifier.rowWeight(1.0f))
        FotoPerfilInstance()
        OpcionPerfilInstance()
        PublicacionesCuenta(modifier = Modifier.rowWeight(1.0f))
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun PerfilPreview() {
    MaterialTheme {
        RelayContainer {
            Perfil(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
    }
}

@Composable
fun BarraSuperiorInstance(modifier: Modifier = Modifier) {
    BarraSuperior(
        variante = Variante.ConMenu,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}

@Composable
fun CuadroTextoInstance(modifier: Modifier = Modifier) {
    CuadroTexto(modifier = modifier.fillMaxWidth(1.0f))
}

@Composable
fun FotoPerfilInstance(modifier: Modifier = Modifier) {
    FotoPerfil(
        size = Size.Enorme,
        modifier = modifier.requiredWidth(216.0.dp).requiredHeight(216.0.dp)
    )
}

@Composable
fun OpcionPerfilInstance(modifier: Modifier = Modifier) {
    OpcionPerfil(modifier = modifier.requiredWidth(312.0.dp).requiredHeight(48.0.dp))
}

@Composable
fun PublicacionesCuenta(modifier: Modifier = Modifier) {
    Publicaciones(modifier = modifier.fillMaxWidth(1.0f).requiredHeight(472.0.dp))
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
        scrollable = true,
        itemSpacing = 24.0,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}
