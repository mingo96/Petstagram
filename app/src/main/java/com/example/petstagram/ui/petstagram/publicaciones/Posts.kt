package com.example.petstagram.publicaciones

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.example.petstagram.publicacion.Post
import kotlinx.coroutines.flow.StateFlow

/**
 * publicaciones
 *
 * This composable was generated from the UI Package 'publicaciones'.
 * Generated code; do not edit directly
 */
@Composable
fun Posts(
    modifier: Modifier = Modifier, posts: StateFlow<List<Post>>,
    spectator: Profile,
    onScroll: (Double) -> Unit,
    onLike: (Post) -> Boolean,
    onSave: (Post) -> Boolean
) {
    BoxWithConstraints {

        val postsState by posts.collectAsStateWithLifecycle()
        val localwidth = maxWidth
        val scrollState = rememberScrollState()

        TopLevel(modifier = modifier.width(maxWidth), scrollState = scrollState) {
            for (i in postsState){
                if(scrollState.maxValue!=0) {
                    val screenScrolled =
                        scrollState.value.toDouble() / scrollState.maxValue.toDouble()
                    onScroll.invoke(screenScrolled)
                }
                PostInstance(modifier = Modifier
                    .width(localwidth)
                    .padding(vertical = 4.dp), post = i,
                    spectator = spectator,
                    onLike = onLike,
                    onSave = onSave)
            }
        }
    }
}

@Composable
fun PostInstance(
    modifier: Modifier = Modifier,
    post: Post,
    spectator: Profile,
    onLike: (Post) -> Boolean,
    onSave: (Post) -> Boolean
) {
    Post(modifier = modifier
        .fillMaxWidth(1.0f),
        post = post,
        spectator = spectator,
        onLike = onLike,
        onSave = onSave)
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
