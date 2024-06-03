package com.example.petstagram.perfil

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.petstagram.ViewModels.PetObserverViewModel
import com.example.petstagram.ViewModels.ProfileObserverViewModel
import com.example.petstagram.cuadrotexto.Label
import com.example.petstagram.cuadrotexto.Variation
import com.example.petstagram.fotoperfil.FotoPerfilBase
import com.example.petstagram.perfilpropio.DataContainer
import com.example.petstagram.perfilpropio.ImageContainer
import com.example.petstagram.perfilpropio.StateSelector
import com.example.petstagram.perfilpropio.TopBarInstance
import com.example.petstagram.perfilpropio.UserNameContainer
import com.example.petstagram.publicaciones.Posts
import com.example.petstagram.ui.petstagram.Pets.FollowingBox
import com.example.petstagram.ui.petstagram.Pets.PetList
import com.example.petstagram.visualizarcategoria.TopLevel
import kotlinx.coroutines.launch

/**
 * perfil ajeno
 *
 * This composable was generated from the UI Package 'perfil'.
 * Generated code; do not edit directly
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SomeonesProfile(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ProfileObserverViewModel
) {

    //on launch start loading user info
    LaunchedEffect(key1 = Unit) {
        viewModel.keepUpWithUserInfo()
    }

    //on exit stop loading
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopLoading()
            viewModel.clean()
        }
    }

    val pets by viewModel.pets.collectAsState()

    val observedProfile by viewModel.observedProfile.collectAsState()

    val following by viewModel.follow.observeAsState()

    val scroll = rememberLazyListState()

    val scope = rememberCoroutineScope()

    BoxWithConstraints {
        val height = maxHeight
        val width = maxWidth

        TopLevel(modifier = Modifier) {
            TopBarInstance(
                modifier = Modifier
                    .rowWeight(1.0f)
                    .height(180.dp), navController = navController
            )


            LazyColumn(
                Modifier.height(height - 60.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {

                    DataContainer(height = height) {

                        ImageContainer {
                            ProfilePicInstance(
                                Modifier
                                    .height(height.times(0.23f))
                                    .width(height.times(0.23f)),
                                url = observedProfile.profilePic
                            )
                        }

                        UserNameContainer {
                            Label(
                                variation = Variation.UserName,
                                added = observedProfile.userName,
                                modifier = Modifier.fillMaxWidth(0.7f)
                            )

                            FollowingBox(following = following, profileInteractor = viewModel)

                        }
                    }
                }

                item {

                    val state by remember {
                        derivedStateOf { scroll.firstVisibleItemIndex == 0 }
                    }
                    StateSelector(Modifier.height(56.dp), state = state, onClick = {
                        scope.launch {
                            scroll.animateScrollToItem(it)
                        }
                    })
                }

                item {
                    val flingBehavior = rememberSnapFlingBehavior(lazyListState = scroll)
                    LazyRow(
                        state = scroll,
                        flingBehavior = flingBehavior,
                        modifier = Modifier.width(width * 2)
                            .height(height-60.dp-56.dp-48.dp),
                    ) {
                        item {

                            Posts(
                                modifier = Modifier
                                    .zIndex(5F)
                                    .height(height.times(0.8f))
                                    .width(width), controller = viewModel
                            )
                        }
                        item {
                            PetList(
                                pets = pets,
                                onSelect = {
                                    PetObserverViewModel.staticPet = it
                                    navController.navigate("mascota")
                                },
                                modifier = Modifier
                                    .height(height.times(0.8f))
                                    .width(width),

                                )
                        }


                    }
                }

            }

        }

    }
}

@Composable
fun ProfilePicInstance(modifier: Modifier = Modifier, url: String = "a") {
    FotoPerfilBase(
        modifier = modifier, added = url
    )
}

@Composable
fun FollowingText(modifier: Modifier, personal: Boolean = false, followers: () -> Int) {
    Box(
        modifier = Modifier
            .border(
                2.dp, Color.Gray, RoundedCornerShape(20)
            )
            .padding(8.dp)
    ) {
        Text(
            text = "${if (personal) "Te" else "Le"} siguen: ${followers()}",
            style = TextStyle(color = Color.White),
            modifier = modifier.wrapContentSize()
        )
    }
}
