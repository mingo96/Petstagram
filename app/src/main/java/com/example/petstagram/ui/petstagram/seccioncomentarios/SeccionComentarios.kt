package com.example.petstagram.ui.petstagram.seccioncomentarios

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.petstagram.ui.petstagram.comentario.Comentario
import com.example.petstagram.seccioncomentarios.inter
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText

/**
 * Seccion de comentarios
 *
 * This composable was generated from the UI Package 'seccion_comentarios'.
 * Generated code; do not edit directly
 */
@Composable
fun SeccionComentarios(modifier: Modifier = Modifier) {
    TopLevel(modifier = modifier) {
        Titulo(modifier = Modifier.rowWeight(1.0f)) {
            TituloComentarios()
            BotonMas(modifier = Modifier.columnWeight(1.0f)) {
                CuadroSumar(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
            }
        }
        Comentarios(modifier = Modifier.rowWeight(1.0f)) {
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
            ComentarioInstance(modifier = Modifier.rowWeight(1.0f))
        }
    }
}

@Preview(widthDp = 408, heightDp = 440)
@Composable
private fun SeccionComentariosPreview() {
    MaterialTheme {
        RelayContainer {
            SeccionComentarios(modifier = Modifier.columnWeight(1.0f))
        }
    }
}

@Composable
fun TituloComentarios(modifier: Modifier = Modifier) {
    RelayText(
        content = "Comentarios de la publicación de \${nombreMascota}",
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.4285714721679688.em,
        letterSpacing = 0.10000000149011612.sp,
        fontWeight = FontWeight(500.0.toInt()),
        maxLines = -1,
        modifier = modifier.requiredWidth(280.0.dp).requiredHeight(40.0.dp).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun CuadroSumar(modifier: Modifier = Modifier) {
    RelayText(
        content = "+",
        fontSize = 40.0.sp,
        fontFamily = inter,
        height = 1.2102272033691406.em,
        fontWeight = FontWeight(500.0.toInt()),
        maxLines = -1,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f).wrapContentHeight(
            align = Alignment.Bottom,
            unbounded = true
        )
    )
}

@Composable
fun BotonMas(
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
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(
            start = 8.0.dp,
            top = 0.0.dp,
            end = 8.0.dp,
            bottom = 0.0.dp
        ),
        itemSpacing = 8.0,
        radius = 15.0,
        strokeWidth = 1.0,
        strokeColor = Color(
            alpha = 38,
            red = 0,
            green = 0,
            blue = 0
        ),
        content = content,
        modifier = modifier.requiredWidth(64.0.dp).requiredHeight(40.dp)
    )
}

@Composable
fun Titulo(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        mainAxisAlignment = MainAxisAlignment.SpaceEvenly,
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(
            end = 24.0.dp
        ),
        content = content,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}

@Composable
fun ComentarioInstance(modifier: Modifier = Modifier) {
    Comentario(modifier = modifier.fillMaxWidth(1.0f).requiredHeight(48.0.dp))
}

@Composable
fun Comentarios(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        mainAxisAlignment = MainAxisAlignment.Start,
        scrollable = true,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight()
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
            red = 0,
            green = 0,
            blue = 0
        ),
        mainAxisAlignment = MainAxisAlignment.Start,
        padding = PaddingValues(
            start = 8.0.dp,
            top = 16.0.dp,
            end = 8.0.dp,
            bottom = 0.0.dp
        ),
        itemSpacing = 16.0,
        strokeWidth = 1.0,
        strokeColor = Color(
            alpha = 255,
            red = 225,
            green = 196,
            blue = 1
        ),
        radius = 6.0,
        content = content,
        modifier = modifier.fillMaxHeight(1.0f)
    )
}
