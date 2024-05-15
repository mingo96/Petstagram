package com.example.petstagram.cuadroinfo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.example.petstagram.Controllers.PostsUIController
import com.example.petstagram.R
import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.UIPost
import com.example.petstagram.cuadrotexto.PostTitle
import com.example.petstagram.fotoperfil.FotoPerfilBase
import com.example.petstagram.guardar.Guardar
import com.example.petstagram.guardar.SavePressed
import com.example.petstagram.like.Like
import com.example.petstagram.like.Pressed
import com.example.petstagram.opciones.Opciones
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText
import com.google.relay.compose.RelayVector

@Composable
fun PostDownBar(
    added: UIPost,
    saved: MutableLiveData<SavePressed>,
    likes: MutableLiveData<Int>,
    tapOnComments: () -> Unit = {},
    controller: PostsUIController? = null
) {

    val likesCount by likes.observeAsState()

    val isSaved by saved.observeAsState()

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


    Container {
        TitleContainer {
            PostTitle(title = "${added.creatorUser!!.userName}: ${added.title}")
        }
        IntersectLine(
            modifier = Modifier
                .boxAlign(
                    alignment = Alignment.BottomStart,
                    offset = DpOffset(
                        x = 0.0.dp,
                        y = (-1.0).dp
                    )
                )
                .rowWeight(1.0f))
        Row (horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)){
            Box()
            {
                this@Row.AnimatedVisibility(
                    visible = added.liked == Pressed.True,
                    enter = onEnter,
                    exit = ExitTransition.None
                ) {
                    LikePulsadoFalse(Modifier.clickable {
                        controller?.likeOnPost(added)
                        likes.value = added.likes.size
                    }, added.liked)
                }

                this@Row.AnimatedVisibility(
                    visible = added.liked == Pressed.False,
                    enter = onEnter,
                    exit = ExitTransition.None
                ) {
                    LikePulsadoFalse(Modifier.clickable {
                        controller?.likeOnPost(added)
                        likes.value = added.likes.size
                    }, added.liked)
                }
            }
            Text(text = "Likes : ${(likesCount!!).quantity()}", style = TextStyle(color = Color.White))
            BotonSeccionComentariosVariacionInferior {
                TextoBotonComentariosVariacionInferior(modifier = Modifier
                    .rowWeight(1.0f)
                    .columnWeight(1.0f)
                    .clickable { tapOnComments.invoke() })
            }
            Box {
                this@Row.AnimatedVisibility(
                    visible = isSaved== SavePressed.No,
                    enter = onEnter,
                    exit = ExitTransition.None
                ) {
                    SaveIcon(Modifier.clickable {
                        if(controller?.saveClicked(added) == true)
                            saved.value = SavePressed.Si
                        else
                            saved.value = SavePressed.No
                    }, isSaved!!)
                }
                this@Row.AnimatedVisibility(
                    visible = isSaved == SavePressed.Si,
                    enter = onEnter,
                    exit = ExitTransition.None
                ) {
                    SaveIcon(Modifier.clickable {
                        if(controller?.saveClicked(added) == true)
                            saved.value = SavePressed.No
                        else
                            saved.value = SavePressed.Si

                    }, isSaved!!)
                }
            }
        }


    }
}

@Composable
fun DeadPostDownBar(creatorUser : Profile, title : String){
    Container {

        TitleContainer {
            PostTitle(title = "${creatorUser.userName}: ${title}")
        }
        IntersectLine(
            modifier = Modifier
                .boxAlign(
                    alignment = Alignment.BottomStart,
                    offset = DpOffset(
                        x = 0.0.dp,
                        y = (-1.0).dp
                    )
                )
                .rowWeight(1.0f))
        Row (horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)){

            LikePulsadoFalse(Modifier.clickable {
            }, Pressed.False)


            Text(text = "Likes : 0", style = TextStyle(color = Color.White))
            BotonSeccionComentariosVariacionInferior {
                TextoBotonComentariosVariacionInferior(modifier = Modifier
                    .rowWeight(1.0f)
                    .columnWeight(1.0f)
                    .clickable { })
            }
            SaveIcon(Modifier.clickable {
            }, SavePressed.No)

        }


    }
}

/**returns the amount as a string but if more than 3 0's, in thousands, if more than 6 0's, millions*/
fun Int.quantity(): String{

    var spare = this.toString()
    var times = 0
    while (spare.length>4){
        when(times){
            0->{
                spare = spare.substring(0..spare.length-4)+"k"
            }
            1->{
                spare = spare.substring(0..spare.length-5)+"m"
            }
        }
        times++
    }
    return spare

}

@Composable
fun TopPostLimit(modifier : Modifier = Modifier, added : UIPost, controller: PostsUIController? = null){
    val context = LocalContext.current
    TopLevelVariacionSuperior(modifier = modifier) {
        FotoPerfilSizePeque(picture = added.creatorUser!!.profilePic)
        ProfileName(added = added.creatorUser!!.userName)
        //guardar, reportar
        OpcionesOpciones(modifier.clickable {
            controller?.optionsClicked(post = added)
        })
    }
}

@Composable
fun FotoPerfilSizePeque(modifier: Modifier = Modifier, picture: String) {
    FotoPerfilBase(modifier = modifier
        .requiredWidth(32.dp)
        .requiredHeight(32.0.dp),
        added = picture)
}


@Composable
fun TitleContainer(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        mainAxisAlignment = MainAxisAlignment.Start,
        arrangement = RelayContainerArrangement.Row,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}


@Composable
fun IntersectLine(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.cuadro_info_line_1),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .requiredHeight(1.dp)
    )
}

@Composable
fun ProfileName(modifier: Modifier = Modifier, added: String) {
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
fun SaveIcon(modifier: Modifier = Modifier, variation : SavePressed) {
    Guardar(modifier = modifier
        .requiredWidth(32.0.dp)
        .requiredHeight(32.0.dp),
        savePressed = variation)
}

@Composable
fun Container(
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
        mainAxisAlignment = MainAxisAlignment.Center,
        arrangement = RelayContainerArrangement.Column,
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
