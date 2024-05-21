package com.example.petstagram.ui.petstagram

import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView


/**function that uses [ExoPlayer] to display a video given an Uri in string format
 * works for local files and urls*/
@kotlin.OptIn(ExperimentalFoundationApi::class)
@OptIn(UnstableApi::class)
@Composable
fun DisplayVideo(source: ExoPlayer, modifier: Modifier, onLike: () -> Unit = {}) {

    //when we get out it releases memory
    DisposableEffect(Unit) {
        onDispose {
            source.pause()
        }
    }
    val context = LocalContext.current
    val coso = remember {
        PlayerView(context).apply {
            player = source
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            useController = false
            isClickable = false
        }
    }

    AnimatedVisibility(
        visible = !source.isLoading,
        enter = expandVertically { it },
        exit = shrinkVertically { it }) {
        AndroidView(
            factory = {
                coso
            },
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .combinedClickable(
                    enabled = true,
                    onClick = {
                        source.playWhenReady = !source.playWhenReady
                    },
                    onDoubleClick = {
                        onLike.invoke()
                    }
                )
                .background(Color.Gray)
        )
    }

    AnimatedVisibility(
        visible = source.isLoading,
        enter = expandVertically { it },
        exit = shrinkVertically { it }) {
        CircularProgressIndicator(
            modifier
                .fillMaxWidth()
                .height(500.dp)
                .background(Color.Black)
        )
    }
}


/**function that uses [ExoPlayer] to display a video given an Uri in string format
 * works for local files and urls*/
@kotlin.OptIn(ExperimentalFoundationApi::class)
@OptIn(UnstableApi::class)
@Composable
fun DisplayVideoFromSource(
    source: MediaItem,
    modifier: Modifier,
    onDoubleTap: () -> Unit = {},
    onTap: () -> Unit = {},
    isVisible: Boolean = true,
    uri: Uri = Uri.EMPTY
) {

    val context = LocalContext.current
    //main controller
    val mediaPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
    }

    val loading by remember {
        derivedStateOf { mediaPlayer.isLoading }
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

    if (!loading) {
        Box(modifier) {

            AnimatedVisibility(
                visible = isVisible,
                exit = fadeOut(animationSpec = tween(1000)),
                enter = EnterTransition.None
            ) {
                mediaPlayer.playWhenReady = true

                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            useController = false
                            player = mediaPlayer
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            enabled = true,
                            onClick = {
                                onTap()
                                mediaPlayer.playWhenReady = !mediaPlayer.playWhenReady
                            },
                            onDoubleClick = {
                                onDoubleTap.invoke()
                            }
                        )
                        .background(Color.Gray)
                )
            }
            AnimatedVisibility(
                visible = !isVisible,
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
                    modifier = Modifier.fillMaxWidth().clickable {
                        onTap()
                    }
                )

            }
        }
    } else {
        CircularProgressIndicator(
            modifier
                .fillMaxWidth()
                .height(500.dp)
                .background(Color.Black)
        )
    }
}
