@file:kotlin.OptIn(ExperimentalFoundationApi::class)

package com.example.petstagram.publicacion

import android.view.Gravity
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.compose.ui.zIndex
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import coil.compose.SubcomposeAsyncImage
import com.example.petstagram.Controllers.PostsUIController
import com.example.petstagram.R
import com.example.petstagram.UiData.UIPost
import com.example.petstagram.cuadroinfo.PostDownBar
import com.example.petstagram.cuadroinfo.TopPostLimit
import com.example.petstagram.ui.petstagram.DisplayVideoFromSource
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
@Composable
fun Post(modifier: Modifier = Modifier, post: UIPost,
    controller :PostsUIController, isVisible : Boolean)
{
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

        CuadroInfoInstance(modifier = Modifier.rowWeight(1.0f).zIndex(1F), post = post, controller = controller)

        PostSource(modifier = Modifier
            .zIndex(0F)
            .rowWeight(1.0f)
            .heightIn(0.dp,500.dp)
            .combinedClickable(
                enabled = true,
                onDoubleClick = {
                    controller.likeOnPost(post)
                    likes.value = post.likes.size
                },
                onClick = {}),
            controller = controller,
            post = post,
            likes = likes,
            isVisible = isVisible)

        PostDownBar(
            controller = controller,
            added = post,
            likes = likes,
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
                    enter = fadeIn()+expandVertically { it } + slideInVertically { it },
                    exit = fadeOut()+shrinkVertically { it } + slideOutVertically { it }
                ) {
                    //align bottom

                    val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
                    dialogWindowProvider.window.setGravity(Gravity.BOTTOM)

                    CommentsSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.7f),

                        controller = controller,
                        post = post
                        //postComment = onComment,
                        //commentLiked = onCommentLiked
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
fun CuadroInfoInstance(modifier: Modifier = Modifier, post: UIPost, controller: PostsUIController?=null) {
    TopPostLimit(
        modifier = modifier
            .fillMaxWidth(1.0f)
            .requiredHeight(48.0.dp),
        added = post,
        controller = controller
    )
}

@OptIn(UnstableApi::class) @Composable
fun PostSource(modifier: Modifier = Modifier, 
               post: UIPost, 
               controller: PostsUIController,
               likes : MutableLiveData<Int>? = null,
               isVisible: Boolean) {


    when (post.typeOfMedia) {
        "image" -> {

            SubcomposeAsyncImage(
                modifier = modifier
                    .fillMaxWidth(),
                model = post.UIURL,
                loading = { CircularProgressIndicator(
                    Modifier
                        .fillMaxWidth()
                        .height(400.dp))},
                contentDescription = post.title,
                contentScale = ContentScale.Crop
            )
        }
        "video" -> {
            if (post.mediaItem != MediaItem.EMPTY)
                DisplayVideoFromSource(source = post.mediaItem, modifier = modifier, onDoubleTap = {
                    controller.likeOnPost(post)
                    likes!!.value = post.likes.size
                },
                    onTap = {controller.toggleStop()},
                    isVisible = isVisible,
                    uri = post.UIURL)

        }
        else -> {
            Image(
                painter = painterResource(id = R.drawable.acceso_aperfil_imagen_galeria),
                contentDescription = "Aún nada",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }
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
