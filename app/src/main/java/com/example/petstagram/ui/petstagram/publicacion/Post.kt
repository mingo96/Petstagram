package com.example.petstagram.publicacion

import android.annotation.SuppressLint
import android.view.Gravity
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import coil.compose.SubcomposeAsyncImage
import com.example.petstagram.UiData.Comment
import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.UIComment
import com.example.petstagram.UiData.UIPost
import com.example.petstagram.cuadroinfo.PostDownBar
import com.example.petstagram.cuadroinfo.TopPostLimit
import com.example.petstagram.guardar.SavePressed
import com.example.petstagram.like.Pressed
import com.example.petstagram.ui.petstagram.DisplayVideo
import com.example.petstagram.ui.petstagram.seccioncomentarios.CommentsSection
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
fun Post(modifier: Modifier = Modifier, post: UIPost,
         onLike: (Post)->Unit,
         onSave: (Post)->Boolean,
         onComment: (String) -> Unit,
         onCommentLiked: (UIComment) -> Boolean) {

    var commentsDisplayed by rememberSaveable {
        mutableStateOf(false)
    }

    val coroutine = rememberCoroutineScope()

    var animationDisplayer by rememberSaveable {
        mutableStateOf(false)
    }

    val likes = MutableLiveData(post.likes.size)

    val saved = MutableLiveData(post.saved)

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

        PostDownBar(
            modifier = modifier.rowWeight(1.0f),
            added = post,
            likes = likes,
            onLike = {
                onLike.invoke(post)
                likes.value = post.likes.size
            },
            onSave = {
                onSave.invoke(post)
            },
            saved = saved,
            tapOnComments = {commentsDisplayed = !commentsDisplayed}
        )

        if (commentsDisplayed) {
            Dialog(
                onDismissRequest = {
                    coroutine.launch {
                        animationDisplayer = false
                        delay(300)
                        commentsDisplayed = !commentsDisplayed
                    }
                },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    usePlatformDefaultWidth = false
                )
            )
            {

                //on open, set display animation to true
                LaunchedEffect(Unit) {
                    animationDisplayer = true
                }

                //animations
                AnimatedVisibility(
                    visible = commentsDisplayed && animationDisplayer,
                    enter = slideInVertically {
                        it
                    },
                    exit = slideOutVertically { it }
                ) {
                    //align bottom

                    val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
                    dialogWindowProvider.window.setGravity(Gravity.BOTTOM)

                    CommentsSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.7f),
                        comments = post.UIComments,
                        account = post.creatorUser!!,
                        postComment = onComment,
                        commentLiked = onCommentLiked
                    )

                }
                //on close, animationDisplayer closes
                DisposableEffect(Unit) {
                    onDispose {
                        animationDisplayer = false
                    }
                }
            }
        }
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
fun PostSource(modifier: Modifier = Modifier, post: UIPost) {


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
        DisplayVideo(source = post.UIsource, modifier = modifier)
    }
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
