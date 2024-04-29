package com.example.petstagram.publicaciones

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.petstagram.Controllers.PostsUIController
import com.example.petstagram.guardar.SavePressed
import com.example.petstagram.like.Pressed
import com.example.petstagram.publicacion.Post

/**
 * publicaciones
 *
 * This composable was generated from the UI Package 'publicaciones'.
 * Generated code; do not edit directly
 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun Posts(
    modifier: Modifier = Modifier,
    controller : PostsUIController
) {

    val postsState by controller.posts.collectAsState()
    val scrollState = rememberScrollState()

    BoxWithConstraints {

        val localwidth by rememberSaveable {
          mutableFloatStateOf(maxWidth.value)
        }

        TopLevel(modifier = modifier.width(Dp(localwidth)), scrollState = scrollState) {
            LaunchedEffect(key1 = scrollState) {
                if(scrollState.maxValue!=0) {
                    snapshotFlow { scrollState.isScrollInProgress }.collect{
                        controller.scroll(scrollState.value.toDouble() / scrollState.maxValue.toDouble())
                    }
                }
            }


            for (i in postsState){

                //Do NOT try to implement directly with controller as param, major usage of ram+bugs?¿
                Post(modifier = Modifier
                    .width(Dp(localwidth))
                    .padding(vertical = 4.dp),
                    post = i,
                    onLike = {
                        if(controller.likeOnPost(i))
                            i.liked = Pressed.True
                        else
                            i.liked = Pressed.False
                             },
                    onSave = {
                        if (i.saved==SavePressed.Si){
                            i.saved=SavePressed.No
                        }else
                            i.saved=SavePressed.Si
                        controller.saveClicked(i)
                    },
                    onComment = {
                        controller.comment(it,i)
                    },
                    onCommentLiked = {
                        if(controller.likeOnComment(it)){
                            it.liked = Pressed.True
                            true
                        }else{
                            it.liked = Pressed.False
                            false
                        }
                    })
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                if (controller.alreadyLoading){

                    CircularProgressIndicator(Modifier.width(200.dp).height(200.dp))
                    Text(text = "Cargando",color = Color.Black)

                }else{

                    Text(text = "Parece que no hay más publicaciones", color = Color.Black)
                }
            }
            
        }
    }
}


@Composable
fun TopLevel(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
            .background(
                Color(
                    alpha = 255,
                    red = 224,
                    green = 164,
                    blue = 0
                )
            )
            .verticalScroll(
                state = scrollState
            )

    )

}
