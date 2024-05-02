package com.example.petstagram.publicaciones

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
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
import androidx.compose.runtime.setValue
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
@Composable
fun Posts(
    modifier: Modifier = Modifier,
    controller : PostsUIController
) {

    val postsState by controller.posts.collectAsState()
    val scrollState = rememberScrollState()
    val isLoading by controller.isLoading.observeAsState()

    BoxWithConstraints {

        val localwidth by rememberSaveable {
          mutableFloatStateOf(maxWidth.value)
        }

        TopLevel(modifier = modifier.width(Dp(localwidth)), scrollState = scrollState) {
            LaunchedEffect(key1 = scrollState) {
                snapshotFlow { scrollState.isScrollInProgress }.collect{

                    controller.scroll(scrollState.value.toDouble() / scrollState.maxValue.toDouble())
                }
            }



            for (i in postsState){
                var seen by rememberSaveable {
                    mutableStateOf(false)
                }

                //Do NOT try to implement directly with controller as param, major usage of ram+bugs?¿
                AnimatedVisibility(visible = seen, enter = slideInHorizontally { it }) {

                    Post(modifier = Modifier
                        .width(Dp(localwidth))
                        .padding(vertical = 4.dp),
                        post = i,
                        controller = controller)
                }
                LaunchedEffect(key1 = seen) {
                    seen = true
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                if (isLoading == true){

                    CircularProgressIndicator(
                        Modifier
                            .width(200.dp)
                            .height(200.dp)
                            .padding(top = 16.dp))
                    Text(text = "Cargando",color = Color.Black)

                }else{
                    Text(text = "Cargando más publicaciones", color = Color.Black, modifier = Modifier.padding(vertical = 200.dp))
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
