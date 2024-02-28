package com.example.petstagram.perfilpropio

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.petstagram.R
import com.example.petstagram.ViewModels.ProfilesViewModel
import com.example.petstagram.cuadrotexto.Label
import com.example.petstagram.cuadrotexto.Variacion
import com.example.petstagram.cuadrotexto.inter
import com.example.petstagram.menuprincipal.BarraSuperiorInstance
import com.example.petstagram.perfil.FotoPerfilInstance
import com.example.petstagram.publicaciones.Publicaciones
import com.example.petstagram.visualizarcategoria.TopLevel
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayImage
import com.google.relay.compose.RelayVector

/**
 * perfil propio
 *
 * This composable was generated from the UI Package 'perfil_propio'.
 * Generated code; do not edit directly
 */
@Composable
fun PerfilPropio(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ProfilesViewModel
) {
    LaunchedEffect(key1 = viewModel){
        viewModel.fetchPosts()
        viewModel.keepUpWithUserInfo()
    }

    val sourceSelecter = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){ uri ->
        if(uri != null)viewModel.setResource(uri)
    }

    val thisContext =(LocalContext.current)

    val profilePicObserver by viewModel.resource.observeAsState()

    val isLoading by viewModel.isLoading.observeAsState()

    val editing by viewModel.isEditing.observeAsState()

    val accessText : ()->String = { viewModel.getUserNameText() }

    val changeText :(String)->Unit = {viewModel.editUserName(it)}

    BoxWithConstraints {
        val height = maxHeight
        TopLevel(modifier = modifier) {
            BarraSuperiorInstance(modifier = Modifier
                .rowWeight(1.0f)
                .height(height.times(0.23f)),navController = navController)
            ContenedorNombreUsuario(Modifier.height(height.times(0.06f))) {
                YourUserName(editing = editing!!, accesoTexto = accessText, cambiarTexto = changeText)
                BotonEditarNombreUsuario(Modifier.clickable {
                    viewModel.editUserNameClicked(thisContext)
                }) {
                    CirculoEditarNombreUsuario()
                    BotonEditarNombreUsuarioSynth {
                        ImagenCambiarNombreUsuario()
                    }
                }
            }
            ContenedorImagen {
                FotoPerfilInstance(
                    Modifier
                        .height(height.times(0.30f))
                        .width(height.times(0.30f)),
                    url = profilePicObserver.orEmpty())
                BotonEditar(Modifier.clickable { sourceSelecter.launch("*/*") }) {
                    CirculoEditar()
                    BotonEditarSynth {
                        ImagenEditar()
                    }
                }
            }
            YourPostsLabel(Modifier.height(height.times(0.06f)))
            if (isLoading!!)
                CircularProgressIndicator(
                    modifier
                        .rowWeight(1.0f)
                        .height(height.times(0.825f))
                        .fillMaxWidth(0.8f))
            else
                PublicacionesInstance(
                    modifier = Modifier
                        .rowWeight(1.0f)
                        .height(height.times(0.48f)),
                    viewModel = viewModel
                )
            //PublicacionesCuenta(modifier = Modifier.rowWeight(1.0f).height(AlturaTotal.times(0.48f)))
        }
    }

}


@Composable
fun ContenedorNombreUsuario(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        arrangement = RelayContainerArrangement.Row,
        itemSpacing = 24.0,
        content = content,
        modifier = modifier
    )
}

@Composable
fun BotonEditarNombreUsuario(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        content = content,
        modifier = modifier
            .requiredWidth(32.0.dp)
            .requiredHeight(32.0.dp)
    )
}

@Composable
fun BotonEditarNombreUsuarioSynth(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        arrangement = RelayContainerArrangement.Row,
        content = content,
        modifier = modifier
            .requiredWidth(32.0.dp)
            .requiredHeight(32.0.dp)
            .alpha(alpha = 100.0f)
    )
}

@Composable
fun ImagenCambiarNombreUsuario(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.perfil_propio_imagen_cambiar_nombre_usuario),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .requiredWidth(18.0.dp)
            .requiredHeight(18.0.dp)
    )
}

@Composable
fun CirculoEditarNombreUsuario(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.perfil_propio_circulo_editar_nombre_usuario),
        modifier = modifier
            .requiredWidth(32.0.dp)
            .requiredHeight(32.0.dp)
    )
}

@Composable
fun ContenedorImagen(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        arrangement = RelayContainerArrangement.Row,
        itemSpacing = 24.0,
        content = content,
        modifier = modifier
    )
}

@Composable
fun BotonEditar(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        content = content,
        modifier = modifier
            .requiredWidth(48.0.dp)
            .requiredHeight(48.0.dp)
    )
}

@Composable
fun CirculoEditar(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.perfil_propio_circulo_editar),
        modifier = modifier
            .requiredWidth(48.0.dp)
            .requiredHeight(48.0.dp)
    )
}

@Composable
fun BotonEditarSynth(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        arrangement = RelayContainerArrangement.Row,
        content = content,
        modifier = modifier
            .requiredWidth(48.0.dp)
            .requiredHeight(48.0.dp)
            .alpha(alpha = 100.0f)
    )
}

@Composable
fun ImagenEditar(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.perfil_propio_imagen_editar),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .requiredWidth(32.0.dp)
            .requiredHeight(32.0.dp)
    )
}

@Composable
fun PublicacionesInstance(modifier: Modifier = Modifier, viewModel: ProfilesViewModel) {
    Publicaciones(
        modifier = modifier
            .fillMaxWidth(1.0f),
        posts = viewModel.posts
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourUserName(modifier: Modifier = Modifier,
                 editing : Boolean,
                 accesoTexto:()->String,
                 cambiarTexto : (String)->Unit) {

    if (editing)
        OutlinedTextField(
            value = accesoTexto.invoke(),
            onValueChange = cambiarTexto,
            textStyle = TextStyle(
                fontSize = 20.0.sp,
                fontFamily = inter,
                lineHeight = 1.2102272033691406.em,
                color = Color.Black
            ),
            shape = RoundedCornerShape(10),
            modifier = modifier
                .fillMaxWidth(0.7f)
                .wrapContentHeight(
                    align = Alignment.CenterVertically,
                    unbounded = true
                )
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(10.dp),
                    color = Color(
                        alpha = 38,
                        red = 0,
                        green = 0,
                        blue = 0
                    )
                )
                .background(
                    Color(
                        alpha = 255,
                        red = 225,
                        green = 196,
                        blue = 1
                    ),
                    shape = RoundedCornerShape(10)
                )
        )

    else
        Label(
            modifier = modifier.requiredWidth(94.0.dp),
            variacion = Variacion.TuPerfil
        )
}

@Composable
fun YourPostsLabel(modifier: Modifier = Modifier) {
    Label(
        modifier = modifier.requiredWidth(186.0.dp),
        variacion = Variacion.TusPublicaciones
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
        scrollable = true,
        itemSpacing = 24.0,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
