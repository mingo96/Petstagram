package com.example.petstagram.publicaciones

import android.annotation.SuppressLint
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.petstagram.Controllers.PostsUIController
import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.UIPost
import com.example.petstagram.publicacion.Post
import kotlinx.coroutines.flow.StateFlow

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
    BoxWithConstraints {

        val postsState by controller.posts.collectAsStateWithLifecycle()
        val localwidth = maxWidth
        val scrollState = rememberScrollState()
        val spectator by mutableStateOf(controller.actualUser)

        TopLevel(modifier = modifier.width(maxWidth), scrollState = scrollState) {
            if(scrollState.maxValue!=0) {
                val screenScrolled =
                    scrollState.value.toDouble() / scrollState.maxValue.toDouble()
                controller.scroll(screenScrolled)
            }
            for (i in postsState){

                PostInstance(modifier = Modifier
                    .width(localwidth)
                    .padding(vertical = 4.dp), post = i,
                    spectator = spectator,
                    onLike = {controller.likeClicked(i)},
                    onSave = { controller.saveClicked(i)})
            }
        }
    }
}

@Composable
fun PostInstance(
    modifier: Modifier = Modifier,
    post: UIPost,
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
