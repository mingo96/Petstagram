package com.example.petstagram.ui.petstagram.barradeselecciondetipodepublicacion

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.petstagram.atras.Atras
import com.example.petstagram.R
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText
import com.google.relay.compose.RelayVector

// Design to select for BarraDeSeleccionDeTipoDePublicacion
enum class Seleccionado {
    Publicaciones,
    Adopcion
}

/**
 * Seleccion de tipo de pub
 *
 * This composable was generated from the UI Package 'barra_de_seleccion_de_tipo_de_publicacion'.
 * Generated code; do not edit directly
 */
@Composable
fun BarraDeSeleccionDeTipoDePublicacion(
    modifier: Modifier = Modifier,
    seleccionado: Seleccionado = Seleccionado.Publicaciones
) {
    when (seleccionado) {
        Seleccionado.Publicaciones -> TopLevelSeleccionadoPublicaciones(modifier = modifier) {
            PublicacionSelccionadoSeleccionadoPublicaciones {
                IconoSeleccionadoSeleccionadoPublicaciones(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)) {
                    IconoSeleccionadoPublicaciones(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
                }
                PublicacionesSeleccionadoPublicaciones(
                    modifier = Modifier.boxAlign(
                        alignment = Alignment.Center,
                        offset = DpOffset(
                            x = 12.5.dp,
                            y = 0.0.dp
                        )
                    )
                )
            }
            AdopcionNoSeleccionadoSeleccionadoPublicaciones {
                AdopcionSeleccionadoPublicaciones(
                    modifier = Modifier.boxAlign(
                        alignment = Alignment.Center,
                        offset = DpOffset(
                            x = 0.0.dp,
                            y = 0.0.dp
                        )
                    )
                )
            }
            AtrasAtras()
        }
        Seleccionado.Adopcion -> TopLevelSeleccionadoAdopcion(modifier = modifier) {
            PublicacionNoSeleccionadoSeleccionadoAdopcion {
                PublicacionesSeleccionadoAdopcion(
                    modifier = Modifier.boxAlign(
                        alignment = Alignment.Center,
                        offset = DpOffset(
                            x = 0.0.dp,
                            y = 0.0.dp
                        )
                    )
                )
            }
            AdopcionSeleccionadoSeleccionadoAdopcion {
                IconoSeleccionadoSeleccionadoAdopcion(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)) {
                    IconoSeleccionadoAdopcion(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
                }
                AdopcionSeleccionadoAdopcion(
                    modifier = Modifier.boxAlign(
                        alignment = Alignment.Center,
                        offset = DpOffset(
                            x = 12.5.dp,
                            y = 0.0.dp
                        )
                    )
                )
            }
            AtrasAtras()
        }
    }
}

@Preview(widthDp = 329, heightDp = 48)
@Composable
private fun BarraDeSeleccionDeTipoDePublicacionSeleccionadoPublicacionesPreview() {
    MaterialTheme {
        BarraDeSeleccionDeTipoDePublicacion(seleccionado = Seleccionado.Publicaciones)
    }
}

@Preview(widthDp = 329, heightDp = 48)
@Composable
private fun BarraDeSeleccionDeTipoDePublicacionSeleccionadoAdopcionPreview() {
    MaterialTheme {
        BarraDeSeleccionDeTipoDePublicacion(seleccionado = Seleccionado.Adopcion)
    }
}

