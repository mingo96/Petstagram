package com.example.petstagram.categorias

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.petstagram.UiData.Category
import com.example.petstagram.ViewModels.CategoriesViewModel
import com.example.petstagram.categoria.Category
import com.example.petstagram.categoria.ListedCategory
import com.example.petstagram.ui.theme.Secondary
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
    val categories by categoryViewModel.categories.collectAsState()
    LazyColumn(
        modifier = modifier
            .background(
                Secondary
            )
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
    ) {
        items(categories) { category ->

            CategoryInstance(
                Modifier.fillMaxWidth(),
                navController = navController,
                category = category,
                viewModel = categoryViewModel
            )
        }
    }


}

@Composable
fun CategoryList(
    modifier: Modifier,
    onSelect: (Category) -> Unit,
    categoryList: List<Category>,
    selected: Category?
) {
    BoxWithConstraints(modifier = modifier) {
        val width = maxWidth

        LazyColumn(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .background(
                    Secondary
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categoryList) { category ->

                ListedCategory(
                    modifier = Modifier.width(width),
                    category = category,
                    onSelected = { onSelect(category) },
                    selected = (selected != null && selected.name == category.name)
                )
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
    Category(
        modifier = modifier,
        navController = navController,
        category = category,
        viewModel = viewModel
    )
}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Secondary,
        mainAxisAlignment = MainAxisAlignment.Start,
        scrollable = true,
        itemSpacing = 8.0,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .wrapContentHeight()

    )
}
