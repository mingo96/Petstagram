package com.example.petstagram.perfilpropio

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.petstagram.R
import com.example.petstagram.ViewModels.OwnProfileViewModel
import com.example.petstagram.barrasuperior.TopBar
import com.example.petstagram.barrasuperior.Variant
import com.example.petstagram.cuadrotexto.Label
import com.example.petstagram.cuadrotexto.Variation
import com.example.petstagram.cuadrotexto.inter
import com.example.petstagram.perfil.FollowingText
import com.example.petstagram.perfil.ProfilePicInstance
import com.example.petstagram.publicaciones.Posts
import com.example.petstagram.ui.petstagram.Pets.PetList
import com.example.petstagram.ui.theme.Primary
import com.example.petstagram.ui.theme.Secondary
import com.example.petstagram.visualizarcategoria.TopLevel
import com.google.relay.compose.CrossAxisAlignment
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayImage
import com.google.relay.compose.RelayVector

/**UI screen for the user info and data input (profile pic, username)
 * @param navController to navigate
 * @param viewModel to receive and send information
 */
@Composable
fun MyProfile(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: OwnProfileViewModel
) {

    //on launch start loading user info
    LaunchedEffect(key1 = Unit) {
        viewModel.keepUpWithUserInfo()
    }

    //on exit stop loading
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopLoading()
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

    val state by viewModel.state.observeAsState()

    val offsetObserver by viewModel.offset.collectAsState()

    /**actual profile pic*/
    val profilePicObserver by viewModel.resource.observeAsState()

    /**informs UI of changes in loading value*/
    val isLoading by viewModel.isLoading.observeAsState()

    /**informs UI of changes in editing value*/
    val editing by viewModel.isEditing.observeAsState()

    val pets by viewModel.pets.collectAsState()

    val accessText: () -> String = { viewModel.getUserNameText() }

    val changeText: (String) -> Unit = { viewModel.editUserName(it) }

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
                                    .width(height.times(0.23f))
                                    .clickable { sourceSelector.launch("image/*") },
                                url = profilePicObserver.orEmpty()
                            )
                        }

                        UserNameContainer {
                            YourUserName(
                                editing = editing!!,
                                textValue = accessText,
                                changeText = changeText,
                                modifier = Modifier.fillMaxWidth(0.7f)
                            )
                            Row (horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(8.dp)){

                                FollowingText(modifier = Modifier, personal = true) {
                                    viewModel.actualUser.followers.size
                                }
                                EditUsernameButton(
                                    Modifier.clickable {
                                        viewModel.editUserNameClicked(thisContext)
                                    }
                                ) {
                                    EditUsernameBackgroundCircle(
                                        Modifier
                                            .requiredWidth(32.0.dp)
                                            .requiredHeight(32.0.dp)
                                    )
                                    EditUsernameImageContainer {
                                        EditUsernameImage()
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
                            PostsInstance(
                                modifier = Modifier
                                    .zIndex(5F)
                                    .height(height.times(0.8f))
                                    .offset(x = offsetObserver - width),
                                viewModel = viewModel
                            )
                            PetList(
                                pets = pets,
                                onNewPet = { navController.navigate("añadirMascota") },
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


/**top bar with which you can move in the app*/
@Composable
fun TopBarInstance(modifier: Modifier = Modifier, navController: NavHostController) {
    TopBar(
        modifier = modifier.fillMaxWidth(1.0f),
        navController = navController,
        variant = Variant.WithMenu
    )
}

@Composable
fun DataContainer(height: Dp, content: @Composable RowScope.() -> Unit) {
    Row(
        Modifier
            .height(height.times(0.23f) + 24.dp)
            .padding(top = 24.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        content = content
    )
}

@Composable
fun UserNameContainer(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        arrangement = RelayContainerArrangement.Column,
        mainAxisAlignment = MainAxisAlignment.SpaceEvenly,
        crossAxisAlignment = CrossAxisAlignment.Center,
        itemSpacing = 24.0,
        content = content,
        modifier = modifier.fillMaxHeight()
    )
}

@Composable
fun EditUsernameButton(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        content = content,
        modifier = modifier
            .requiredWidth(32.0.dp)
            .requiredHeight(32.0.dp)
    )
}

@Composable
fun EditUsernameImageContainer(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        arrangement = RelayContainerArrangement.Row,
        content = content,
        modifier = modifier
            .requiredWidth(32.0.dp)
            .requiredHeight(32.0.dp)
            .alpha(alpha = 100.0f)
    )
}

@Composable
fun EditUsernameImage(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.perfil_propio_imagen_cambiar_nombre_usuario),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .requiredWidth(18.0.dp)
            .requiredHeight(18.0.dp)
    )
}

@Composable
fun EditUsernameBackgroundCircle(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.perfil_propio_circulo_editar_nombre_usuario),
        modifier = modifier
    )
}

@Composable
fun ImageContainer(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        content = content,
        modifier = modifier
    )
}


/**pass-by function to call [Posts], not much to see (logic-wise)*/
@Composable
fun PostsInstance(modifier: Modifier = Modifier, viewModel: OwnProfileViewModel) {

    Posts(
        modifier = modifier
            .fillMaxWidth(1.0f),
        controller = viewModel
    )
}


/**text in-out function, not much to see (logic-wise)
 * @param editing `true` we play a input TextField
 * @param editing `false` we play default text*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourUserName(
    modifier: Modifier = Modifier,
    editing: Boolean = false,
    textValue: () -> String,
    changeText: (String) -> Unit = {}
) {

    if (editing)
        OutlinedTextField(
            value = textValue.invoke(),
            onValueChange = changeText,
            textStyle = TextStyle(
                fontSize = 20.0.sp,
                fontFamily = inter,
                lineHeight = 1.2102272033691406.em,
                color = Color.Black
            ),
            singleLine = true,
            shape = RoundedCornerShape(10),
            modifier = modifier
                .wrapContentHeight(
                    align = Alignment.CenterVertically,
                    unbounded = true
                )
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(10.dp),
                    color = Color(
                        alpha = 38,
                        red = 0,
                        green = 0,
                        blue = 0
                    )
                )
                .background(
                    Color(
                        alpha = 255,
                        red = 225,
                        green = 196,
                        blue = 1
                    ),
                    shape = RoundedCornerShape(10)
                )
        )
    else
        Label(
            modifier = Modifier.wrapContentWidth(),
            variation = Variation.YourProfile,
            added = textValue()
        )
}


/**pass-by function to call a [Label], not much to see (logic-wise)*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateSelector(modifier: Modifier = Modifier, state: Boolean, onClick: () -> Unit) {
    SingleChoiceSegmentedButtonRow(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        SegmentedButton(
            selected = state,
            onClick = onClick,
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
            colors = colors(state)
        ) {
            Text(
                text = "Publicaciones",
                style = TextStyle(color = if (state) Color.Black else Color.White)
            )
        }
        SegmentedButton(
            selected = !state,
            onClick = onClick,
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
            colors = colors(state)
        ) {
            Text(
                text = "Mascotas",
                style = TextStyle(color = if (!state) Color.Black else Color.White)
            )

        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun colors(state : Boolean): SegmentedButtonColors {
    return SegmentedButtonDefaults.colors(
        activeContainerColor = if (state) Primary else Secondary,
        activeBorderColor = if (state) Primary else Secondary,
        inactiveBorderColor = if (state) Primary else Secondary,
        activeContentColor = Color.Black
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
        scrollable = true,
        itemSpacing = 24.0,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
