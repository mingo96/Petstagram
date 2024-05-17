package com.example.petstagram.menuprincipal

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.petstagram.ViewModels.CategoriesViewModel
import com.example.petstagram.ui.petstagram.barradeselecciondetipodepublicacion.BarraDeSeleccionDeTipoDePublicacion
import com.example.petstagram.barrasuperior.TopBar
import com.example.petstagram.categorias.Categories
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.ScrollAnchor

/**UI representacion of BaseMenu, interactuates with [viewModel] to get the data*/
@Composable
fun CategoriesMenu(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: CategoriesViewModel
) {
    //on start, start loading categories
    LaunchedEffect(key1 = viewModel){
        viewModel.fetchCategories()
    }

    //stop loading when closed
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopLoading()
        }
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val height = maxHeight
        TopLevel(modifier = modifier) {
            TopBarInstance(modifier = Modifier
                .rowWeight(1.0f)
                .requiredHeight(height.times(0.24f)), navController = navController)
            BarraTipoNotificacion(modifier = Modifier
                .rowWeight(1.0f)
                .requiredHeight(height.times(0.07f)),navController = navController)
            CategoriesInstance(modifier = Modifier
                .rowWeight(1.0f)
                .requiredHeight(height.times(0.83f)),
                navController = navController,
                categoryViewModel = viewModel)
        }
    }

}

/**top bar with which you can move in the app*/
@Composable
fun TopBarInstance(modifier: Modifier = Modifier, navController: NavHostController) {
    TopBar(modifier = modifier.fillMaxWidth(1.0f),navController = navController)
}

/**esthetic bar (for now)*/
@Composable
fun BarraTipoNotificacion(modifier: Modifier = Modifier, navController: NavHostController) {
    BarraDeSeleccionDeTipoDePublicacion(modifier = modifier.fillMaxWidth(1.0f),navController = navController)
}

/**instantiates categories from [categoryViewModel]*/
@Composable
fun CategoriesInstance(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    categoryViewModel: CategoriesViewModel
) {

    Categories(modifier = modifier.fillMaxWidth(),
        navController = navController,
        categoryViewModel= categoryViewModel)

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
        scrollAnchor = ScrollAnchor.End,
        scrollable = true,
        itemSpacing = 8.0,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
