package com.example.petstagram.categoria

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.petstagram.UiData.Category
import com.example.petstagram.ViewModels.CategoriesViewModel
import com.google.relay.compose.CrossAxisAlignment
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText
import java.util.Locale

/**Ui representation of [Category]
 */
@Composable
fun Category(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    category: Category,
    viewModel: CategoriesViewModel
) {
    TopLevel(modifier = modifier) {
        ImageContainer(modifier = Modifier.rowWeight(1.0f)) {
            SubcomposeAsyncImage(
                model = category.categoryImage,
                loading = {
                    CircularProgressIndicator()
                },
                contentDescription = category.name,
                contentScale = ContentScale.Crop
            )
        }
        Buttons(modifier = Modifier.rowWeight(1.0f)) {
            PostsAccessButton(modifier = Modifier
                .rowWeight(1.0f)
                .columnWeight(1.0f)
                .clickable {
                    viewModel.selectedCategory = category
                    navController.navigate("publicaciones")
                }) {
                CategoryText(texto = category.name)
            }
            AddButton(modifier = Modifier
                .columnWeight(1.0f)
                .clickable {
                    viewModel.selectedCategory = category
                    navController.navigate("publicar")
                }) {
                AddText(modifier = Modifier
                    .rowWeight(1.0f)
                    .columnWeight(1.0f))
            }
        }
    }
}

@Composable
fun ListedCategory(modifier: Modifier, category: Category, onSelected : ()->Unit = {}){

    TopLevel(modifier.clickable { onSelected() }) {
        ImageContainer(modifier = Modifier.rowWeight(1.0f)) {
            SubcomposeAsyncImage(
                model = category.categoryImage,
                loading = {
                    CircularProgressIndicator()
                },
                contentDescription = category.name,
                contentScale = ContentScale.Crop
            )
        }
        Row (modifier = Modifier
            .fillMaxWidth()
            .requiredWidth(40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){
            CategoryName(texto = category.name)
        }
    }

}


@Composable
fun CategoryName(modifier: Modifier = Modifier, texto: String) {
    RelayText(
        content = texto.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
        fontSize = 20.0.sp,
        fontFamily = inter,
        color = Color.White,
        height = 1.2102272033691406.em,
        fontWeight = FontWeight(800.0.toInt()),
        overflow = TextOverflow.Ellipsis,
        maxLines = -1,
        modifier = modifier
            .requiredWidth(232.0.dp)
            .wrapContentHeight(
                align = Alignment.CenterVertically,
                unbounded = true
            )
    )
}


@Composable
fun ImageContainer(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 217,
            green = 217,
            blue = 217
        ),
        isStructured = false,
        radius = 5.0,
        strokeWidth = 2.0,
        strokeColor = Color(
            alpha = 255,
            red = 0,
            green = 0,
            blue = 0
        ),
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .requiredHeight(176.0.dp)
    )
}

@Composable
fun CategoryText(modifier: Modifier = Modifier, texto: String) {
    RelayText(
        content = "Publicaciones de $texto",
        fontSize = 15.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 35,
            green = 35,
            blue = 35
        ),
        height = 1.2102272033691406.em,
        fontWeight = FontWeight(800.0.toInt()),
        overflow = TextOverflow.Ellipsis,
        maxLines = -1,
        modifier = modifier
            .requiredWidth(232.0.dp)
            .wrapContentHeight(
                align = Alignment.CenterVertically,
                unbounded = true
            )
    )
}

@Composable
fun PostsAccessButton(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 224,
            green = 164,
            blue = 0
        ),
        padding = PaddingValues(all = 8.0.dp),
        itemSpacing = 8.0,
        radius = 15.0,
        strokeWidth = 1.0,
        strokeColor = Color(
            alpha = 38,
            red = 0,
            green = 0,
            blue = 0
        ),
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}

@Composable
fun AddText(modifier: Modifier = Modifier) {
    RelayText(
        content = "+",
        fontSize = 40.0.sp,
        fontFamily = inter,
        height = 1.2102272033691406.em,
        fontWeight = FontWeight(500.0.toInt()),
        maxLines = -1,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
            .wrapContentHeight(
                align = Alignment.Bottom,
                unbounded = true
            )
    )
}

@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 224,
            green = 164,
            blue = 0
        ),
        arrangement = RelayContainerArrangement.Row,
        padding = PaddingValues(
            start = 8.0.dp,
            top = 0.0.dp,
            end = 8.0.dp,
            bottom = 0.0.dp
        ),
        itemSpacing = 8.0,
        radius = 15.0,
        strokeWidth = 1.0,
        strokeColor = Color(
            alpha = 38,
            red = 0,
            green = 0,
            blue = 0
        ),
        content = content,
        modifier = modifier
            .requiredWidth(64.0.dp)
            .fillMaxHeight(1.0f)
    )
}

@Composable
fun Buttons(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        mainAxisAlignment = MainAxisAlignment.Start,
        crossAxisAlignment = CrossAxisAlignment.Start,
        arrangement = RelayContainerArrangement.Row,
        itemSpacing = 16.0,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .requiredHeight(40.0.dp)
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
            red = 0,
            green = 0,
            blue = 0
        ),
        arrangement = RelayContainerArrangement.Column,
        padding = PaddingValues(all = 16.0.dp),
        itemSpacing = 8.0,
        strokeWidth = 8.0,
        strokeColor = Color(
            alpha = 255,
            red = 35,
            green = 35,
            blue = 35
        ),
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .wrapContentHeight()
    )
}
