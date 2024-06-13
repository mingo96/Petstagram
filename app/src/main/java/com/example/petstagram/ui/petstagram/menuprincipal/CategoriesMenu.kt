package com.example.petstagram.menuprincipal

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.petstagram.ViewModels.CategoriesViewModel
import com.example.petstagram.barrasuperior.AtrasAtras
import com.example.petstagram.barrasuperior.TopBar
import com.example.petstagram.barrasuperior.Variant
import com.example.petstagram.categorias.Categories
import com.example.petstagram.perfilpropio.StateSelector
import com.example.petstagram.ui.petstagram.barradeselecciondetipodepublicacion.BarraDeSeleccionDeTipoDePublicacion
import com.example.petstagram.ui.petstagram.perfiles.ProfileSearch
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.ScrollAnchor
import kotlinx.coroutines.launch

/**UI representacion of BaseMenu, interactuates with [viewModel] to get the data*/
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CategoriesMenu(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: CategoriesViewModel,
    onExit: () -> Unit
) {
    //stop loading when closed
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopLoading()
        }
    }

    val scroll = rememberLazyListState()

    val scope = rememberCoroutineScope()
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val height = maxHeight
        val width = maxWidth
        TopLevel(
            modifier = modifier,
        ) {

            TopBarInstance(
                modifier = Modifier.height(180.dp),
                navController = navController,
                onExit = onExit
            )

            val state by remember {
                derivedStateOf { scroll.firstVisibleItemIndex == 0 }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(top = 8.dp)
            ) {

                StateSelector(
                    Modifier.height(56.dp).width(width-56.dp), state = state, onClick = {
                        scope.launch {
                            scroll.animateScrollToItem(it)
                        }
                    }, text1 = "Categorías", text2 = "Perfiles"
                )

                AtrasAtras(
                    Modifier
                        .height(56.dp)
                        .clickable {
                            onExit()

                        })

            }

            val flingBehavior = rememberSnapFlingBehavior(lazyListState = scroll)
            LazyRow(
                state = scroll,
                flingBehavior = flingBehavior,
                modifier = Modifier
                    .width(width * 2)
                    .height(height - 60.dp - 56.dp-8.dp)
            ) {
                item {
                    CategoriesInstance(
                        modifier = Modifier.fillMaxHeight()
                            .width(width),
                        navController = navController,
                        categoryViewModel = viewModel
                    )
                }
                item {
                    ProfileSearch(
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxHeight()
                            .width(width),
                        navController = navController
                    )
                }

            }


        }
    }

}

/**top bar with which you can move in the app*/
@Composable
fun TopBarInstance(
    modifier: Modifier = Modifier, navController: NavHostController, onExit: () -> Unit
) {
    TopBar(
        modifier = modifier.fillMaxWidth(1.0f),
        navController = navController,
        variant = Variant.Simple,
        onExit = onExit
    )
}

/**instantiates categories from [categoryViewModel]*/
@Composable
fun CategoriesInstance(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    categoryViewModel: CategoriesViewModel
) {

    Categories(
        modifier = modifier.fillMaxWidth(),
        navController = navController,
        categoryViewModel = categoryViewModel
    )

}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier, content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color.Transparent,
        scrollAnchor = ScrollAnchor.End,
        scrollable = true,
        content = content,
        mainAxisAlignment = MainAxisAlignment.Center,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}