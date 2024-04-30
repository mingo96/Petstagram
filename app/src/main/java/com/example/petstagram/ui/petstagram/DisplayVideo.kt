package com.example.petstagram.ui.petstagram

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.LoadControl
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView


/**function that uses [ExoPlayer] to display a video given an Uri in string format
 * works for local files and urls*/
@OptIn(UnstableApi::class) @Composable
fun DisplayVideo(source : MediaItem, modifier:Modifier) {

    val context = LocalContext.current
    //main controller
    var mediaPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    //on launch set content and basic configuration
    LaunchedEffect(source) {
        mediaPlayer.setMediaItem(source)
        mediaPlayer.repeatMode = Player.REPEAT_MODE_ALL
        mediaPlayer.trackSelectionParameters = mediaPlayer.trackSelectionParameters.buildUpon().setMaxVideoFrameRate(60).setMaxVideoSize(500, 500).build()
        mediaPlayer.prepare()
    }

    //when we get out it releases memory
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.pause()
            mediaPlayer.clearMediaItems()
        }
    }

    AnimatedVisibility(visible = !mediaPlayer.isLoading, enter = expandVertically { it }, exit = shrinkVertically { it }) {
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
                .background(Color.Gray)
        )
    }
    //the video itself, they repeat for ever, start stopped, on click you swap between
    //stopped / playing, basic controller was just too ugly

    AnimatedVisibility(visible = mediaPlayer.isLoading, enter = expandVertically { it }, exit = shrinkVertically { it }) {
        CircularProgressIndicator(
            modifier
                .fillMaxWidth()
                .height(500.dp)
                .background(Color.Black))
    }
}
