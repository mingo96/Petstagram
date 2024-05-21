package com.example.petstagram.perfil

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.petstagram.R
import com.example.petstagram.ViewModels.ProfileObserverViewModel
import com.example.petstagram.cuadrotexto.Label
import com.example.petstagram.cuadrotexto.Variation
import com.example.petstagram.fotoperfil.FotoPerfilBase
import com.example.petstagram.fotoperfil.Size
import com.example.petstagram.perfilpropio.DataContainer
import com.example.petstagram.perfilpropio.ImageContainer
import com.example.petstagram.perfilpropio.PostsInstance
import com.example.petstagram.perfilpropio.StateSelector
import com.example.petstagram.perfilpropio.TopBarInstance
import com.example.petstagram.perfilpropio.UserNameContainer
import com.example.petstagram.perfilpropio.YourUserName
import com.example.petstagram.publicaciones.Posts
import com.example.petstagram.ui.petstagram.Pets.PetList
import com.example.petstagram.visualizarcategoria.TopLevel

/**
 * perfil ajeno
 *
 * This composable was generated from the UI Package 'perfil'.
 * Generated code; do not edit directly
 */
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
            viewModel.clear()
        }
    }

    val state by viewModel.state.observeAsState()

    val offsetObserver by viewModel.offset.collectAsState()

    /**informs UI of changes in loading value*/
    val isLoading by viewModel.isLoading.observeAsState()

    val pets by viewModel.pets.collectAsState()

    val observedProfile by viewModel.observedProfile.collectAsState()

    val following by viewModel.follow.observeAsState()

    BoxWithConstraints {
        val height = maxHeight
        val width = maxWidth

        TopLevel(modifier = Modifier) {
            TopBarInstance(
                modifier = Modifier
                    .rowWeight(1.0f)
                    .height(height.times(0.24f)), navController = navController
            )


            LazyColumn(
                Modifier.height(height.times(0.926f)),
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
                            Row(Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                                FollowingText(modifier = Modifier) {
                                    viewModel.followers()
                                }

                                Box {
                                    this@Row.AnimatedVisibility(visible = following == true) {

                                        Image(
                                            painter = painterResource(id = R.drawable.sustraccion),
                                            contentDescription = "unfollow",
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clickable { viewModel.unFollow() })

                                    }
                                    this@Row.AnimatedVisibility(visible = following == false) {

                                        Image(
                                            painter = painterResource(id = R.drawable.agregar),
                                            contentDescription = "follow",
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clickable { viewModel.follow() })

                                    }
                                    this@Row.AnimatedVisibility(visible = following == null) {

                                        Image(
                                            painter = painterResource(id = R.drawable.cheque),
                                            contentDescription = "just followed",
                                            modifier = Modifier.size(40.dp)
                                        )

                                    }
                                }

                            }
                        }
                    }
                }

                item {

                    StateSelector(
                        Modifier.height(height.times(0.06f)),
                        state = state!!,
                        onClick = { viewModel.ToggleState(width) })
                }

                item {

                    if (isLoading!!)
                        CircularProgressIndicator(
                            modifier
                                .rowWeight(1.0f)
                                .height(height.times(0.8f))
                                .fillMaxWidth(0.8f)
                        )
                    else {
                        Box(
                            Modifier.fillMaxWidth(),
                        ) {
                            Posts(
                                modifier = Modifier
                                    .zIndex(5F)
                                    .height(height.times(0.8f))
                                    .fillMaxWidth()
                                    .offset(x = offsetObserver - width),
                                controller = viewModel
                            )
                            PetList(
                                pets = pets,
                                onSelect = {},
                                modifier = Modifier
                                    .height(height.times(0.8f))
                                    .offset(x = offsetObserver)
                            )


                        }
                    }
                }
            }

        }

    }
}

@Composable
fun ProfilePicInstance(modifier: Modifier = Modifier, url: String = "") {
    FotoPerfilBase(
        size = Size.Enorme,
        modifier = modifier,
        added = url
    )
}

@Composable
fun FollowingText(modifier: Modifier, personal: Boolean = false, followers: () -> Int) {
    Box(
        modifier = Modifier
            .border(
                2.dp,
                Color.Gray,
                RoundedCornerShape(20)
            )
            .padding(8.dp)
    ) {
        Text(
            text = "${if (personal) "Te" else "Le"} siguen: ${followers()}",
            style = TextStyle(color = Color.White),
            modifier = modifier
                .wrapContentSize()
        )
    }
}
