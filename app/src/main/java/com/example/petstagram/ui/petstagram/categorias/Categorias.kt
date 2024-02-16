package com.example.petstagram.categorias

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.petstagram.categoria.Categoria
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope

/**
 * Categorias
 *
 * This composable was generated from the UI Package 'categorias'.
 * Generated code; do not edit directly
 */
@Composable
fun Categorias(modifier: Modifier = Modifier) {
    BoxWithConstraints {
        val anchomax = maxWidth
        TopLevel(modifier = modifier) {
            CategoriaInstance(modifier.width(anchomax))
            CategoriaInstance(modifier.width(anchomax))
            CategoriaInstance(modifier.width(anchomax))
            CategoriaInstance(modifier.width(anchomax))
        }

    }
}

@Preview(widthDp = 360, heightDp = 688)
@Composable
private fun CategoriasPreview() {
    MaterialTheme {
        RelayContainer {
            Categorias(modifier = Modifier
                .rowWeight(1.0f)
                .columnWeight(1.0f))
        }
    }
}

@Composable
fun CategoriaInstance(modifier: Modifier = Modifier) {
    Categoria(modifier = modifier)
}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 225,
            green = 196,
            blue = 1
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
