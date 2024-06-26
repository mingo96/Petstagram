package com.example.petstagram.ui.petstagram

import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.petstagram.UiData.UIPost
import com.google.relay.compose.tappable

/**function that uses [ExoPlayer] to display a video given an Uri in string format
 * works for local files and urls*/
@OptIn(UnstableApi::class)
@Composable
fun DisplayVideoFromSource(
    source: MediaItem,
    modifier: Modifier,
    onDoubleTap: () -> Unit = {},
    onLongTap: () -> Unit = {},
    isVisible: Boolean? = true,
    uri: Uri = Uri.EMPTY
) {

    val context = LocalContext.current
    //main controller
    val mediaPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    //on launch set content and basic configuration
    LaunchedEffect(source) {
        mediaPlayer.setMediaItem(source)
        mediaPlayer.repeatMode = Player.REPEAT_MODE_ALL
        mediaPlayer.trackSelectionParameters =
            mediaPlayer.trackSelectionParameters.buildUpon().setMaxVideoFrameRate(60)
                .setMaxVideoSize(500, 400).build()
        mediaPlayer.prepare()

    }

    //when we get out it releases memory
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.pause()
            mediaPlayer.clearMediaItems()
            mediaPlayer.release()
        }
    }


    AnimatedVisibility(
        isVisible == null, Modifier.zIndex(1F), enter = scaleIn(), exit = scaleOut()
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "pausa",
                modifier = Modifier
                    .zIndex(1F)
                    .size(200.dp)
            )
            Text(text = "Cambiando modo de video")

        }


    }
    if (!mediaPlayer.isLoading) {
        Box(modifier.tappable(onTap = {
            if (isVisible == true) mediaPlayer.playWhenReady = !mediaPlayer.playWhenReady
        }, onDoubleTap = {
            onDoubleTap()
        }, onLongPress = {
            onLongTap()
            mediaPlayer.playWhenReady = !mediaPlayer.playWhenReady
        })) {

            AnimatedVisibility(
                visible = isVisible == true,
                exit = fadeOut(animationSpec = tween(10)),
                enter = EnterTransition.None,
                modifier = Modifier.zIndex(0F)
            ) {

                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            useController = false
                            player = mediaPlayer
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH

                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .zIndex(0F)
                )
            }
            AnimatedVisibility(
                visible = isVisible == false || isVisible == null,
                exit = fadeOut(animationSpec = tween(1000)),
                enter = EnterTransition.None
            ) {
                mediaPlayer.pause()

                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(context, uri)
                val bitmap by remember {
                    mutableStateOf(retriever.getFrameAtIndex(1))
                }

                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "primer frame",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )

            }
        }
    } else {
        LinearProgressIndicator(
            modifier
                .fillMaxWidth()
                .height(500.dp)
                .background(Color.Black)
        )
    }
}


/**function that uses [ExoPlayer] to display a video given an Uri in string format
 * works for local files and urls*/
@OptIn(UnstableApi::class)
@Composable
fun DisplayVideoFromPost(
    source: UIPost,
    modifier: Modifier,
    onDoubleTap: () -> Unit = {},
    onLongTap: () -> Unit = {},
    isVisible: Boolean? = true,
    pauseAnimationState: Boolean?,
    onTap: () -> Unit
) {

    val context = LocalContext.current
    //main controller
    val mediaPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    //on launch set content and basic configuration
    LaunchedEffect(source) {

        mediaPlayer.setMediaItem(source.mediaItem)
        mediaPlayer.repeatMode = Player.REPEAT_MODE_ALL
        mediaPlayer.trackSelectionParameters =
            mediaPlayer.trackSelectionParameters.buildUpon().setMaxVideoFrameRate(60)
                .setMaxVideoSize(500, 600).build()
        mediaPlayer.prepare()

    }

    //get the 1st frame
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, source.UIURL)
    val bitmap by remember {
        mutableStateOf(retriever.getFrameAtIndex(1))
    }

    //when we get out it releases memory
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.pause()
            mediaPlayer.clearMediaItems()
            mediaPlayer.release()
        }
    }

    //we see this when animation state is null and visibility is not null
    AnimatedVisibility(
        pauseAnimationState == null && isVisible != null,
        Modifier.zIndex(1F),
        enter = scaleIn(),
        exit = scaleOut()
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Icon(
                imageVector = if (mediaPlayer.playWhenReady) Icons.Filled.PlayArrow else Icons.Outlined.PlayArrow,
                contentDescription = "pausa",
                modifier = Modifier
                    .zIndex(1F)
                    .size(200.dp)
            )

        }

    }

    //we see this when visibility is null, video mode is changing
    AnimatedVisibility(
        isVisible == null, Modifier.zIndex(1F), enter = scaleIn(), exit = scaleOut()
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "cambio de modo",
                modifier = Modifier
                    .zIndex(1F)
                    .size(150.dp)
            )
            Text(text = "Cambiando modo de video")

        }

    }

    val loading by remember { derivedStateOf { mediaPlayer.isLoading } }

    var isFullScreen by rememberSaveable {
        mutableStateOf(false)
    }

    if (!loading) {
        Box(
            modifier
                .animateContentSize(
                    spring(
                        Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow
                    )
                )
                .heightIn(0.dp, if (isFullScreen) Dp.Infinity else 350.dp)
                .tappable(onTap = {
                    //on tap, if it is visible, just play or pause
                    if (isVisible == true) {
                        mediaPlayer.playWhenReady = !mediaPlayer.playWhenReady
                        onTap()
                    }
                    //if it is not visible, we are in video mode, so we need to long tap to unlock video
                    else {
                        onLongTap()
                        onTap()
                        mediaPlayer.playWhenReady = true
                    }
                }, onDoubleTap = {
                    onDoubleTap()
                }, onLongPress = {
                    onLongTap()
                    mediaPlayer.playWhenReady = false
                })
                .zIndex(0f)
        ) {

            AnimatedVisibility(
                visible = isVisible == true,
                exit = fadeOut(animationSpec = tween(10)),
                enter = EnterTransition.None,
                modifier = Modifier.zIndex(0F)
            ) {
                LaunchedEffect(key1 = true) {
                    mediaPlayer.playWhenReady = true
                }

                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            useController = false
                            player = mediaPlayer
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH

                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .zIndex(0F)
                )
            }

            //when it is not visible, we show the first frame
            AnimatedVisibility(
                visible = isVisible == false || isVisible == null,
                exit = fadeOut(animationSpec = tween(1000)),
                enter = EnterTransition.None
            ) {
                LaunchedEffect(key1 = true) {
                    mediaPlayer.playWhenReady = false
                }
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "primer frame",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )

            }

            //swaps between full size and stated size
            Checkbox(
                checked = isFullScreen, onCheckedChange = {
                    isFullScreen = !isFullScreen
                }, modifier = Modifier
                    .zIndex(1f)
                    .align(Alignment.TopStart)
            )
        }
    } else {
        //if still loading show a progress bar
        LinearProgressIndicator(
            modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.Black)
        )
    }
}
