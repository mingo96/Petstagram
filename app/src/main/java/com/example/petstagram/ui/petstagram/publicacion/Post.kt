package com.example.petstagram.publicacion

import android.net.Uri
import android.view.Gravity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import coil.compose.SubcomposeAsyncImage
import com.example.petstagram.Controllers.PostsUIController
import com.example.petstagram.R
import com.example.petstagram.UiData.UIPost
import com.example.petstagram.ViewModels.ProfileObserverViewModel
import com.example.petstagram.cuadroinfo.PostDownBar
import com.example.petstagram.cuadroinfo.TopPostLimit
import com.example.petstagram.like.Like
import com.example.petstagram.like.Pressed
import com.example.petstagram.ui.petstagram.DisplayVideoFromPost
import com.example.petstagram.ui.petstagram.seccioncomentarios.CommentsSection
import com.example.petstagram.ui.theme.Secondary
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.tappable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Post
 * using a controller to control the post
 * and the Vision of it as a boolean
 */
@Composable
fun Post(
    modifier: Modifier = Modifier, post: UIPost, controller: PostsUIController, isVisible: Boolean
) {
    /**spare variable for comments animation*/
    var commentsDisplayed by rememberSaveable {
        mutableStateOf(false)
    }

    /**spare variable for comments animation*/
    val coroutine = rememberCoroutineScope()

    /**spare variable for comments animation*/
    var animationDisplayer by rememberSaveable {
        mutableStateOf(false)
    }

    /**spare variable to control the number of likes in a way that causes a recomposition*/
    val likes = MutableLiveData(post.likes.size)

    /**spare variable to control if the post is saved in a way that causes a recomposition*/
    val saved = MutableLiveData(post.saved)

    TopLevel(modifier = modifier) {

        PostTopBar(
            modifier = Modifier
                .rowWeight(1.0f)
                .zIndex(1F), post = post, controller = controller
        )

        PostSource(
            modifier = Modifier
                .zIndex(0F)
                .rowWeight(1.0f)
                .heightIn(100.dp, 450.dp)
                .tappable(onDoubleTap = {
                    controller.likeOnPost(post)
                    likes.value = post.likes.size
                }), controller = controller, post = post, likes = likes, isVisible = isVisible
        )

        PostDownBar(controller = controller,
            added = post,
            likes = likes,
            saved = saved,
            tapOnComments = { commentsDisplayed = !commentsDisplayed })

        if (commentsDisplayed) {
            Dialog(
                onDismissRequest = {
                    coroutine.launch {
                        animationDisplayer = false
                        delay(300)
                        commentsDisplayed = !commentsDisplayed
                    }
                }, properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    usePlatformDefaultWidth = false
                )
            ) {

                //on open, set display animation to true
                LaunchedEffect(Unit) {
                    animationDisplayer = true
                }

                //animations
                AnimatedVisibility(visible = commentsDisplayed && animationDisplayer,
                    enter = fadeIn() + expandVertically { it } + slideInVertically { it },
                    exit = fadeOut() + shrinkVertically { it } + slideOutVertically { it }) {
                    //align bottom

                    val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
                    dialogWindowProvider.window.setGravity(Gravity.BOTTOM)

                    CommentsSection(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f),

                        controller = controller, post = post, navigateToUser = {
                            coroutine.launch {
                                animationDisplayer = false
                                delay(300)
                                commentsDisplayed = !commentsDisplayed
                            }
                            controller.navController.navigate(
                                if (controller.actualUser.id == it) "perfilPropio" else {
                                    ProfileObserverViewModel.staticProfile.id = it
                                    "perfilAjeno"
                                }
                            )
                        }
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
fun PostTopBar(
    modifier: Modifier = Modifier, post: UIPost, controller: PostsUIController? = null
) {
    TopPostLimit(
        modifier = modifier
            .fillMaxWidth(1.0f)
            .requiredHeight(48.0.dp),
        added = post,
        controller = controller
    )
}

/**displays the post image or video*/
@Composable
fun PostSource(
    modifier: Modifier = Modifier,
    post: UIPost,
    controller: PostsUIController,
    likes: MutableLiveData<Int>? = null,
    isVisible: Boolean
) {
    //animations
    val enter = scaleIn() + fadeIn() + slideInVertically { it }
    val exit = scaleOut() + fadeOut() + slideOutVertically()

    Box(contentAlignment = Alignment.Center) {
        //like animation holder
        val likedPost by controller.likedPost.observeAsState()
        AnimatedVisibility(
            visible = likedPost == post,
            modifier = Modifier
                .fillMaxSize(0.5f)
                .zIndex(10F),
            enter = enter,
            exit = exit
        ) {
            Like(
                pressed = Pressed.True, modifier = Modifier
                    .size(150.dp)
                    .zIndex(10F)
            )
        }

        when (post.typeOfMedia) {
            "image" -> {

                //if file is not downloaded tries to load from cache
                if (post.UIURL == Uri.EMPTY) {
                    CacheImg(modifier = modifier, post = post)
                } else {
                    PostImg(modifier = modifier, post = post)
                }

            }

            "video" -> {
                /**represents if the video is stopped*/
                val videoStopped by controller.videoStopped.observeAsState(false)
                /**represents if the video mode is image or video*/
                val videoModeAnimation by controller.videoMode.observeAsState(false)
                /**spare variable to control the video animation, when null, the animation is being displayed*/
                val pauseAnimationState by controller.pauseAnimationState.observeAsState()

                if (post.mediaItem != MediaItem.EMPTY) DisplayVideoFromPost(source = post,
                    modifier = modifier,
                    onTap = {
                        controller.animatePause()
                    },
                    onDoubleTap = {
                        controller.likeOnPost(post)
                        //we call this so UI recomposes
                        likes!!.value = post.likes.size
                    },
                    onLongTap = {
                        controller.toggleStop()
                        controller.animateVideoMode()
                    },
                    //when videoModeAnimation is null animation is displayed, else it has normal use
                    isVisible = if (videoModeAnimation) null else isVisible && !videoStopped,
                    pauseAnimationState = pauseAnimationState
                )
                else

                    LinearProgressIndicator(
                        modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .background(Color.Black),
                        color = Secondary,
                    )

            }

            //rare case bad post got loaded
            else -> {
                DefaultImg()
            }
        }
    }

}

@Composable
fun DefaultImg() {

    Image(
        painter = painterResource(id = R.drawable.acceso_aperfil_imagen_galeria),
        contentDescription = "Aún nada",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PostImg(modifier: Modifier, post: UIPost) {

    SubcomposeAsyncImage(
        modifier = modifier.fillMaxWidth(), model = post.UIURL, loading = {
            LinearProgressIndicator(
                modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .background(Color.Black),
                color = Secondary,
            )
        }, contentDescription = post.title, contentScale = ContentScale.Crop
    )
}

@Composable
fun CacheImg(modifier: Modifier, post: UIPost) {

    SubcomposeAsyncImage(modifier = modifier.fillMaxWidth(), model = post.source, loading = {
        LinearProgressIndicator(
            modifier
                .fillMaxWidth()
                .height(500.dp)
                .background(Color.Black),
            color = Secondary,
        )
    }, error = {
        LinearProgressIndicator(
            modifier
                .fillMaxWidth()
                .height(500.dp)
                .background(Color.Black),
            color = Secondary,
        )
    }, contentDescription = post.title, contentScale = ContentScale.Crop
    )
}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier, content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color.Black,
        mainAxisAlignment = MainAxisAlignment.Start,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
