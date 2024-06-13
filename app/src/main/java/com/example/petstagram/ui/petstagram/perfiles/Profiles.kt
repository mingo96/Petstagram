package com.example.petstagram.ui.petstagram.perfiles

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavController
import com.example.petstagram.ViewModels.CategoriesViewModel
import com.example.petstagram.ViewModels.ProfileObserverViewModel
import com.example.petstagram.cuadroinfo.FotoPerfilSizePeque
import com.example.petstagram.loginenmovil.myTextFieldColors
import com.example.petstagram.ui.theme.Primary
import com.example.petstagram.ui.theme.Secondary

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileSearch(
    modifier: Modifier = Modifier, viewModel: CategoriesViewModel, navController: NavController
) {

    val text by viewModel.searchText.observeAsState("")

    val profiles by viewModel.profiles.observeAsState(emptyList())

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(readText = { text },
            writeText = { viewModel.setSearchText(it) },
            search = { viewModel.search() })

        LazyColumn(
            modifier = Modifier
                .background(Color.Black)
                .animateContentSize(
                    tween(500)
                )
        ) {
            items(profiles, key = { it.id }) { it ->
                var followers by remember { mutableStateOf(it.followers) }
                Column(
                    modifier = Modifier.animateItemPlacement(
                        spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow,
                            visibilityThreshold = IntOffset.VisibilityThreshold.times(2f)
                        )
                    )
                ) {
                    SearchedProfile(text = it.userName,
                        pic = it.profilePic,
                        following = viewModel.userid in followers,
                        onClick = {
                            if (it.id == viewModel.userid) {
                                navController.navigate("perfilPropio")
                            } else {
                                ProfileObserverViewModel.staticProfile.id = it.id
                                navController.navigate("perfilAjeno")
                            }
                        }) {
                        viewModel.follow(it)
                        followers = it.followers
                    }
                    Divider(color = Color.White, thickness = 1.dp)
                }
            }
            if (profiles.isEmpty()){
                item {
                    if (text.isNotBlank()) {

                        Text(text = "No hay resultados")
                    }else{
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            color = Primary
                        )
                        Text(text = "Cargando")
                    }
                }
            }
        }

    }

}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    readText: () -> String,
    writeText: (String) -> Unit,
    search: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        TextField(value = readText(),
            onValueChange = writeText,
            modifier = Modifier.fillMaxWidth(),
            colors = myTextFieldColors(),
            placeholder = { Text(text = "Nombre") },
            maxLines = 1,
            keyboardActions = KeyboardActions(onDone = { search() }),
            trailingIcon = {

                Icon(imageVector = Icons.Filled.Search,
                    tint = Primary,
                    contentDescription = "buscar",
                    modifier = Modifier
                        .clickable {
                            search()
                        }
                        .size(24.dp))
            })
    }
}

@Composable
fun SearchedProfile(
    modifier: Modifier = Modifier,
    text: String,
    pic: String,
    following: Boolean,
    onClick: () -> Unit,
    onFollow: (() -> Unit)? = null
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onClick() }) {

            FotoPerfilSizePeque(picture = pic)
            Text(
                if (text.length > 16 && onFollow != null) text.substring(0, 16) + "..." else text,
                color = Secondary,
                fontSize = 4.em,
                maxLines = 1
            )
        }
        if (onFollow != null) Box(contentAlignment = Alignment.CenterEnd) {
            androidx.compose.animation.AnimatedVisibility(
                visible = following, enter = scaleIn(), exit = scaleOut()
            ) {

                Text(text = "Dejar de seguir",
                    color = Primary,
                    fontSize = 5.em,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.clickable { onFollow() })
            }
            androidx.compose.animation.AnimatedVisibility(
                visible = !following, enter = scaleIn(), exit = scaleOut()
            ) {

                Text(text = "Seguir",
                    color = Primary,
                    fontSize = 5.em,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.clickable { onFollow() })
            }
        }

    }

}