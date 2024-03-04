package com.example.petstagram.publicaciones

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.petstagram.UiData.Post
import com.example.petstagram.publicacion.Publicacion
import com.google.relay.compose.BoxScopeInstance.scope
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import kotlinx.coroutines.flow.StateFlow

/**
 * publicaciones
 *
 * This composable was generated from the UI Package 'publicaciones'.
 * Generated code; do not edit directly
 */
@Composable
fun Posts(modifier: Modifier = Modifier, posts : StateFlow<List<Post>>, onScroll : (Double)->Unit) {
    BoxWithConstraints {
        val postsState by posts.collectAsStateWithLifecycle()
        val anchoMax = maxWidth
        val scrollState = rememberScrollState()
        TopLevel(modifier = modifier.width(maxWidth), scrollState = scrollState) {
            for (i in postsState){
                if(scrollState.maxValue!=0) {
                    val screenScrolled =
                        scrollState.value.toDouble() / scrollState.maxValue.toDouble()
                    onScroll.invoke(screenScrolled)
                }
                PublicacionInstance(modifier = Modifier
                    .width(anchoMax)
                    .padding(vertical = 4.dp), post = i, url = i.source)
            }
        }
    }
}

@Composable
fun PublicacionInstance(
    modifier: Modifier = Modifier,
    post: Post,
    url: String
) {
    Publicacion(modifier = modifier
        .fillMaxWidth(1.0f),
        post = post,
        url = url)
}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    content: @Composable ColumnScope.() -> Unit
) {
    //RelayContainer(
    //    backgroundColor = Color(
    //        alpha = 255,
    //        red = 224,
    //        green = 164,
    //        blue = 0
    //    ),
    //    mainAxisAlignment = MainAxisAlignment.Start,
    //    scrollable = true,
    //    itemSpacing = 8.0,
    //    content = content,
    //    modifier = modifier
    //        .fillMaxWidth(1.0f)
    //        .fillMaxHeight(1.0f)
    //)
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
