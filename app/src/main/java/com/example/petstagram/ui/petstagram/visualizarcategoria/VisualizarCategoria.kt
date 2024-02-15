package com.example.petstagram.visualizarcategoria

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
fun VisualizarCategoria(modifier: Modifier = Modifier) {
    TopLevel(modifier = modifier) {
        BarraSuperiorInstance(modifier = Modifier.rowWeight(1.0f))
        CuadroTextoInstance()
        PublicacionesInstance(modifier = Modifier.rowWeight(1.0f))
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun VisualizarCategoriaPreview() {
    MaterialTheme {
        RelayContainer {
            VisualizarCategoria(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
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
    CuadroTexto(
        variacion = Variacion.PublicacionesCategoria,
        modifier = modifier.requiredWidth(296.0.dp)
    )
}

@Composable
fun PublicacionesInstance(modifier: Modifier = Modifier) {
    Publicaciones(modifier = modifier.fillMaxWidth(1.0f).requiredHeight(692.0.dp))
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
        itemSpacing = 8.0,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}
