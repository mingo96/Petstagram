package com.example.petstagram.publicaciones

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.petstagram.Controllers.PostsUIController
import com.example.petstagram.publicacion.Post
import com.example.petstagram.ui.petstagram.seccioncomentarios.BotonMas
import com.example.petstagram.ui.petstagram.seccioncomentarios.CuadroSumar
import com.example.petstagram.ui.theme.Primary
import com.example.petstagram.ui.theme.Secondary
import kotlinx.coroutines.delay

/**
 * publicaciones
 *
 * This composable was generated from the UI Package 'publicaciones'.
 * Generated code; do not edit directly
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Posts(
    modifier: Modifier = Modifier,
    controller: PostsUIController,
    navController: NavController? = null
) {

    val postsState by controller.posts.collectAsState()
    val isLoading by controller.isLoading.observeAsState(false)
    val dots by controller.funnyAhhString.collectAsState()
    val postSelected by controller.postSelectedForOptions.observeAsState()
    val context = LocalContext.current
    val options by controller.optionsDisplayed.observeAsState(false)

    LaunchedEffect(key1 = Unit) {
        controller.startRollingDots()
    }


    val pullState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = { controller.scroll() },
    )
    BoxWithConstraints(
        Modifier.pullRefresh(
            pullState
        )
    ) {

        val localwidth by rememberSaveable {
            mutableFloatStateOf(maxWidth.value)
        }
        val localHeight by rememberSaveable {
            mutableFloatStateOf(maxHeight.value)
        }

        val state: LazyListState = rememberLazyListState()

        LazyColumn(
            state = state, modifier = modifier
                .width(Dp(localwidth))
                .fillMaxHeight(1.0f)
                .background(
                    Primary,
                )
        ) {


            itemsIndexed(postsState) { index, it ->
                var seen by rememberSaveable {
                    mutableStateOf(false)
                }

                AnimatedVisibility(visible = postSelected == it,
                    enter = slideInHorizontally() + expandVertically(),
                    exit = slideOutHorizontally { it } + shrinkVertically()) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                controller.reportPost(context = context)
                                controller.clearOptions()
                            },
                            colors = buttonColors(),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Text(text = if (controller.actualUser.id == it.creatorUser!!.id) "Borrar" else "Reportar")
                        }
                        Button(
                            onClick = {
                                controller.savePostResource(context = context)
                                controller.clearOptions()
                            },
                            colors = buttonColors(),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Text(text = "Descargar")
                        }
                        Button(
                            onClick = { controller.clearOptions() },
                            colors = buttonColors(),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Text(text = "Cancelar")
                        }
                    }
                }

                AnimatedVisibility(
                    visible = seen, enter = slideInHorizontally() + expandVertically()
                ) {
                    val isVisible by remember {
                        derivedStateOf {
                            (index == state.firstVisibleItemIndex || index == state.firstVisibleItemIndex + 1 && postsState[state.firstVisibleItemIndex].typeOfMedia != "video") && !state.isScrollInProgress
                        }
                    }
                    Post(
                        modifier = Modifier
                            .width(Dp(localwidth))
                            .padding(vertical = 4.dp),
                        post = it,
                        controller = controller,
                        isVisible = isVisible
                    )
                }
                LaunchedEffect(key1 = seen) {
                    seen = true
                }
            }
            //item{
            //    Column {
//
            //        for (post in postsState) {
//
            //            var seen by rememberSaveable {
            //                mutableStateOf(false)
            //            }
//
            //            AnimatedVisibility(visible = seen, enter = slideInHorizontally { it }) {
//
            //                Post(
            //                    modifier = Modifier
            //                        .width(Dp(localwidth))
            //                        .padding(vertical = 4.dp),
            //                    post = post,
            //                    controller = controller
            //                )
            //            }
            //            LaunchedEffect(key1 = seen) {
            //                seen = true
            //            }
            //        }
            //    }
            //}
            //item {
            //}
            item {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxHeight(0.3f)
                        .fillMaxWidth()
                ) {

                    LaunchedEffect(key1 = true) {
                        if (!isLoading) {
                            controller.scroll()
                        }
                    }
                    if (isLoading) {

                        CircularProgressIndicator(
                            Modifier
                                .width(200.dp)
                                .height(200.dp)
                                .padding(top = 16.dp)
                        )
                        Text(text = "Cargando$dots", color = Color.Black)

                    } else {
                        if (navController != null) {


                            Icon(imageVector = if (!options) Icons.Outlined.KeyboardArrowDown else Icons.Outlined.KeyboardArrowUp,
                                contentDescription = "display",
                                Modifier
                                    .clickable { controller.toggleOptionsDisplayed() }
                                    .size(Dp(localHeight * 0.1f)))

                            AnimatedVisibility(
                                visible = options,
                                enter = expandVertically(animationSpec = tween(100)) + scaleIn(
                                    animationSpec = tween(100)
                                ),
                                exit = shrinkVertically(animationSpec = tween(100)) + scaleOut(
                                    animationSpec = tween(100)
                                ),
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                LaunchedEffect(key1 = Unit) {
                                    delay(100)
                                    state.animateScrollToItem(postsState.size + 1)
                                }
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    FloatingActionButton(
                                        onClick = {
                                            navController.navigate("publicar")
                                        },
                                        modifier = Modifier.fillMaxWidth(0.3333f),
                                        containerColor = Secondary,
                                        contentColor = Color.White
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Add,
                                            contentDescription = "add",
                                            tint = Color.Black
                                        )
                                    }

                                    FloatingActionButton(
                                        onClick = { controller.scroll(false) },
                                        modifier = Modifier.fillMaxWidth(0.5f),
                                        containerColor = Secondary,
                                        contentColor = Color.White
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Refresh,
                                            contentDescription = "updatea",
                                            tint = Color.Black
                                        )

                                    }

                                }

                            }

                        } else {
                            Column(
                                verticalArrangement = Arrangement.SpaceEvenly,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.height(height = Dp(localHeight * 0.3f))
                            ) {

                                Text(
                                    text = "No hay más publicaciones!",
                                    color = Color.Black,
                                )

                                FloatingActionButton(
                                    onClick = { controller.scroll(false) },
                                    Modifier.padding(8.dp),
                                    containerColor = Secondary,
                                    contentColor = Color.White
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Refresh,
                                        contentDescription = "updatea",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }

        PullRefreshIndicator(
            refreshing = false, state = pullState, modifier = Modifier.align(
                Alignment.TopCenter
            )
        )


    }
}

@Composable
fun buttonColors(): ButtonColors {
    return ButtonColors(
        contentColor = Color.Black,
        containerColor = Color.White.copy(0.5F),
        disabledContainerColor = Primary,
        disabledContentColor = Primary
    )
}

