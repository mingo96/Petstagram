package com.example.petstagram.cuadroinfo

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.UIPost
import com.example.petstagram.fotoperfil.FotoPerfilBase
import com.example.petstagram.guardar.Guardar
import com.example.petstagram.like.Like
import com.example.petstagram.like.Pressed
import com.example.petstagram.opciones.Opciones
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText


@SuppressLint("MutableCollectionMutableState")
@Composable
fun PostDownBar(
    modifier: Modifier = Modifier,
    added: UIPost,
    spectator: Profile = Profile(),
    onLike: () -> Unit = {  },
    onSave: () -> Boolean = { false },
    likes: MutableLiveData<Int>
) {
    val likesCount by likes.observeAsState()

    var post by remember {
        mutableStateOf(added)
    }


    val density: Density = LocalDensity.current

    val onEnter = slideInVertically {
        // Slide in from 40 dp from the top.
        with(density) { -40.dp.roundToPx() }
    } + expandVertically(
        // Expand from the top.
        expandFrom = Alignment.Top
    ) + fadeIn(
        // Fade in with the initial alpha of 0.3f.
        initialAlpha = 0.3f
    )

    TopLevelVariacionInferior {

        ContenedorBotonesIzquierdaVariacionInferior {
            Box()
            {
                AnimatedVisibility(
                    visible = post.liked == Pressed.True,
                    enter = onEnter,
                    exit = ExitTransition.None
                ) {
                    LikePulsadoFalse(Modifier.clickable {
                        onLike.invoke()
                    }, post.liked)
                }

                AnimatedVisibility(
                    visible = post.liked == Pressed.False,
                    enter = onEnter,
                    exit = ExitTransition.None
                ) {
                    LikePulsadoFalse(Modifier.clickable {
                        onLike.invoke()
                    }, post.liked)
                }
            }
            Text(text = "Likes : $likesCount", style = TextStyle(color = Color.White))
            BotonSeccionComentariosVariacionInferior {
                TextoBotonComentariosVariacionInferior(modifier = Modifier
                    .rowWeight(1.0f)
                    .columnWeight(1.0f)
                    .clickable { })
            }
        }

        GuardarGuardarPulsadoNo(Modifier.clickable { onSave.invoke() })

    }
}

@Composable
fun TopPostLimit(modifier : Modifier, added : Post){
    TopLevelVariacionSuperior(modifier = modifier) {
        FotoPerfilSizePeque(picture = added.creatorUser!!.profilePic)
        TextoNombrePerfilVariacionSuperior(added = added.creatorUser!!.userName)
        OpcionesOpciones()
    }
}

@Composable
fun FotoPerfilSizePeque(modifier: Modifier = Modifier, picture: String) {
    FotoPerfilBase(modifier = modifier
        .requiredWidth(32.0.dp)
        .requiredHeight(32.0.dp),
        added = picture)
}

@Composable
fun TextoNombrePerfilVariacionSuperior(modifier: Modifier = Modifier, added: String) {
    RelayText(
        content = added,
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
        modifier = modifier
            .requiredWidth(264.0.dp)
            .requiredHeight(32.0.dp)
            .wrapContentHeight(
                align = Alignment.CenterVertically,
                unbounded = true
            )
    )
}

@Composable
fun OpcionesOpciones(modifier: Modifier = Modifier) {
    Opciones(modifier = modifier
        .requiredWidth(16.0.dp)
        .requiredHeight(32.0.dp))
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
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}

@Composable
fun LikePulsadoFalse(modifier: Modifier = Modifier, pressed: Pressed) {
    Like(modifier = modifier
        .requiredWidth(32.0.dp)
        .requiredHeight(32.0.dp), pressed = pressed)
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
        modifier = modifier
            .padding(
                paddingValues = PaddingValues(
                    start = 8.800048828125.dp,
                    top = 8.0.dp,
                    end = 8.799957275390625.dp,
                    bottom = 8.0.dp
                )
            )
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
            .wrapContentHeight(
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
        modifier = modifier
            .requiredWidth(176.0.dp)
            .requiredHeight(32.0.dp)
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
        modifier = modifier
            .fillMaxWidth(0.8f)
            .requiredHeight(48.0.dp)
    )
}

@Composable
fun GuardarGuardarPulsadoNo(modifier: Modifier = Modifier) {
    Guardar(modifier = modifier
        .requiredWidth(32.0.dp)
        .requiredHeight(32.0.dp))
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
