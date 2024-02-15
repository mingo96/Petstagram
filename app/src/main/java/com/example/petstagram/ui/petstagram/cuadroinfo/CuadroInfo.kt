package com.example.petstagram.cuadroinfo

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.petstagram.fotoperfil.FotoPerfil
import com.example.petstagram.guardar.Guardar
import com.example.petstagram.like.Like
import com.example.petstagram.opciones.Opciones
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText

// Design to select for CuadroInfo
enum class Variacion {
    Default,
    Superior,
    Inferior
}

/**
 * cuadros de info
 *
 * This composable was generated from the UI Package 'cuadro_info'.
 * Generated code; do not edit directly
 */
@Composable
fun CuadroInfo(
    modifier: Modifier = Modifier,
    variacion: Variacion = Variacion.Default
) {
    when (variacion) {
        Variacion.Default -> TopLevelVariacionDefault(modifier = modifier) {}
        Variacion.Superior -> TopLevelVariacionSuperior(modifier = modifier) {
            FotoPerfilSizePeque()
            TextoNombrePerfilVariacionSuperior()
            OpcionesOpciones()
        }
        Variacion.Inferior -> TopLevelVariacionInferior(modifier = modifier) {
            ContenedorBotonesIzquierdaVariacionInferior {
                LikePulsadoFalse()
                BotonSeccionComentariosVariacionInferior {
                    TextoBotonComentariosVariacionInferior(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
                }
            }
            GuardarGuardarPulsadoNo()
        }
    }
}

@Preview(widthDp = 395, heightDp = 48)
@Composable
private fun CuadroInfoVariacionDefaultPreview() {
    MaterialTheme {
        RelayContainer {
            CuadroInfo(
                variacion = Variacion.Default,
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 48)
@Composable
private fun CuadroInfoVariacionSuperiorPreview() {
    MaterialTheme {
        RelayContainer {
            CuadroInfo(
                variacion = Variacion.Superior,
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 48)
@Composable
private fun CuadroInfoVariacionInferiorPreview() {
    MaterialTheme {
        RelayContainer {
            CuadroInfo(
                variacion = Variacion.Inferior,
                modifier = Modifier.rowWeight(1.0f)
            )
        }
    }
}

@Composable
fun TopLevelVariacionDefault(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 0,
            green = 0,
            blue = 0
        ),
        isStructured = false,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun FotoPerfilSizePeque(modifier: Modifier = Modifier) {
    FotoPerfil(modifier = modifier.requiredWidth(32.0.dp).requiredHeight(32.0.dp))
}

@Composable
fun TextoNombrePerfilVariacionSuperior(modifier: Modifier = Modifier) {
    RelayText(
        content = "\${nombrePerfil}",
        fontSize = 15.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 225,
            green = 196,
            blue = 1
        ),
        height = 1.2102272033691406.em,
        textAlign = TextAlign.Left,
        fontWeight = FontWeight(700.0.toInt()),
        maxLines = -1,
        modifier = modifier.requiredWidth(264.0.dp).requiredHeight(32.0.dp).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun OpcionesOpciones(modifier: Modifier = Modifier) {
    Opciones(modifier = modifier.requiredWidth(16.0.dp).requiredHeight(32.0.dp))
}

@Composable
fun TopLevelVariacionSuperior(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 0,
            green = 0,
            blue = 0
        ),
        mainAxisAlignment = MainAxisAlignment.SpaceBetween,
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(all = 8.0.dp),
        strokeWidth = 1.0,
        strokeColor = Color(
            alpha = 255,
            red = 0,
            green = 0,
            blue = 0
        ),
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun LikePulsadoFalse(modifier: Modifier = Modifier) {
    Like(modifier = modifier.requiredWidth(32.0.dp).requiredHeight(32.0.dp))
}

@Composable
fun TextoBotonComentariosVariacionInferior(modifier: Modifier = Modifier) {
    RelayText(
        content = "Sección de comentarios",
        fontSize = 12.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 35,
            green = 35,
            blue = 35
        ),
        height = 1.2102272510528564.em,
        maxLines = -1,
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 8.800048828125.dp,
                top = 8.0.dp,
                end = 8.799957275390625.dp,
                bottom = 8.0.dp
            )
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun BotonSeccionComentariosVariacionInferior(
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
        isStructured = false,
        radius = 15.0,
        strokeWidth = 1.0,
        strokeColor = Color(
            alpha = 38,
            red = 0,
            green = 0,
            blue = 0
        ),
        content = content,
        modifier = modifier.requiredWidth(176.0.dp).requiredHeight(32.0.dp)
    )
}

@Composable
fun ContenedorBotonesIzquierdaVariacionInferior(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        mainAxisAlignment = MainAxisAlignment.SpaceBetween,
        arrangement = RelayContainerArrangement.Row,
        content = content,
        modifier = modifier.requiredWidth(224.0.dp).requiredHeight(48.0.dp)
    )
}

@Composable
fun GuardarGuardarPulsadoNo(modifier: Modifier = Modifier) {
    Guardar(modifier = modifier.requiredWidth(32.0.dp).requiredHeight(32.0.dp))
}

@Composable
fun TopLevelVariacionInferior(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 0,
            green = 0,
            blue = 0
        ),
        mainAxisAlignment = MainAxisAlignment.SpaceBetween,
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(
            start = 8.0.dp,
            top = 0.0.dp,
            end = 8.0.dp,
            bottom = 0.0.dp
        ),
        strokeWidth = 1.0,
        strokeColor = Color(
            alpha = 255,
            red = 0,
            green = 0,
            blue = 0
        ),
        content = content,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}
