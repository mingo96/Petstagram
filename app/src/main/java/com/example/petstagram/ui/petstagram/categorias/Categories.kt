package com.example.petstagram.categorias

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.petstagram.UiData.Category
import com.example.petstagram.ViewModels.CategoriesViewModel
import com.example.petstagram.categoria.Category
import com.example.petstagram.categoria.ListedCategory
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
fun Categories(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    categoryViewModel: CategoriesViewModel
) {
    LaunchedEffect(Unit){
        categoryViewModel.fetchCategories()
    }
    BoxWithConstraints {
        val width = maxWidth
        val categories by categoryViewModel.categories.collectAsStateWithLifecycle()
        LazyColumn(
            modifier = modifier
                .background(
                    Color(
                        alpha = 255,
                        red = 225,
                        green = 196,
                        blue = 1
                    )
                )
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) {category->

                CategoryInstance(
                    Modifier.width(width),
                    navController = navController,
                    category = category,
                    viewModel = categoryViewModel)
            }
        }

    }
}

@Composable
fun CategoryList(modifier : Modifier,onSelect : (Category)->Unit, categoryList: List<Category>){
    BoxWithConstraints(modifier = modifier) {
        val width = maxWidth

        LazyColumn(modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(
                Color(
                    alpha = 255,
                    red = 225,
                    green = 196,
                    blue = 1
                )
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)){
            items(categoryList){category->
                ListedCategory(modifier = Modifier.width(width), category = category, onSelected = { onSelect(category) })
            }
        }

    }
}

/**pass-by function to display [Category]*/
@Composable
fun CategoryInstance(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    category: Category,
    viewModel: CategoriesViewModel
) {
    Category(modifier = modifier, navController = navController, category = category, viewModel = viewModel)
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
