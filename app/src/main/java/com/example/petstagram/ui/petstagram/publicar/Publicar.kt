package com.example.petstagram.publicar

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.petstagram.ViewModels.PrincipalViewModel
import com.example.petstagram.barrasuperior.BarraSuperior
import com.example.petstagram.barrasuperior.Variante
import com.example.petstagram.cuadrotexto.CuadroTexto
import com.example.petstagram.cuadrotexto.Variacion
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope

/**
 * pantalla para publicar
 *
 *
 * This composable was generated from the UI Package 'publicar'.
 * Generated code; do not edit directly
 */
@Composable
fun Publicar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: PrincipalViewModel
) {
    TopLevel(modifier = modifier) {
        BarraSuperiorInstance(modifier = Modifier.rowWeight(1.0f), navController = navController)
        CuadroTextoInstance()
        CuadroTexto1()
        CuadroTexto2()
        CuadroTexto3()
        CuadroTexto4()
    }
}


@Composable
fun BarraSuperiorInstance(modifier: Modifier = Modifier, navController: NavHostController) {
    BarraSuperior(
        variante = Variante.ConMenu,
        modifier = modifier.fillMaxWidth(1.0f),
        navController = navController
    )
}

@Composable
fun CuadroTextoInstance(modifier: Modifier = Modifier) {
    CuadroTexto(
        variacion = Variacion.CrearPublicacion,
        modifier = modifier.requiredWidth(280.0.dp)
    )
}

@Composable
fun CuadroTexto1(modifier: Modifier = Modifier) {
    CuadroTexto(
        variacion = Variacion.SeleccionarRecurso,
        modifier = modifier.requiredWidth(296.0.dp)
    )
}

@Composable
fun CuadroTexto2(modifier: Modifier = Modifier) {
    CuadroTexto(
        variacion = Variacion.TituloPublicacion,
        modifier = modifier.requiredWidth(296.0.dp)
    )
}

@Composable
fun CuadroTexto3(modifier: Modifier = Modifier) {
    CuadroTexto(
        variacion = Variacion.CategoriaPublicacion,
        modifier = modifier.requiredWidth(296.0.dp)
    )
}

@Composable
fun CuadroTexto4(modifier: Modifier = Modifier) {
    CuadroTexto(
        variacion = Variacion.Publicar,
        modifier = modifier.requiredWidth(296.0.dp)
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
        mainAxisAlignment = MainAxisAlignment.End,
        scrollable = true,
        itemSpacing = 80.0,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}
