package com.example.petstagram.publicacion

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.petstagram.UiData.Post
import com.example.petstagram.cuadroinfo.CuadroInfo
import com.example.petstagram.cuadroinfo.Variacion
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope

/**
 * publicacion
 *
 * This composable was generated from the UI Package 'publicacion'.
 * Generated code; do not edit directly
 */
@Composable
fun Publicacion(modifier: Modifier = Modifier, post: Post, url: String) {
    TopLevel(modifier = modifier) {
        CuadroInfoInstance(modifier = modifier.rowWeight(1.0f), post = post)
        PostSource(modifier = modifier.rowWeight(1.0f), post = post, url = url)
        BotonesPublicacion(modifier = modifier.rowWeight(1.0f), post = post)
    }
}

@Composable
fun CuadroInfoInstance(modifier: Modifier = Modifier, post: Post) {
    CuadroInfo(
        variacion = Variacion.Superior,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .requiredHeight(48.0.dp),
        added = post
    )
}

@OptIn(UnstableApi::class) @Composable
fun PostSource(modifier: Modifier = Modifier, post: Post, url: String) {
    if (post.typeOfMedia == "image") {
        AsyncImage(
            modifier = modifier.fillMaxWidth(),
            model = url,
            contentDescription = post.title,
            contentScale = ContentScale.Crop
        )
    }else{
        val mediaPlayer = ExoPlayer.Builder(LocalContext.current).build()
        val media = MediaItem.fromUri(url)
        LaunchedEffect(media) {
            mediaPlayer.setMediaItem(media)
            mediaPlayer.pause()
            //mediaPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING

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
                }
            },
            modifier = modifier
                .fillMaxWidth().fillMaxHeight()
        )
    }
}

@Composable
fun BotonesPublicacion(modifier: Modifier = Modifier, post: Post) {
    CuadroInfo(
        modifier = modifier
            .fillMaxWidth(1.0f)
            .requiredHeight(48.0.dp),
        variacion = Variacion.Inferior,
        added = post
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
