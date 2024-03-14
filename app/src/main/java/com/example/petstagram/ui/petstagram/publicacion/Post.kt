package com.example.petstagram.publicacion

import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import coil.compose.SubcomposeAsyncImage
import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.example.petstagram.cuadroinfo.PostDownBar
import com.example.petstagram.cuadroinfo.TopPostLimit
import com.example.petstagram.ui.petstagram.DisplayVideo
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope

/**
 * publicacion
 *
 * This composable was generated from the UI Package 'publicacion'.
 * Generated code; do not edit directly
 */
@OptIn(UnstableApi::class) @SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState",
    "Range"
)
@kotlin.OptIn(ExperimentalFoundationApi::class)
@Composable
fun Post(modifier: Modifier = Modifier, post: Post, spectator : Profile,
         onLike: (Post)->Boolean,
         onSave: (Post)->Boolean) {

    val likes = MutableLiveData(post.likes.size)

    TopLevel(modifier = modifier) {
        CuadroInfoInstance(modifier = Modifier.rowWeight(1.0f), post = post)
        PostSource(modifier = Modifier
            .rowWeight(1.0f)
            .combinedClickable(
                enabled = true,
                onDoubleClick = {
                    onLike.invoke(post)
                    likes.value = post.likes.size
                },
                onClick = {}),
            post = post)
        PostButtons(
            modifier = Modifier.rowWeight(1.0f),
            post = post,
            spectator =spectator,
            likes = likes,
            onLike = {
                if(onLike.invoke(post)){
                    likes.value = post.likes.size
                    true
                }else{
                    likes.value = post.likes.size
                    false
                }
                     }
        ) { onSave.invoke(post) }
    }
}

@Composable
fun CuadroInfoInstance(modifier: Modifier = Modifier, post: Post) {
    TopPostLimit(
        modifier = modifier
            .fillMaxWidth(1.0f)
            .requiredHeight(48.0.dp),
        added = post
    )
}

@OptIn(UnstableApi::class) @Composable
fun PostSource(modifier: Modifier = Modifier, post: Post) {
    if (post.typeOfMedia == "image") {
        SubcomposeAsyncImage(
            modifier = modifier.fillMaxWidth(),
            model = post.source,
            loading = { CircularProgressIndicator(
                Modifier
                    .fillMaxWidth()
                    .height(400.dp))},
            contentDescription = post.title,
            contentScale = ContentScale.Crop
        )
    }else{
        DisplayVideo(source = post.source, modifier = modifier)}
}

@OptIn(UnstableApi::class) @Composable
fun PostButtons(
    modifier: Modifier = Modifier, post: Post, spectator: Profile, likes: MutableLiveData<Int>,
    onLike: ()->Boolean,
    onSave: ()->Boolean) {

    PostDownBar(
        modifier = modifier
            .fillMaxWidth(1.0f)
            .requiredHeight(48.0.dp),
        added = post,
        spectator = spectator,
        likes = likes,
        onLike = onLike,
        onSave = onSave
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
            red = 217,
            green = 217,
            blue = 217
        ),
        mainAxisAlignment = MainAxisAlignment.Start,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
