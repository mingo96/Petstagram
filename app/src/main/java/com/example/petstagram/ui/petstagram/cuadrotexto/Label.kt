package com.example.petstagram.cuadrotexto

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
enum class Variation {
    UserName,
    Petstagram,
    YourProfile,
    User,
    YourPosts,
    Password,
    CategoryPosts,
    Register,
    Login,
    CreatePost,
    SelectResource,
    PostTitle,
    PostCategory,
    Publish
}

/**
 * cuadros de texto
 *
 *
 * This composable was generated from the UI Package 'cuadro_texto'.
 * Generated code; do not edit directly
 */
@Composable
fun Label(
    modifier: Modifier = Modifier,
    variation: Variation = Variation.UserName,
    added: String = ""
) {
    when (variation) {
        Variation.UserName -> TopLevelVariacionNombreUsuario(modifier = modifier) {
            TextoNombreUsuarioVariacionNombreUsuario(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
        Variation.Petstagram -> TopLevelVariacionBienvenida(modifier = modifier) {
            TextoBienvenidaVariacionBienvenida(modifier = Modifier.rowWeight(1.0f))
        }
        Variation.YourProfile -> TopLevelVariacionTuPerfil(modifier = modifier) {
            TextoTuPerfilVariacionTuPerfil(modifier = Modifier.rowWeight(1.0f))
        }
        Variation.User -> TopLevelVariacionUsuario(modifier = modifier) {
            TextoEmailVariacionUsuario(modifier = Modifier.rowWeight(1.0f))
        }
        Variation.YourPosts -> TopLevelVariacionTusPublicaciones(modifier = modifier) {
            TextoTusPublicacionesVariacionTusPublicaciones(modifier = Modifier.rowWeight(1.0f))
        }
        Variation.Password -> TopLevelVariacionClave(modifier = modifier) {
            TextoClaveVariacionClave(modifier = Modifier.rowWeight(1.0f))
        }
        Variation.CategoryPosts -> TopLevelVariacionPublicacionesCategoria(modifier = modifier) {
            TextoPubCategoriaVariacionPublicacionesCategoria(modifier = Modifier.rowWeight(1.0f), added)
        }
        Variation.Register -> TopLevelVariacionRegistro(modifier = modifier) {
            TextoRegistroVariacionRegistro(modifier = Modifier.rowWeight(1.0f))
        }
        Variation.Login -> TopLevelVariacionInicioSesion(modifier = modifier) {
            TextoInicioSesionVariacionInicioSesion(modifier = Modifier.rowWeight(1.0f))
        }
        Variation.CreatePost -> TopLevelVariacionCrearPublicacion(modifier = modifier) {
            TextoCrearPublicacionVariacionCrearPublicacion(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
        Variation.SelectResource -> TopLevelVariacionSeleccionarRecurso(modifier = modifier) {
            TextoSeleccionarRecursoVariacionSeleccionarRecurso(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
        Variation.PostTitle -> TopLevelVariacionTituloPublicacion(modifier = modifier) {
            PostTitle(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f), added  )
        }
        Variation.PostCategory -> TopLevelVariacionCategoriaPublicacion(modifier = modifier) {
            TextoCategoriaPublicacionVariacionCategoriaPublicacion(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
        Variation.Publish -> TopLevelVariacionPublicar(modifier = modifier) {
            TextoPublicarVariacionPublicar(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
    }
}

//IM NOT REFACTORING ALLAT
@Preview(widthDp = 281, heightDp = 40)
@Composable
private fun CuadroTextoVariacionNombreUsuarioPreview() {
    MaterialTheme {
        RelayContainer {
            Label(
                modifier = Modifier.rowWeight(1.0f),
                variation = Variation.UserName
            )
        }
    }
}

@Preview(widthDp = 234, heightDp = 64)
@Composable
private fun CuadroTextoVariacionBienvenidaPreview() {
    MaterialTheme {
        RelayContainer {
            Label(
                modifier = Modifier.rowWeight(1.0f),
                variation = Variation.Petstagram
            )
        }
    }
}

@Preview(widthDp = 94, heightDp = 40)
@Composable
private fun CuadroTextoVariacionTuPerfilPreview() {
    MaterialTheme {
        RelayContainer {
            Label(
                modifier = Modifier.rowWeight(1.0f),
                variation = Variation.YourProfile
            )
        }
    }
}

@Preview(widthDp = 296, heightDp = 58)
@Composable
private fun CuadroTextoVariacionUsuarioPreview() {
    MaterialTheme {
        RelayContainer {
            Label(
                modifier = Modifier.rowWeight(1.0f),
                variation = Variation.User
            )
        }
    }
}

@Preview(widthDp = 186, heightDp = 40)
@Composable
private fun CuadroTextoVariacionTusPublicacionesPreview() {
    MaterialTheme {
        RelayContainer {
            Label(
                modifier = Modifier.rowWeight(1.0f),
                variation = Variation.YourPosts
            )
        }
    }
}

@Preview(widthDp = 296, heightDp = 58)
@Composable
private fun CuadroTextoVariacionClavePreview() {
    MaterialTheme {
        RelayContainer {
            Label(
                modifier = Modifier.rowWeight(1.0f),
                variation = Variation.Password
            )
        }
    }
}

@Preview(widthDp = 296, heightDp = 44)
@Composable
private fun CuadroTextoVariacionPublicacionesCategoriaPreview() {
    MaterialTheme {
        RelayContainer {
            Label(
                modifier = Modifier.rowWeight(1.0f),
                variation = Variation.CategoryPosts
            )
        }
    }
}

@Preview(widthDp = 296, heightDp = 58)
@Composable
private fun CuadroTextoVariacionRegistroPreview() {
    MaterialTheme {
        RelayContainer {
            Label(
                modifier = Modifier.rowWeight(1.0f),
                variation = Variation.Register
            )
        }
    }
}

@Preview(widthDp = 296, heightDp = 58)
@Composable
private fun CuadroTextoVariacionInicioSesionPreview() {
    MaterialTheme {
        RelayContainer {
            Label(
                modifier = Modifier.rowWeight(1.0f),
                variation = Variation.Login
            )
        }
    }
}

@Preview(widthDp = 280, heightDp = 40)
@Composable
private fun CuadroTextoVariacionCrearPublicacionPreview() {
    MaterialTheme {
        RelayContainer {
            Label(
                modifier = Modifier.rowWeight(1.0f),
                variation = Variation.CreatePost
            )
        }
    }
}

@Preview(widthDp = 296, heightDp = 40)
@Composable
private fun CuadroTextoVariacionSeleccionarRecursoPreview() {
    MaterialTheme {
        RelayContainer {
            Label(
                modifier = Modifier.rowWeight(1.0f),
                variation = Variation.SelectResource
            )
        }
    }
}

@Preview(widthDp = 296, heightDp = 40)
@Composable
private fun CuadroTextoVariacionTituloPublicacionPreview() {
    MaterialTheme {
        RelayContainer {
            Label(
                modifier = Modifier.rowWeight(1.0f),
                variation = Variation.PostTitle
            )
        }
    }
}

@Preview(widthDp = 296, heightDp = 40)
@Composable
private fun CuadroTextoVariacionCategoriaPublicacionPreview() {
    MaterialTheme {
        RelayContainer {
            Label(
                modifier = Modifier.rowWeight(1.0f),
                variation = Variation.PostCategory
            )
        }
    }
}

@Preview(widthDp = 296, heightDp = 40)
@Composable
private fun CuadroTextoVariacionPublicarPreview() {
    MaterialTheme {
        RelayContainer {
            Label(
                modifier = Modifier.rowWeight(1.0f),
                variation = Variation.Publish
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
fun TextoPubCategoriaVariacionPublicacionesCategoria(modifier: Modifier = Modifier, added: String) {
    RelayText(
        content = "Publicaciones de la categoria ${added}",
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

@Composable
fun TextoCrearPublicacionVariacionCrearPublicacion(modifier: Modifier = Modifier) {
    RelayText(
        content = "Crear Publicacion",
        fontSize = 30.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 225,
            green = 196,
            blue = 1
        ),
        height = 1.2102272510528564.em,
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
fun TopLevelVariacionCrearPublicacion(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(all = 8.0.dp),
        itemSpacing = 10.0,
        radius = 5.0,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun TextoSeleccionarRecursoVariacionSeleccionarRecurso(modifier: Modifier = Modifier) {
    RelayText(
        content = "Seleccionar recurso",
        fontSize = 16.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.2102272510528564.em,
        fontWeight = FontWeight(700.0.toInt()),
        overflow = TextOverflow.Ellipsis,
        maxLines = -1,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        ).padding(8.dp)
    )
}

@Composable
fun TopLevelVariacionSeleccionarRecurso(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 0,
            red = 224,
            green = 164,
            blue = 0
        ),
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(all = 8.0.dp),
        itemSpacing = 10.0,
        radius = 50.0,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun PostTitle(modifier: Modifier = Modifier, title : String) {
    RelayText(
        content = title,
        fontSize = 12.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.2102272510528564.em,
        fontWeight = FontWeight(700.0.toInt()),
        overflow = TextOverflow.Ellipsis,
        maxLines = -1,
        modifier = modifier.wrapContentWidth(
            unbounded = false,
            align = Alignment.Start
        ).padding(8.dp)
    )
}

@Composable
fun TopLevelVariacionTituloPublicacion(
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
        padding = PaddingValues(all = 8.0.dp),
        itemSpacing = 10.0,
        radius = 5.0,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun TextoCategoriaPublicacionVariacionCategoriaPublicacion(modifier: Modifier = Modifier) {
    RelayText(
        content = "Categoria de la publicacion",
        fontSize = 24.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.2102272510528564.em,
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
fun TopLevelVariacionCategoriaPublicacion(
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
        padding = PaddingValues(all = 8.0.dp),
        itemSpacing = 10.0,
        radius = 5.0,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun TextoPublicarVariacionPublicar(modifier: Modifier = Modifier) {
    RelayText(
        content = "Publicar",
        fontSize = 30.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
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
fun TopLevelVariacionPublicar(
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
        padding = PaddingValues(all = 8.0.dp),
        itemSpacing = 10.0,
        radius = 5.0,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}
