package com.example.petstagram.ui.petstagram.Pets

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.petstagram.Controllers.ProfileInteractor
import com.example.petstagram.R
import com.example.petstagram.ViewModels.PetObserverViewModel
import com.example.petstagram.cuadrotexto.Label
import com.example.petstagram.cuadrotexto.Variation
import com.example.petstagram.perfil.FollowingText
import com.example.petstagram.perfil.ProfilePicInstance
import com.example.petstagram.perfilpropio.DataContainer
import com.example.petstagram.perfilpropio.EditUsernameButton
import com.example.petstagram.perfilpropio.EditUsernameImage
import com.example.petstagram.perfilpropio.EditUsernameImageContainer
import com.example.petstagram.perfilpropio.ImageContainer
import com.example.petstagram.perfilpropio.TopBarInstance
import com.example.petstagram.perfilpropio.UserNameContainer
import com.example.petstagram.perfilpropio.YourUserName
import com.example.petstagram.publicaciones.Posts
import com.example.petstagram.visualizarcategoria.TopLevel

@Composable
fun PetProfile(navController: NavHostController, viewModel: PetObserverViewModel) {

    LaunchedEffect(key1 = Unit) {
        viewModel.keepUpWithUserInfo()
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            viewModel.clean()
        }
    }

    val thisContext = (LocalContext.current)

    /**external activity that returns the local uri of the file the user selects*/
    val sourceSelector =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            if (uri != null && uri != Uri.EMPTY) {
                viewModel.setResource(uri, thisContext)
            } else Toast.makeText(thisContext, "selección vacía", Toast.LENGTH_SHORT).show()
        }

    val observedProfile by viewModel.observedPet.collectAsState()

    val ownerProfile by viewModel.petsOwner.collectAsState()

    val following by viewModel.follow.observeAsState()

    val resource by viewModel.resource.observeAsState()

    val editing by viewModel.isEditing.observeAsState()


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

                if (viewModel.imOwner) item {

                    DataContainer(height = height) {

                        ImageContainer {
                            ProfilePicInstance(
                                Modifier
                                    .height(height.times(0.23f))
                                    .width(height.times(0.23f))
                                    .clickable { sourceSelector.launch("image/*") },
                                url = resource.orEmpty()
                            )
                        }

                        UserNameContainer {
                            YourUserName(
                                added = "Perfil de tu mascota",
                                editing = editing!!,
                                textValue = { viewModel.getPetNameText() },
                                changeText = { viewModel.editPetName(it) },
                                modifier = Modifier.fillMaxWidth(0.7f)
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(8.dp)
                            ) {

                                FollowingText(modifier = Modifier, personal = false) {
                                    viewModel.followers()
                                }
                                EditUsernameButton(Modifier.clickable {
                                    viewModel.editUserNameClicked(thisContext)
                                }) {
                                    EditUsernameImageContainer {
                                        EditUsernameImage()
                                    }
                                }
                            }
                        }
                    }
                }
                else item {

                    DataContainer(height = height) {

                        ImageContainer {
                            Box(contentAlignment = Alignment.BottomEnd) {
                                ProfilePicInstance(
                                    Modifier
                                        .height(
                                            height
                                                .times(0.23f)
                                                .times(0.35f)
                                        )
                                        .width(
                                            height
                                                .times(0.23f)
                                                .times(0.35f)
                                        )
                                        .zIndex(1F)
                                        .clickable { viewModel.enterProfile(ownerProfile) },
                                    url = ownerProfile.profilePic
                                )
                                ProfilePicInstance(
                                    Modifier
                                        .height(height.times(0.23f))
                                        .width(height.times(0.23f))
                                        .zIndex(0F), url = resource!!
                                )
                            }
                        }

                        UserNameContainer {
                            Label(
                                variation = Variation.UserName,
                                added = observedProfile.name,
                                modifier = Modifier.fillMaxWidth(0.7f)
                            )

                            FollowingBox(following = following, profileInteractor = viewModel)

                        }
                    }
                }
                item {


                    Posts(
                        modifier = Modifier
                            .zIndex(5F)
                            .height(height.times(0.925f))
                            .fillMaxWidth(),
                        controller = viewModel
                    )

                }


            }
        }

    }

}

@Composable
fun FollowingBox(following: Boolean?, profileInteractor: ProfileInteractor) {
    Row(
        Modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        FollowingText(modifier = Modifier) {
            profileInteractor.followers()
        }
        Box {
            this@Row.AnimatedVisibility(
                visible = following == true,
                enter = expandHorizontally() + slideInHorizontally(),
                exit = shrinkHorizontally() + slideOutHorizontally()
            ) {

                Image(painter = painterResource(id = R.drawable.sustraccion),
                    contentDescription = "unfollow",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { profileInteractor.unFollow() })

            }
            this@Row.AnimatedVisibility(visible = following == false,
                enter = expandHorizontally() + slideInHorizontally { it },
                exit = shrinkHorizontally() + slideOutHorizontally { it }) {

                Image(painter = painterResource(id = R.drawable.agregar),
                    contentDescription = "follow",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { profileInteractor.follow() })

            }
            this@Row.AnimatedVisibility(visible = following == null,
                enter = expandHorizontally() + slideInHorizontally(),
                exit = shrinkHorizontally() + slideOutHorizontally { it }) {

                Image(
                    painter = painterResource(id = R.drawable.cheque),
                    contentDescription = "just followed",
                    modifier = Modifier.size(40.dp)
                )

            }
        }
    }
}