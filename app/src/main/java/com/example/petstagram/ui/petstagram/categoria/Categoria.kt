package com.example.petstagram.categoria

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.petstagram.R
import com.google.relay.compose.CrossAxisAlignment
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayImage
import com.google.relay.compose.RelayText

/**
 * categoria
 *
 * This composable was generated from the UI Package 'categoria'.
 * Generated code; do not edit directly
 */
@Composable
fun Categoria(modifier: Modifier = Modifier, navController: NavHostController) {
    TopLevel(modifier = modifier) {
        ContendorImagen(modifier = Modifier.rowWeight(1.0f)) {
            ImagenGenerica(
                modifier = Modifier.boxAlign(
                    alignment = Alignment.Center,
                    offset = DpOffset(
                        x = 0.0.dp,
                        y = 0.5.dp
                    )
                )
            )
        }
        Botones(modifier = Modifier.rowWeight(1.0f)) {
            BotonAccesoPublicaciones(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f).clickable { navController.navigate("publicaciones") }) {
                TextoCategoria()
            }
            BotonMas(modifier = Modifier.columnWeight(1.0f).clickable { navController.navigate("nuevaPublicacion") }) {
                CuadroSumar(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
            }
        }
    }
}


@Composable
fun ImagenGenerica(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.categoria_imagen_generica),
        contentScale = ContentScale.Crop,
        modifier = modifier.requiredWidth(120.0.dp).requiredHeight(125.0.dp)
    )
}

@Composable
fun ContendorImagen(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 217,
            green = 217,
            blue = 217
        ),
        isStructured = false,
        radius = 5.0,
        strokeWidth = 2.0,
        strokeColor = Color(
            alpha = 255,
            red = 0,
            green = 0,
            blue = 0
        ),
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).requiredHeight(176.0.dp)
    )
}

@Composable
fun TextoCategoria(modifier: Modifier = Modifier) {
    RelayText(
        content = "Publicaciones de \${categoría}",
        fontSize = 15.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 35,
            green = 35,
            blue = 35
        ),
        height = 1.2102272033691406.em,
        fontWeight = FontWeight(800.0.toInt()),
        overflow = TextOverflow.Ellipsis,
        maxLines = -1,
        modifier = modifier.requiredWidth(232.0.dp).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun BotonAccesoPublicaciones(
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
        padding = PaddingValues(all = 8.0.dp),
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
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
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
        modifier = modifier.requiredWidth(64.0.dp).fillMaxHeight(1.0f)
    )
}

@Composable
fun Botones(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        mainAxisAlignment = MainAxisAlignment.Start,
        crossAxisAlignment = CrossAxisAlignment.Start,
        arrangement = RelayContainerArrangement.Row,
        itemSpacing = 16.0,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).requiredHeight(40.0.dp)
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
        crossAxisAlignment = CrossAxisAlignment.Start,
        padding = PaddingValues(all = 16.0.dp),
        itemSpacing = 8.0,
        strokeWidth = 8.0,
        strokeColor = Color(
            alpha = 255,
            red = 35,
            green = 35,
            blue = 35
        ),
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).wrapContentHeight()
    )
}
