package com.example.petstagram.menuprincipal

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.petstagram.ViewModels.CategoriesViewModel
import com.example.petstagram.ui.petstagram.barradeselecciondetipodepublicacion.BarraDeSeleccionDeTipoDePublicacion
import com.example.petstagram.barrasuperior.BarraSuperior
import com.example.petstagram.categorias.Categorias
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.ScrollAnchor

/**
 * menu principal
 *
 * This composable was generated from the UI Package 'menu_principal'.
 * Generated code; do not edit directly
 */
@Composable
fun CategoriesMenu(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: CategoriesViewModel
) {
    LaunchedEffect(key1 = viewModel){

        viewModel.fetchCategories()
    }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val height = maxHeight
        TopLevel(modifier = modifier) {
            BarraSuperiorInstance(modifier = Modifier
                .rowWeight(1.0f)
                .requiredHeight(height.times(0.22f)), navController = navController)
            BarraTipoNotificacion(modifier = Modifier
                .rowWeight(1.0f)
                .requiredHeight(height.times(0.07f)),navController = navController)
            CategoriasInstance(modifier = Modifier
                .rowWeight(1.0f)
                .requiredHeight(height.times(0.84f)),
                navController = navController,
                categoryViewModel = viewModel)
        }
    }

}


@Composable
fun BarraSuperiorInstance(modifier: Modifier = Modifier, navController: NavHostController) {
    BarraSuperior(modifier = modifier.fillMaxWidth(1.0f),navController = navController)
}

@Composable
fun BarraTipoNotificacion(modifier: Modifier = Modifier, navController: NavHostController) {
    BarraDeSeleccionDeTipoDePublicacion(modifier = modifier.fillMaxWidth(1.0f),navController = navController)
}

@Composable
fun CategoriasInstance(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    categoryViewModel: CategoriesViewModel
) {

    Categorias(modifier = modifier,
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
        mainAxisAlignment = MainAxisAlignment.Center,
        scrollAnchor = ScrollAnchor.Start,
        scrollable = true,
        itemSpacing = 8.0,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
