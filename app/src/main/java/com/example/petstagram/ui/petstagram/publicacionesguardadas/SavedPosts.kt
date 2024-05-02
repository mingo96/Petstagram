package com.example.petstagram.ui.petstagram.publicacionesguardadas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.petstagram.UiData.Category
import com.example.petstagram.ViewModels.PostsViewModel
import com.example.petstagram.ViewModels.SavedPostsViewModel
import com.example.petstagram.barrasuperior.AtrasAtras
import com.example.petstagram.barrasuperior.TopBar
import com.example.petstagram.barrasuperior.Variant
import com.example.petstagram.cuadrotexto.Label
import com.example.petstagram.cuadrotexto.Variation
import com.example.petstagram.publicaciones.Posts
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope


/**UI screen to display a [Category] posts*/
@Composable
fun SavedPosts(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: SavedPostsViewModel
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

    val categories by viewModel.categories.collectAsState()

    BoxWithConstraints {
        val height = maxHeight
        TopLevel(modifier = modifier) {
            TopBarInstance(
                modifier = Modifier
                    .rowWeight(1.0f)
                    .height(height.times(0.2225f)),
                navController = navController
            )


            Row(Modifier.height(height.times(0.07f))) {

                LazyRow (horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(end = 8.dp))
                {
                    items(categories){i->
                        CategoryDisplay(category = i, click = {viewModel.selectCategory(i)})
                    }
                }
                AtrasAtras(
                    Modifier
                        .fillMaxHeight()
                        .clickable { navController.navigateUp() })
            }

            if (viewModel.statedCategory!= null) {
                CategoryText(
                    modifier.requiredHeight(height.times(0.05f)),
                    added = viewModel.statedCategory!!.name
                )
            }
            AnimatedVisibility(visible = isLoading!!,
                enter = slideInVertically { it },
                exit = slideOutVertically { 1 }) {

                Dialog(onDismissRequest = {  }) {
                    CircularProgressIndicator(
                        modifier
                            .rowWeight(1.0f)
                            .height(height.times(0.825f))
                            .fillMaxWidth(0.8f))
                }
            }

            PostsInstance(
                modifier = Modifier
                    .rowWeight(1.0f)
                    .height(height.times(0.835f)),
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun CategoryDisplay(category: Category, click : ()->Unit){
    Column(modifier = Modifier
        .fillMaxHeight()
        .width(168.dp)
        .background(Color.Black)
        .clickable { click.invoke() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)) {
        RelayContainer (
            modifier = Modifier.fillMaxHeight(0.7f).padding(top = 4.dp),
            radius = 6.0){
            SubcomposeAsyncImage(
                model = category.categoryImage,
                loading = {
                    CircularProgressIndicator()
                },
                contentDescription = category.name,
                contentScale = ContentScale.Crop
            )
        }

        Text(text = "Mostrar ${category.name}", style = TextStyle(fontSize = 10.sp))
    }
}

/**pass-by function to display [TopBar]*/
@Composable
fun TopBarInstance(modifier: Modifier = Modifier, navController: NavHostController) {
    TopBar(
        modifier = modifier.fillMaxWidth(1.0f),
        variant = Variant.Simple,
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
fun PostsInstance(modifier: Modifier = Modifier, viewModel: SavedPostsViewModel) {
    Posts(
        modifier = modifier
            .fillMaxWidth(1.0f),
        controller = viewModel
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
        itemSpacing = 8.0,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
