package com.example.petstagram.visualizarcategoria

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.petstagram.barrasuperior.BarraSuperior
import com.example.petstagram.barrasuperior.Variante
import com.example.petstagram.cuadrotexto.CuadroTexto
import com.example.petstagram.cuadrotexto.Variacion
import com.example.petstagram.publicaciones.Publicaciones
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope

/**
 * Pantalla de categoria
 *
 * This composable was generated from the UI Package 'visualizar_categoria'.
 * Generated code; do not edit directly
 */
@Composable
fun VisualizarCategoria(modifier: Modifier = Modifier, navController: NavHostController) {
    BoxWithConstraints {
        val AlturaTotal = maxHeight
        TopLevel(modifier = modifier) {
            BarraSuperiorInstance(modifier = Modifier.rowWeight(1.0f).height(AlturaTotal.times(0.2225f)), navController = navController)
            CuadroTextoInstance(modifier.requiredHeight(AlturaTotal.times(0.05f)))
            PublicacionesInstance(modifier = Modifier.rowWeight(1.0f).height(AlturaTotal.times(0.825f)))
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
    CuadroTexto(
        variacion = Variacion.PublicacionesCategoria,
        modifier = modifier
    )
}

@Composable
fun PublicacionesInstance(modifier: Modifier = Modifier) {
    Publicaciones(modifier = modifier
        .fillMaxWidth(1.0f))
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
        mainAxisAlignment = MainAxisAlignment.End,
        scrollable = true,
        itemSpacing = 24.0,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
