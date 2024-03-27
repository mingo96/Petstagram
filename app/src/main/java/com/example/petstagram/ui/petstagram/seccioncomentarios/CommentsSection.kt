package com.example.petstagram.ui.petstagram.seccioncomentarios

import android.graphics.drawable.shapes.Shape
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.petstagram.UiData.Comment
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.UIComment
import com.example.petstagram.ui.petstagram.comentario.Comment
import com.example.petstagram.seccioncomentarios.inter
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsSection(modifier: Modifier = Modifier,
                    comments:MutableList<UIComment>,
                    account : Profile,
                    postComment : (String)->Boolean) {

    var commenting by remember {
        mutableStateOf(false)
    }

    TopLevel(modifier = modifier) {
        var content by remember{ mutableStateOf("")}

        CommentsTopSection(modifier = Modifier.rowWeight(1.0f)) {
            if (!commenting) {
                CommentsTitle(userName = account.userName)
            }
            else{
                OutlinedTextField(value = content,
                    onValueChange = {content = it},
                    modifier = Modifier.padding(end = 8.dp),
                    label = { Text(text = "Introduce tu comentario")},
                    shape = RoundedCornerShape(15),
                    textStyle = TextStyle(Color.White)
                )
            }
            BotonMas(modifier = Modifier
                .columnWeight(1.0f)
                .clickable {
                    if (!commenting) {
                        commenting = true
                    }else{
                        if(postComment.invoke(content)){

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
            for (i in comments){
                Comment(comment = i)
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
            .requiredWidth(280.0.dp)
            .requiredHeight(40.0.dp)
            .wrapContentHeight(
                align = Alignment.CenterVertically,
                unbounded = true
            )
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
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
            .wrapContentHeight(
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
        modifier = modifier
            .requiredWidth(64.0.dp)
            .requiredHeight(40.dp)
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
        modifier = modifier.fillMaxWidth(1.0f)
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
        itemSpacing = 16.0,
        strokeWidth = 1.0,
        strokeColor = Color(
            alpha = 255,
            red = 225,
            green = 196,
            blue = 1
        ),
        radius = 20.0,
        content = content,
        modifier = modifier.fillMaxHeight(1.0f)
    )
}
