package com.example.petstagram.perfilpropio

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
import com.example.petstagram.ViewModels.PrincipalViewModel
import com.example.petstagram.cuadrotexto.CuadroTexto
import com.example.petstagram.cuadrotexto.Variacion
import com.example.petstagram.menuprincipal.BarraSuperiorInstance
import com.example.petstagram.perfil.FotoPerfilInstance
import com.example.petstagram.publicaciones.Publicaciones
import com.example.petstagram.visualizarcategoria.TopLevel
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope

/**
 * perfil propio
 *
 * This composable was generated from the UI Package 'perfil_propio'.
 * Generated code; do not edit directly
 */
@Composable
fun PerfilPropio(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: PrincipalViewModel
) {
    BoxWithConstraints {
        val AlturaTotal = maxHeight
        TopLevel(modifier = modifier) {
            BarraSuperiorInstance(modifier = Modifier.rowWeight(1.0f).height(AlturaTotal.times(0.23f)),navController = navController)
            CuadroTextoInstance(Modifier.height(AlturaTotal.times(0.06f)))
            FotoPerfilInstance(Modifier.height(AlturaTotal.times(0.30f)).width(AlturaTotal.times(0.30f)))
            CuadroTexto1(Modifier.height(AlturaTotal.times(0.06f)))
            PublicacionesCuenta(modifier = Modifier.rowWeight(1.0f).height(AlturaTotal.times(0.48f)))
        }
    }

}




@Composable
fun CuadroTextoInstance(modifier: Modifier = Modifier) {
    CuadroTexto(
        variacion = Variacion.TuPerfil,
        modifier = modifier.requiredWidth(94.0.dp)
    )
}

@Composable
fun CuadroTexto1(modifier: Modifier = Modifier) {
    CuadroTexto(
        variacion = Variacion.TusPublicaciones,
        modifier = modifier.requiredWidth(186.0.dp)
    )
}

@Composable
fun PublicacionesCuenta(modifier: Modifier = Modifier) {
    Publicaciones(modifier)
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
