package com.example.petstagram.menuprincipal

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.petstagram.ViewModels.CategoriesViewModel
import com.example.petstagram.ui.petstagram.barradeselecciondetipodepublicacion.BarraDeSeleccionDeTipoDePublicacion
import com.example.petstagram.barrasuperior.TopBar
import com.example.petstagram.barrasuperior.Variant
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
                .height(height.times(0.238f)),
                navController = navController)
            CategoriesInstance(modifier = Modifier
                .rowWeight(1.0f)
                .height(height.times(0.927f)),
                navController = navController,
                categoryViewModel = viewModel)
        }
    }

}

/**top bar with which you can move in the app*/
@Composable
fun TopBarInstance(modifier: Modifier = Modifier, navController: NavHostController) {
    TopBar(modifier = modifier.fillMaxWidth(1.0f),navController = navController, variant = Variant.WithMenu)
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