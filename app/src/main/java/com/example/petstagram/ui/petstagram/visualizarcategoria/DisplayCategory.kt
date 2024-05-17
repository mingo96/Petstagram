package com.example.petstagram.visualizarcategoria

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.petstagram.UiData.Category
import com.example.petstagram.ViewModels.PostsViewModel
import com.example.petstagram.barrasuperior.TopBar
import com.example.petstagram.barrasuperior.Variant
import com.example.petstagram.cuadrotexto.Label
import com.example.petstagram.cuadrotexto.Variation
import com.example.petstagram.publicaciones.Posts
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.ScrollAnchor

/**UI screen to display a [Category] posts*/
@Composable
fun DisplayCategory(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: PostsViewModel
) {
    //starts loading when entering
    LaunchedEffect(viewModel){
        viewModel.startLoadingPosts()
    }

    //stops loading when ending activity
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopLoading()
        }
    }

    /**observable to let the ui if [viewModel] is still loading data*/
    val isLoading by viewModel.isLoading.observeAsState()

    BoxWithConstraints {
        val height = maxHeight
        TopLevel(modifier = modifier) {
            TopBarInstance(
                modifier = Modifier
                    .rowWeight(1.0f)
                    .height(height.times(0.24f)),
                navController = navController
            )

            CategoryText(modifier.requiredHeight(height.times(0.05f)), added = viewModel.statedCategory.name)

            AnimatedVisibility(visible = isLoading!!,
                enter = slideInVertically { it },
                exit = slideOutVertically { 1 }) {

                Dialog(onDismissRequest = {  }) {
                    val dots by viewModel.funnyAhhString.collectAsState()

                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                        CircularProgressIndicator(
                            modifier
                                .height(height.times(0.6f))
                                .fillMaxWidth(0.8f))
                        Text(text = "Cargando, por favor, espere$dots", style = TextStyle(color = Color.White))
                    }
                }
            }

            PostsInstance(
                modifier = Modifier
                    .rowWeight(1.0f)
                    .height(height.times(0.81f)),
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}

/**pass-by function to display [TopBar]*/
@Composable
fun TopBarInstance(modifier: Modifier = Modifier, navController: NavHostController) {
    TopBar(
        modifier = modifier.fillMaxWidth(1.0f),
        variant = Variant.WithMenu,
        navController = navController
    )
}

/**pass-by function to display the text of the category
 * @param added represents the name of the category*/
@Composable
fun CategoryText(modifier: Modifier = Modifier, added: String) {
    Label(
        variation = Variation.CategoryPosts,
        modifier = modifier,
        added = added
    )
}

/**pass-by function to display [Posts]*/
@Composable
fun PostsInstance(modifier: Modifier = Modifier, viewModel: PostsViewModel, navController: NavHostController) {
    Posts(
        modifier = modifier
            .fillMaxWidth(1.0f),
        controller = viewModel,
        navController = navController
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
        scrollAnchor = ScrollAnchor.End,
        scrollable = true,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
