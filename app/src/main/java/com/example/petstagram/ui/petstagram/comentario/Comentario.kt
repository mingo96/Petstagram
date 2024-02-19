package com.example.petstagram.ui.petstagram.comentario

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.petstagram.R
import com.example.petstagram.fotoperfil.FotoPerfil
import com.example.petstagram.like.Like
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText
import com.google.relay.compose.RelayVector

/**
 * Comentario
 *
 * This composable was generated from the UI Package 'comentario'.
 * Generated code; do not edit directly
 */
@Composable
fun Comentario(modifier: Modifier = Modifier) {
    TopLevel(modifier = modifier) {
        LineaInferior(
            modifier = Modifier.boxAlign(
                alignment = Alignment.BottomStart,
                offset = DpOffset(
                    x = 0.0.dp,
                    y = -1.0.dp
                )
            ).rowWeight(1.0f)
        )
        ContenidoComentario(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 48.0.dp,
                    y = 0.0.dp
                )
            ).columnWeight(1.0f)
        )
        FotoPerfilInstance(
            modifier = Modifier.boxAlign(
                alignment = Alignment.CenterStart,
                offset = DpOffset(
                    x = 8.0.dp,
                    y = 0.0.dp
                )
            )
        )
        LikeInstance(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopEnd,
                offset = DpOffset(
                    x = -8.0.dp,
                    y = 8.0.dp
                )
            )
        )
    }
}

@Preview(widthDp = 360, heightDp = 48)
@Composable
private fun ComentarioPreview() {
    MaterialTheme {
        RelayContainer {
            Comentario(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
    }
}

@Composable
fun LineaInferior(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.comentario_linea_inferior),
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 16.0.dp,
                top = 0.0.dp,
                end = 16.0.dp,
                bottom = 0.0.dp
            )
        ).fillMaxWidth(1.0f).requiredHeight(0.0.dp)
    )
}

@Composable
fun ContenidoComentario(modifier: Modifier = Modifier) {
    RelayText(
        content = "\${comentario}",
        color = Color(
            alpha = 255,
            red = 224,
            green = 164,
            blue = 0
        ),
        height = 1.4285714721679688.em,
        letterSpacing = 0.10000000149011612.sp,
        textAlign = TextAlign.Left,
        fontWeight = FontWeight(500.0.toInt()),
        maxLines = -1,
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 0.0.dp,
                top = 8.0.dp,
                end = 0.0.dp,
                bottom = 8.0.dp
            )
        ).requiredWidth(256.0.dp).fillMaxHeight(1.0f).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun FotoPerfilInstance(modifier: Modifier = Modifier) {
    FotoPerfil(modifier = modifier.requiredWidth(32.0.dp).requiredHeight(32.0.dp))
}

@Composable
fun LikeInstance(modifier: Modifier = Modifier) {
    Like(modifier = modifier.requiredWidth(32.0.dp).requiredHeight(32.0.dp))
}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}