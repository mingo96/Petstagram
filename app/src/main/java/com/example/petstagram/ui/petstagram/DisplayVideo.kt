package com.example.petstagram.ui.petstagram

import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class) @Composable
fun DisplayVideo(source : String, modifier:Modifier) {
    val context = LocalContext.current
    val mediaPlayer = remember {
        ExoPlayer.Builder(context).build()
    }
    val media = MediaItem.fromUri(source)

    LaunchedEffect(media) {
        mediaPlayer.setMediaItem(media)
        mediaPlayer.repeatMode = Player.REPEAT_MODE_ALL
        mediaPlayer.prepare()

    }
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = mediaPlayer
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                useController = false
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable {
                mediaPlayer.playWhenReady = !mediaPlayer.playWhenReady
            }
    )
}
