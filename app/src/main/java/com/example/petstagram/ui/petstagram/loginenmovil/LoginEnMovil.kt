package com.example.petstagram.loginenmovil

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petstagram.R
import com.example.petstagram.cuadrotexto.CuadroTexto
import com.example.petstagram.cuadrotexto.Variacion
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayImage
import com.google.relay.compose.RelayVector

/**
 * login
 *
 * This composable was generated from the UI Package 'login_en_movil'.
 * Generated code; do not edit directly
 */
@Composable
fun LoginEnMovil(modifier: Modifier = Modifier) {
    TopLevel(modifier = modifier) {
        ImagenInicioSesion(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        RectanguloDeCorte(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        TopLevelSynth(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)) {
            CuadroTextoInstance()
            CuadroTexto1(modifier = Modifier.rowWeight(1.0f))
            CuadroTexto2(modifier = Modifier.rowWeight(1.0f))
            CuadroTexto3(modifier = Modifier.rowWeight(1.0f))
            CuadroTexto4(modifier = Modifier.rowWeight(1.0f))
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun LoginEnMovilPreview() {
    MaterialTheme {
        RelayContainer {
            LoginEnMovil(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
    }
}

@Composable
fun ImagenInicioSesion(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.login_en_movil_imagen_inicio_sesion),
        contentScale = ContentScale.Crop,
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 0.0.dp,
                top = 0.0.dp,
                end = 0.0.dp,
                bottom = 484.0.dp
            )
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun RectanguloDeCorte(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.login_en_movil_rectangulo_de_corte),
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 0.0.dp,
                top = 206.5.dp,
                end = 0.0.dp,
                bottom = 480.0.dp
            )
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun CuadroTextoInstance(modifier: Modifier = Modifier) {
    CuadroTexto(
        variacion = Variacion.Bienvenida,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun CuadroTexto1(modifier: Modifier = Modifier) {
    CuadroTexto(
        variacion = Variacion.Usuario,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}

@Composable
fun CuadroTexto2(modifier: Modifier = Modifier) {
    CuadroTexto(
        variacion = Variacion.Clave,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}

@Composable
fun CuadroTexto3(modifier: Modifier = Modifier) {
    CuadroTexto(
        variacion = Variacion.Registro,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}

@Composable
fun CuadroTexto4(modifier: Modifier = Modifier) {
    CuadroTexto(
        variacion = Variacion.InicioSesion,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}

@Composable
fun TopLevelSynth(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        mainAxisAlignment = MainAxisAlignment.SpaceBetween,
        padding = PaddingValues(
            start = 24.0.dp,
            top = 200.0.dp,
            end = 24.0.dp,
            bottom = 70.0.dp
        ),
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f).alpha(alpha = 100.0f)
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
        isStructured = false,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}
