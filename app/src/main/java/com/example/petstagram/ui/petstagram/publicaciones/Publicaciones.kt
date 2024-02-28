package com.example.petstagram.publicaciones

import android.util.Log
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.petstagram.UiData.Post
import com.example.petstagram.publicacion.Publicacion
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * publicaciones
 *
 * This composable was generated from the UI Package 'publicaciones'.
 * Generated code; do not edit directly
 */
@Composable
fun Publicaciones(modifier: Modifier = Modifier, posts : StateFlow<List<Pair<String, Post>>>) {
    BoxWithConstraints {
        val postsState by posts.collectAsState()
        val anchoMax = maxWidth
        TopLevel(modifier = modifier.width(maxWidth)) {
            for (i in postsState){
                Log.i("asdioogyafgga", i.second.id)
                PublicacionInstance(modifier = Modifier.width(anchoMax), post = i.second, url = i.first)
            }
        }
    }
}

@Composable
fun PublicacionInstance(
    modifier: Modifier = Modifier,
    post: Post,
    url: String
) {
    Publicacion(modifier = modifier
        .fillMaxWidth(1.0f),
        post = post,
        url = url)
}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 224,
            green = 164,
            blue = 0
        ),
        mainAxisAlignment = MainAxisAlignment.Start,
        scrollable = true,
        itemSpacing = 8.0,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
