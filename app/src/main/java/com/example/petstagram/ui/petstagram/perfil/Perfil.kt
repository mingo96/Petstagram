package com.example.petstagram.perfil

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
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
fun Perfil(modifier: Modifier = Modifier, navController: NavHostController) {
    BoxWithConstraints {
        val AlturaTotal = maxHeight
        TopLevel(modifier = modifier) {
            BarraSuperiorInstance(modifier = Modifier.rowWeight(1.0f).height(AlturaTotal.times(0.23f)), navController = navController)
            CuadroTextoInstance(modifier = Modifier.rowWeight(1.0f).height(AlturaTotal.times(0.06f)))
            FotoPerfilInstance(modifier.height(AlturaTotal.times(0.30f)).width(AlturaTotal.times(0.30f)))
            OpcionPerfilInstance(modifier.height(AlturaTotal.times(0.06f)))
            PublicacionesCuenta(modifier = Modifier.rowWeight(1.0f).height(AlturaTotal.times(0.48f)))
        }
    }
}



@Composable
fun BarraSuperiorInstance(modifier: Modifier = Modifier, navController: NavHostController) {
    BarraSuperior(
        modifier = modifier.fillMaxWidth(1.0f),
        variante = Variante.ConMenu,
        navController = navController
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
        modifier = modifier
    )
}

@Composable
fun OpcionPerfilInstance(modifier: Modifier = Modifier) {
    OpcionPerfil(modifier = modifier.requiredWidth(312.0.dp))
}

@Composable
fun PublicacionesCuenta(modifier: Modifier = Modifier) {
    Publicaciones(modifier = modifier.fillMaxWidth(1.0f))
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
