package com.example.petstagram.ui.petstagram.seccioncomentarios

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.petstagram.UiData.Comment
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.UIComment
import com.example.petstagram.like.Pressed
import com.example.petstagram.ui.petstagram.comentario.Comment
import com.example.petstagram.seccioncomentarios.inter
import com.google.relay.compose.Clip
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText

/**
 * Seccion de comentarios
 *
 * This composable was generated from the UI Package 'seccion_comentarios'.
 * Generated code; do not edit directly
 */
@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsSection(
    modifier: Modifier = Modifier,
    comments: MutableList<UIComment>,
    account: Profile,
    postComment: (String)->Unit,
    commentLiked: (UIComment)->Boolean) {

    var commenting by remember {
        mutableStateOf(false)
    }

    var commentList by remember {
        mutableStateOf(comments)
    }

    val context = LocalContext.current

    TopLevel(modifier = modifier) {
        var content by remember{ mutableStateOf("")}

        CommentsTopSection(modifier = Modifier.rowWeight(1.0f)) {
            if (!commenting) {
                CommentsTitle(userName = account.userName)
            }
            else{
                OutlinedTextField(value = content,
                    onValueChange = {content = it},
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .wrapContentHeight(
                            align = Alignment.CenterVertically,
                            unbounded = true
                        )
                        .padding(end = 16.dp)
                    ,
                    label = { Text(text = "Introduce tu comentario")},
                    shape = RoundedCornerShape(30),
                    textStyle = TextStyle(color = Color.White, lineHeight = 1.em),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.White,
                        containerColor = Color.DarkGray,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray
                    )
                )
            }
            BotonMas(modifier = Modifier
                .columnWeight(1.0f)
                .clickable {
                    if (!commenting) {
                        commenting = true
                    } else {
                        if (content.length >= 50) {
                            Toast
                                .makeText(context, "Comentario demasiado largo", Toast.LENGTH_SHORT)
                                .show()
                        } else if(content.isNotEmpty()){
                            postComment(content)
                            commentList = comments
                            commenting = false
                        }else{
                            Toast
                                .makeText(context, "Comentario vacío", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }) {
                CuadroSumar(
                    modifier = Modifier
                        .rowWeight(1.0f)
                        .columnWeight(1.0f)
                )
            }
        }
        Comentarios(modifier = Modifier.rowWeight(1.0f)) {

            for (i in commentList){
                Comment(comment = i, onLike = {
                    commentLiked.invoke(i)
                })
            }
        }
    }
}


@Composable
fun CommentsTitle(modifier: Modifier = Modifier, userName : String) {
    RelayText(
        content = "Comentarios de la publicación de $userName",
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.4285714721679688.em,
        letterSpacing = 0.10000000149011612.sp,
        fontWeight = FontWeight(500.0.toInt()),
        maxLines = 2,
        modifier = modifier
            .fillMaxWidth(0.7f)
            .requiredHeight(40.0.dp)
            .wrapContentHeight(
                align = Alignment.CenterVertically,
                unbounded = true
            )
            .padding(end = 16.dp)
    )
}

@Composable
fun CuadroSumar(modifier: Modifier = Modifier) {
    Text(text = "+",
        style = TextStyle(
            fontSize = 40.sp,
            fontWeight = FontWeight(500.0.toInt())
        ),modifier = modifier)
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
        modifier = modifier
            .fillMaxWidth(0.7f)
            .wrapContentHeight(
                align = Alignment.CenterVertically,
                unbounded = false
            )
    )
}

@Composable
fun CommentsTopSection(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        mainAxisAlignment = MainAxisAlignment.SpaceEvenly,
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(
            horizontal = 16.0.dp
        ),
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).wrapContentHeight(align = Alignment.CenterVertically)
    )
}


@Composable
fun Comentarios(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        mainAxisAlignment = MainAxisAlignment.Start,
        scrollable = true,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight()
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
        padding = PaddingValues(
            start = 8.0.dp,
            top = 16.0.dp,
            end = 8.0.dp,
            bottom = 0.0.dp
        ),
        radius = 20.0,
        itemSpacing = 16.0,
        content = content,
        modifier = modifier.fillMaxHeight(1.0f)
            .border(
                width = 4.0.dp,
                color = Color(
                    alpha = 255,
                    red = 225,
                    green = 196,
                    blue = 1
                ),
                shape = RoundedCornerShape(5,5))
    )
}
