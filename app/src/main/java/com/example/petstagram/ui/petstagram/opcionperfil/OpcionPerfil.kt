package com.example.petstagram.opcionperfil

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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText

// Design to select for OpcionPerfil
enum class LeSigue {
    No,
    Si
}

/**
 * opciones segun sigas o no al perfil
 *
 * This composable was generated from the UI Package 'opcion_perfil'.
 * Generated code; do not edit directly
 */
@Composable
fun OpcionPerfil(
    modifier: Modifier = Modifier,
    leSigue: LeSigue = LeSigue.No
) {
    when (leSigue) {
        LeSigue.No -> TopLevelLeSigueNo(modifier = modifier) {
            TextoSeguirLeSigueNo(
                modifier = Modifier.boxAlign(
                    alignment = Alignment.Center,
                    offset = DpOffset(
                        x = 0.0.dp,
                        y = 0.0.dp
                    )
                )
            )
        }
        LeSigue.Si -> TopLevelLeSigueSi(modifier = modifier) {
            TextoDejarSeguirLeSigueSi(
                modifier = Modifier.boxAlign(
                    alignment = Alignment.Center,
                    offset = DpOffset(
                        x = 0.0.dp,
                        y = 0.0.dp
                    )
                )
            )
        }
    }
}

@Preview(widthDp = 280, heightDp = 48)
@Composable
private fun OpcionPerfilLeSigueNoPreview() {
    MaterialTheme {
        RelayContainer {
            OpcionPerfil(
                leSigue = LeSigue.No,
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Preview(widthDp = 280, heightDp = 48)
@Composable
private fun OpcionPerfilLeSigueSiPreview() {
    MaterialTheme {
        RelayContainer {
            OpcionPerfil(
                leSigue = LeSigue.Si,
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Composable
fun TextoSeguirLeSigueNo(modifier: Modifier = Modifier) {
    RelayText(
        content = "Seguir",
        fontSize = 25.0.sp,
        fontFamily = inter,
        height = 1.210227279663086.em,
        fontWeight = FontWeight(800.0.toInt()),
        maxLines = -1,
        modifier = modifier.requiredWidth(264.0.dp).requiredHeight(32.0.dp).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun TopLevelLeSigueNo(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 225,
            green = 196,
            blue = 1
        ),
        isStructured = false,
        radius = 6.0,
        strokeWidth = 2.0,
        strokeColor = Color(
            alpha = 38,
            red = 0,
            green = 0,
            blue = 0
        ),
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun TextoDejarSeguirLeSigueSi(modifier: Modifier = Modifier) {
    RelayText(
        content = "Dejar de seguir",
        fontSize = 25.0.sp,
        fontFamily = inter,
        height = 1.210227279663086.em,
        fontWeight = FontWeight(800.0.toInt()),
        maxLines = -1,
        modifier = modifier.requiredWidth(264.0.dp).requiredHeight(32.0.dp).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun TopLevelLeSigueSi(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 225,
            green = 196,
            blue = 1
        ),
        isStructured = false,
        radius = 6.0,
        strokeWidth = 2.0,
        strokeColor = Color(
            alpha = 38,
            red = 0,
            green = 0,
            blue = 0
        ),
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}