@Composable
fun IconoSeleccionadoPublicaciones(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.barra_de_seleccion_de_tipo_de_publicacion_icono),
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 2.2734375.dp,
                top = 3.7266845703125.dp,
                end = 1.9998960494995117.dp,
                bottom = 3.333314895629883.dp
            )
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun IconoSeleccionadoSeleccionadoPublicaciones(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        clipToParent = false,
        content = content,
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 9.0.dp,
                top = 8.0.dp,
                end = 112.0.dp,
                bottom = 8.0.dp
            )
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun PublicacionesSeleccionadoPublicaciones(modifier: Modifier = Modifier) {
    RelayText(
        content = "Publicaciones",
        height = 1.4285714721679688.em,
        letterSpacing = 0.10000000149011612.sp,
        fontWeight = FontWeight(500.0.toInt()),
        maxLines = -1,
        modifier = modifier.requiredWidth(96.0.dp).requiredHeight(32.0.dp).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun PublicacionSelccionadoSeleccionadoPublicaciones(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        isStructured = false,
        content = content,
        modifier = modifier.requiredWidth(137.0.dp).requiredHeight(32.0.dp).clip(RoundedCornerShape(40.dp,0.dp,0.dp,40.dp))
    )
}

@Composable
fun AdopcionSeleccionadoPublicaciones(modifier: Modifier = Modifier) {
    RelayText(
        content = "Adopción",
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
        modifier = modifier.requiredWidth(104.0.dp).requiredHeight(32.0.dp).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun AdopcionNoSeleccionadoSeleccionadoPublicaciones(
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
        strokeWidth = 1.0,
        strokeColor = Color(
            alpha = 127,
            red = 255,
            green = 255,
            blue = 255
        ),
        content = content,
        modifier = modifier.requiredWidth(104.0.dp).requiredHeight(32.0.dp)
    )
}

@Composable
fun AtrasAtras(modifier: Modifier = Modifier) {
    Atras(modifier = modifier.requiredWidth(56.0.dp).requiredHeight(48.0.dp))
}

@Composable
fun TopLevelSeleccionadoPublicaciones(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 0,
            red = 255,
            green = 255,
            blue = 255
        ),
        mainAxisAlignment = MainAxisAlignment.End,
        arrangement = RelayContainerArrangement.Row,
        itemSpacing = 16.0,
        content = content,
        modifier = modifier
    )
}

@Composable
fun PublicacionesSeleccionadoAdopcion(modifier: Modifier = Modifier) {
    RelayText(
        content = "Publicaciones",
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
        modifier = modifier.requiredWidth(104.0.dp).requiredHeight(32.0.dp).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun PublicacionNoSeleccionadoSeleccionadoAdopcion(
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
        strokeWidth = 1.0,
        strokeColor = Color(
            alpha = 127,
            red = 255,
            green = 255,
            blue = 255
        ),
        content = content,
        modifier = modifier.requiredWidth(104.0.dp).requiredHeight(32.0.dp)
    )
}

@Composable
fun IconoSeleccionadoAdopcion(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.barra_de_seleccion_de_tipo_de_publicacion_icono1),
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 2.2733154296875.dp,
                top = 3.7266845703125.dp,
                end = 2.0000181198120117.dp,
                bottom = 3.333314895629883.dp
            )
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun IconoSeleccionadoSeleccionadoAdopcion(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        clipToParent = false,
        content = content,
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 9.0.dp,
                top = 8.0.dp,
                end = 112.0.dp,
                bottom = 8.0.dp
            )
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun AdopcionSeleccionadoAdopcion(modifier: Modifier = Modifier) {
    RelayText(
        content = "Adopción",
        height = 1.4285714721679688.em,
        letterSpacing = 0.10000000149011612.sp,
        fontWeight = FontWeight(500.0.toInt()),
        maxLines = -1,
        modifier = modifier.requiredWidth(96.0.dp).requiredHeight(32.0.dp).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun AdopcionSeleccionadoSeleccionadoAdopcion(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        isStructured = false,
        content = content,
        modifier = modifier.requiredWidth(137.0.dp).requiredHeight(32.0.dp).clip(RoundedCornerShape(0.dp,40.dp,40.dp,0.dp))
    )
}

@Composable
fun TopLevelSeleccionadoAdopcion(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        mainAxisAlignment = MainAxisAlignment.End,
        arrangement = RelayContainerArrangement.Row,
        itemSpacing = 16.0,
        content = content,
        modifier = modifier
    )
}
