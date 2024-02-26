package com.example.petstagram.categorias

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.petstagram.UiData.Category
import com.example.petstagram.ViewModels.PrincipalViewModel
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
fun Categorias(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    categoryViewModel: PrincipalViewModel
) {
    LaunchedEffect(Unit){
        categoryViewModel.fetchCategories()
    }
    BoxWithConstraints {
        val anchomax = maxWidth
        val categories by categoryViewModel.categories.collectAsState()
        TopLevel(modifier = modifier) {
            for (i in categories){
                CategoriaInstance(Modifier.width(anchomax),
                    navController = navController,
                    category = i,
                    viewModel = categoryViewModel)
            }
        }

    }
}

@Composable
fun CategoriaInstance(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    category: Category,
    viewModel: PrincipalViewModel
) {
    Categoria(modifier = modifier, navController = navController, category = category, viewModel = viewModel)
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
            .wrapContentHeight()
    )
}
