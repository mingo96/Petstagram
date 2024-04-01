package com.example.petstagram.ui.petstagram.comentario

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.petstagram.R
import com.example.petstagram.UiData.UIComment
import com.example.petstagram.cuadroinfo.FotoPerfilSizePeque
import com.example.petstagram.cuadroinfo.LikePulsadoFalse
import com.example.petstagram.like.Pressed
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
fun Comment(modifier: Modifier = Modifier, comment: UIComment, onLike : ()->Boolean) {
    val density: Density = LocalDensity.current

    val onEnter = slideInHorizontally {
        // Slide in from 40 dp from the top.
        with(density) { 40.dp.roundToPx() }
    } + expandHorizontally(
        // Expand from the top.
        expandFrom = Alignment.End
    ) + fadeIn(
        // Fade in with the initial alpha of 0.3f.
        initialAlpha = 0.3f
    )
    var locallyLiked by remember {
        mutableStateOf(comment.liked)
    }

    TopLevel(modifier = modifier) {


        Row(
            horizontalArrangement = Arrangement.SpaceBetween,verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically){
                FotoPerfilSizePeque(picture = comment.objectUser.profilePic)

                CommentContent(
                    modifier = Modifier,
                    content = comment.commentText
                )

                AnimatedVisibility(
                    visible = locallyLiked == Pressed.True,
                    enter = onEnter,
                    exit = ExitTransition.None,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Row {

                        Text(text = "${comment.likes.size}",
                            style = TextStyle(color = Color.White, fontSize = 10.sp))
                        LikePulsadoFalse(Modifier.clickable {
                            locallyLiked = if(onLike.invoke())
                                Pressed.True
                            else
                                Pressed.False

                        }, locallyLiked)
                    }
                }


                AnimatedVisibility(
                    visible = locallyLiked == Pressed.False,
                    enter = onEnter,
                    exit = ExitTransition.None,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Row{
                        Text(
                            text = "${comment.likes.size}",
                            style = TextStyle(color = Color.White,fontSize = 10.sp)
                        )
                        LikePulsadoFalse(Modifier.clickable {
                            locallyLiked = if (onLike.invoke())
                                Pressed.True
                            else
                                Pressed.False
                        }, locallyLiked)
                    }
                }
            }

        }
        TopLine(
            modifier = Modifier
                .boxAlign(
                    alignment = Alignment.BottomStart,
                    offset = DpOffset(
                        x = 0.0.dp,
                        y = (-1.0).dp
                    )
                )
                .rowWeight(1.0f)
        )
    }
}

@Composable
fun TopLine(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.comentario_linea_inferior),
        modifier = modifier
            .padding(
                paddingValues = PaddingValues(
                    start = 16.0.dp,
                    top = 0.0.dp,
                    end = 16.0.dp,
                    bottom = 0.0.dp
                )
            )
            .fillMaxWidth(1.0f)
            .requiredHeight(1.dp)
    )
}

@Composable
fun CommentContent(modifier: Modifier = Modifier, content : String) {
    RelayText(
        content = content,
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
        modifier = modifier
            .padding(
                paddingValues = PaddingValues(
                    start = 8.0.dp,
                    top = 8.0.dp,
                    end = 0.0.dp,
                    bottom = 8.0.dp
                )
            )
            .fillMaxWidth(0.85f)
            .fillMaxHeight(1.0f)
            .wrapContentHeight(
                align = Alignment.CenterVertically,
                unbounded = true
            )
    )
}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = true,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
            .padding(horizontal = 8.dp)
    )
}
