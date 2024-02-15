package com.example.petstagram.cuadrotexto

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText

// Design to select for CuadroTexto
enum class Variacion {
    NombreUsuario,
    Bienvenida,
    TuPerfil,
    Usuario,
    TusPublicaciones,
    Clave,
    PublicacionesCategoria,
    Registro,
    InicioSesion
}

/**
 * cuadros de texto
 *
 *
 * This composable was generated from the UI Package 'cuadro_texto'.
 * Generated code; do not edit directly
 */
@Composable
fun CuadroTexto(
    modifier: Modifier = Modifier,
    variacion: Variacion = Variacion.NombreUsuario
) {
    when (variacion) {
        Variacion.NombreUsuario -> TopLevelVariacionNombreUsuario(modifier = modifier) {
            TextoNombreUsuarioVariacionNombreUsuario(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
        Variacion.Bienvenida -> TopLevelVariacionBienvenida(modifier = modifier) {
            TextoBienvenidaVariacionBienvenida(modifier = Modifier.rowWeight(1.0f))
        }
        Variacion.TuPerfil -> TopLevelVariacionTuPerfil(modifier = modifier) {
            TextoTuPerfilVariacionTuPerfil(modifier = Modifier.rowWeight(1.0f))
        }
        Variacion.Usuario -> TopLevelVariacionUsuario(modifier = modifier) {
            TextoEmailVariacionUsuario(modifier = Modifier.rowWeight(1.0f))
        }
        Variacion.TusPublicaciones -> TopLevelVariacionTusPublicaciones(modifier = modifier) {
            TextoTusPublicacionesVariacionTusPublicaciones(modifier = Modifier.rowWeight(1.0f))
        }
        Variacion.Clave -> TopLevelVariacionClave(modifier = modifier) {
            TextoClaveVariacionClave(modifier = Modifier.rowWeight(1.0f))
        }
        Variacion.PublicacionesCategoria -> TopLevelVariacionPublicacionesCategoria(modifier = modifier) {
            TextoPubCategoriaVariacionPublicacionesCategoria(modifier = Modifier.rowWeight(1.0f))
        }
        Variacion.Registro -> TopLevelVariacionRegistro(modifier = modifier) {
            TextoRegistroVariacionRegistro(modifier = Modifier.rowWeight(1.0f))
        }
        Variacion.InicioSesion -> TopLevelVariacionInicioSesion(modifier = modifier) {
            TextoInicioSesionVariacionInicioSesion(modifier = Modifier.rowWeight(1.0f))
        }
    }
}

@Preview(widthDp = 281, heightDp = 40)
@Composable
private fun CuadroTextoVariacionNombreUsuarioPreview() {
    MaterialTheme {
        RelayContainer {
            CuadroTexto(
                variacion = Variacion.NombreUsuario,
                modifier = Modifier.rowWeight(1.0f)
            )
        }
    }
}

@Preview(widthDp = 234, heightDp = 64)
@Composable
private fun CuadroTextoVariacionBienvenidaPreview() {
    MaterialTheme {
        RelayContainer {
            CuadroTexto(
                variacion = Variacion.Bienvenida,
                modifier = Modifier.rowWeight(1.0f)
            )
        }
    }
}

@Preview(widthDp = 94, heightDp = 40)
@Composable
private fun CuadroTextoVariacionTuPerfilPreview() {
    MaterialTheme {
        RelayContainer {
            CuadroTexto(
                variacion = Variacion.TuPerfil,
                modifier = Modifier.rowWeight(1.0f)
            )
        }
    }
}

@Preview(widthDp = 351, heightDp = 58)
@Composable
private fun CuadroTextoVariacionUsuarioPreview() {
    MaterialTheme {
        RelayContainer {
            CuadroTexto(
                variacion = Variacion.Usuario,
                modifier = Modifier.rowWeight(1.0f)
            )
        }
    }
}

@Preview(widthDp = 186, heightDp = 40)
@Composable
private fun CuadroTextoVariacionTusPublicacionesPreview() {
    MaterialTheme {
        RelayContainer {
            CuadroTexto(
                variacion = Variacion.TusPublicaciones,
                modifier = Modifier.rowWeight(1.0f)
            )
        }
    }
}

@Preview(widthDp = 351, heightDp = 58)
@Composable
private fun CuadroTextoVariacionClavePreview() {
    MaterialTheme {
        RelayContainer {
            CuadroTexto(
                variacion = Variacion.Clave,
                modifier = Modifier.rowWeight(1.0f)
            )
        }
    }
}

@Preview(widthDp = 296, heightDp = 44)
@Composable
private fun CuadroTextoVariacionPublicacionesCategoriaPreview() {
    MaterialTheme {
        RelayContainer {
            CuadroTexto(
                variacion = Variacion.PublicacionesCategoria,
                modifier = Modifier.rowWeight(1.0f)
            )
        }
    }
}

@Preview(widthDp = 351, heightDp = 58)
@Composable
private fun CuadroTextoVariacionRegistroPreview() {
    MaterialTheme {
        RelayContainer {
            CuadroTexto(
                variacion = Variacion.Registro,
                modifier = Modifier.rowWeight(1.0f)
            )
        }
    }
}

@Preview(widthDp = 351, heightDp = 58)
@Composable
private fun CuadroTextoVariacionInicioSesionPreview() {
    MaterialTheme {
        RelayContainer {
            CuadroTexto(
                variacion = Variacion.InicioSesion,
                modifier = Modifier.rowWeight(1.0f)
            )
        }
    }
}

@Composable
fun TextoNombreUsuarioVariacionNombreUsuario(modifier: Modifier = Modifier) {
    RelayText(
        content = "Perfil de \${nombreusuario}",
        fontSize = 20.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 225,
            green = 196,
            blue = 1
        ),
        height = 1.2102272033691406.em,
        fontWeight = FontWeight(700.0.toInt()),
        overflow = TextOverflow.Ellipsis,
        maxLines = -1,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun TopLevelVariacionNombreUsuario(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        mainAxisAlignment = MainAxisAlignment.Start,
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(all = 8.0.dp),
        itemSpacing = 10.0,
        radius = 5.0,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}

@Composable
fun TextoBienvenidaVariacionBienvenida(modifier: Modifier = Modifier) {
    RelayText(
        content = "Petstagram",
        fontSize = 40.0.sp,
        fontFamily = inter,
        height = 1.2102272033691406.em,
        maxLines = -1,
        modifier = modifier.fillMaxWidth(1.0f).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun TopLevelVariacionBienvenida(
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
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(all = 8.0.dp),
        itemSpacing = 10.0,
        radius = 6.0,
        strokeWidth = 2.0,
        strokeColor = Color(
            alpha = 38,
            red = 0,
            green = 0,
            blue = 0
        ),
        content = content,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}

@Composable
fun TextoTuPerfilVariacionTuPerfil(modifier: Modifier = Modifier) {
    RelayText(
        content = "Tu perfil",
        fontSize = 20.0.sp,
        fontFamily = inter,
        height = 1.2102272033691406.em,
        maxLines = -1,
        modifier = modifier.fillMaxWidth(1.0f).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun TopLevelVariacionTuPerfil(
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
        mainAxisAlignment = MainAxisAlignment.Start,
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(all = 8.0.dp),
        itemSpacing = 10.0,
        radius = 6.0,
        strokeWidth = 2.0,
        strokeColor = Color(
            alpha = 38,
            red = 0,
            green = 0,
            blue = 0
        ),
        content = content,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}

@Composable
fun TextoEmailVariacionUsuario(modifier: Modifier = Modifier) {
    RelayText(
        content = "user@example.com",
        fontSize = 35.0.sp,
        fontFamily = inter,
        height = 1.2102272033691406.em,
        maxLines = -1,
        modifier = modifier.fillMaxWidth(1.0f).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun TopLevelVariacionUsuario(
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
        padding = PaddingValues(all = 8.0.dp),
        itemSpacing = 8.0,
        radius = 6.0,
        strokeWidth = 2.0,
        strokeColor = Color(
            alpha = 38,
            red = 0,
            green = 0,
            blue = 0
        ),
        content = content,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}

@Composable
fun TextoTusPublicacionesVariacionTusPublicaciones(modifier: Modifier = Modifier) {
    RelayText(
        content = "Tus Publicaciones",
        fontSize = 20.0.sp,
        fontFamily = inter,
        height = 1.2102272033691406.em,
        maxLines = -1,
        modifier = modifier.fillMaxWidth(1.0f).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun TopLevelVariacionTusPublicaciones(
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
        mainAxisAlignment = MainAxisAlignment.Start,
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(all = 8.0.dp),
        itemSpacing = 10.0,
        radius = 6.0,
        strokeWidth = 2.0,
        strokeColor = Color(
            alpha = 38,
            red = 0,
            green = 0,
            blue = 0
        ),
        content = content,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}

@Composable
fun TextoClaveVariacionClave(modifier: Modifier = Modifier) {
    RelayText(
        content = "********",
        fontSize = 35.0.sp,
        fontFamily = inter,
        height = 1.2102272033691406.em,
        maxLines = -1,
        modifier = modifier.fillMaxWidth(1.0f).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun TopLevelVariacionClave(
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
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(all = 8.0.dp),
        itemSpacing = 10.0,
        radius = 6.0,
        strokeWidth = 2.0,
        strokeColor = Color(
            alpha = 38,
            red = 0,
            green = 0,
            blue = 0
        ),
        content = content,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}

@Composable
fun TextoPubCategoriaVariacionPublicacionesCategoria(modifier: Modifier = Modifier) {
    RelayText(
        content = "Publicaciones de la categoria \${categoria}",
        fontSize = 18.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 225,
            green = 196,
            blue = 1
        ),
        height = 1.2102272245619032.em,
        fontWeight = FontWeight(800.0.toInt()),
        maxLines = -1,
        modifier = modifier.fillMaxWidth(1.0f).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun TopLevelVariacionPublicacionesCategoria(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        mainAxisAlignment = MainAxisAlignment.SpaceBetween,
        arrangement = RelayContainerArrangement.Row,
        radius = 5.0,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}

@Composable
fun TextoRegistroVariacionRegistro(modifier: Modifier = Modifier) {
    RelayText(
        content = "Registrarse",
        fontSize = 35.0.sp,
        fontFamily = inter,
        height = 1.2102272033691406.em,
        maxLines = -1,
        modifier = modifier.fillMaxWidth(1.0f).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun TopLevelVariacionRegistro(
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
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(all = 8.0.dp),
        itemSpacing = 10.0,
        radius = 6.0,
        strokeWidth = 2.0,
        strokeColor = Color(
            alpha = 38,
            red = 0,
            green = 0,
            blue = 0
        ),
        content = content,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}

@Composable
fun TextoInicioSesionVariacionInicioSesion(modifier: Modifier = Modifier) {
    RelayText(
        content = "Iniciar Sesión",
        fontSize = 35.0.sp,
        fontFamily = inter,
        height = 1.2102272033691406.em,
        maxLines = -1,
        modifier = modifier.fillMaxWidth(1.0f).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun TopLevelVariacionInicioSesion(
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
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(all = 8.0.dp),
        itemSpacing = 10.0,
        radius = 6.0,
        strokeWidth = 2.0,
        strokeColor = Color(
            alpha = 38,
            red = 0,
            green = 0,
            blue = 0
        ),
        content = content,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}
