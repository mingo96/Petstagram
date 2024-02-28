package com.example.petstagram.visualizarcategoria

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.petstagram.ViewModels.PostsViewModel
import com.example.petstagram.barrasuperior.BarraSuperior
import com.example.petstagram.barrasuperior.Variante
import com.example.petstagram.cuadrotexto.Label
import com.example.petstagram.cuadrotexto.Variation
import com.example.petstagram.publicaciones.Publicaciones
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope

/**
 * Pantalla de categoria
 *
 * This composable was generated from the UI Package 'visualizar_categoria'.
 * Generated code; do not edit directly
 */
@Composable
fun DisplayCategory(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: PostsViewModel
) {
    LaunchedEffect(viewModel){
        viewModel.startLoadingPosts()
    }

    val isLoading by viewModel.isLoading.observeAsState()

    BoxWithConstraints {
        val height = maxHeight
        TopLevel(modifier = modifier) {
            BarraSuperiorInstance(
                modifier = Modifier
                    .rowWeight(1.0f)
                    .height(height.times(0.2225f)),
                navController = navController
            )

            CategoryText(modifier.requiredHeight(height.times(0.05f)), added = viewModel.statedCategory.name)

            if (isLoading!!)
                CircularProgressIndicator(
                    modifier
                        .rowWeight(1.0f)
                        .height(height.times(0.825f))
                        .fillMaxWidth(0.8f))

            else
                PublicacionesInstance(
                    modifier = Modifier
                        .rowWeight(1.0f)
                        .height(height.times(0.825f)),
                    viewModel = viewModel
                )
        }
    }
}


@Composable
fun BarraSuperiorInstance(modifier: Modifier = Modifier, navController: NavHostController) {
    BarraSuperior(
        modifier = modifier.fillMaxWidth(1.0f),
        variante = Variante.ConMenu,
        navController = navController
    )
}

@Composable
fun CategoryText(modifier: Modifier = Modifier, added: String) {
    Label(
        variation = Variation.CategoryPosts,
        modifier = modifier,
        added = added
    )
}

@Composable
fun PublicacionesInstance(modifier: Modifier = Modifier, viewModel: PostsViewModel) {
    Publicaciones(
        modifier = modifier
            .fillMaxWidth(1.0f),
        posts = viewModel.posts
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
            red = 35,
            green = 35,
            blue = 35
        ),
        mainAxisAlignment = MainAxisAlignment.End,
        scrollable = true,
        itemSpacing = 24.0,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
