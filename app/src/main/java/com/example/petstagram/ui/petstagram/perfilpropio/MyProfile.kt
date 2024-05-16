package com.example.petstagram.perfilpropio

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.petstagram.R
import com.example.petstagram.ViewModels.OwnProfileViewModel
import com.example.petstagram.barrasuperior.TopBar
import com.example.petstagram.barrasuperior.Variant
import com.example.petstagram.cuadrotexto.Label
import com.example.petstagram.cuadrotexto.Variation
import com.example.petstagram.cuadrotexto.inter
import com.example.petstagram.perfil.ProfilePicInstance
import com.example.petstagram.publicaciones.Posts
import com.example.petstagram.visualizarcategoria.TopLevel
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
    LaunchedEffect(key1 = viewModel){
        viewModel.keepUpWithUserInfo()
    }

    //on exit stop loading
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopLoading()
        }
    }

    val thisContext =(LocalContext.current)
    /**external activity that returns the local uri of the file the user selects*/
    val sourceSelector = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){ uri ->
        if(uri != null&&uri != Uri.EMPTY) {
            viewModel.setResource(uri, thisContext)
        }else Toast.makeText(thisContext, "selección vacía", Toast.LENGTH_SHORT).show()
    }


    /**actual profile pic*/
    val profilePicObserver by viewModel.resource.observeAsState()

    /**informs UI of changes in loading value*/
    val isLoading by viewModel.isLoading.observeAsState()

    /**informs UI of changes in editing value*/
    val editing by viewModel.isEditing.observeAsState()


    val accessText : ()->String = { viewModel.getUserNameText() }

    val changeText :(String)->Unit = {viewModel.editUserName(it)}

    BoxWithConstraints {
        val height = maxHeight
        TopLevel(modifier = modifier) {
            TopBarInstance(modifier = Modifier
                .rowWeight(1.0f)
                .height(height.times(0.23f)),navController = navController)
            UserNameContainer(Modifier.height(height.times(0.06f))) {
                YourUserName(editing = editing!!, textValue = accessText, changeText = changeText, modifier = Modifier.fillMaxWidth(0.7f))
                EditUsernameButton(
                    Modifier.clickable {
                        viewModel.editUserNameClicked(thisContext)
                    }
                ) {
                    EditUsernameBackgroundCircle(Modifier
                        .requiredWidth(32.0.dp)
                        .requiredHeight(32.0.dp))
                    EditUsernameImageContainer {
                        EditUsernameImage()
                    }
                }
            }
            ImageContainer {
                ProfilePicInstance(
                    Modifier
                        .height(height.times(0.30f))
                        .width(height.times(0.30f)),
                    url = profilePicObserver.orEmpty())
                EditProfilePicButton(Modifier.clickable { sourceSelector.launch("image/*") }) {
                    EditProfilePicButtonBackgroundCircle()
                    EditProfilePicImageContainer {
                        EditProfilePicImage()
                    }
                }
            }
            YourPostsLabel(Modifier.height(height.times(0.06f)))
            if (isLoading!!)
                CircularProgressIndicator(
                    modifier
                        .rowWeight(1.0f)
                        .height(height.times(0.825f))
                        .fillMaxWidth(0.8f))
            else
                PostsInstance(
                    modifier = Modifier
                        .rowWeight(1.0f)
                        .height(height),
                    viewModel = viewModel
                )
        }
    }

}


/**top bar with which you can move in the app*/
@Composable
fun TopBarInstance(modifier: Modifier = Modifier, navController: NavHostController) {
    TopBar(modifier = modifier.fillMaxWidth(1.0f),navController = navController, variant = Variant.WithMenu)
}
@Composable
fun UserNameContainer(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        arrangement = RelayContainerArrangement.Row,
        itemSpacing = 24.0,
        content = content,
        modifier = modifier
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
        arrangement = RelayContainerArrangement.Row,
        itemSpacing = 24.0,
        content = content,
        modifier = modifier
    )
}

@Composable
fun EditProfilePicButton(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        content = content,
        modifier = modifier
            .requiredWidth(48.0.dp)
            .requiredHeight(48.0.dp)
    )
}

@Composable
fun EditProfilePicButtonBackgroundCircle(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.perfil_propio_circulo_editar),
        modifier = modifier
            .requiredWidth(48.0.dp)
            .requiredHeight(48.0.dp)
    )
}

@Composable
fun EditProfilePicImageContainer(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        arrangement = RelayContainerArrangement.Row,
        content = content,
        modifier = modifier
            .requiredWidth(48.0.dp)
            .requiredHeight(48.0.dp)
            .alpha(alpha = 100.0f)
    )
}

@Composable
fun EditProfilePicImage(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.perfil_propio_imagen_editar),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .requiredWidth(32.0.dp)
            .requiredHeight(32.0.dp)
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
fun YourUserName(modifier: Modifier = Modifier,
                 editing : Boolean = true,
                 textValue:()->String,
                 changeText : (String)->Unit) {

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
            modifier = modifier.requiredWidth(94.0.dp),
            variation = Variation.YourProfile
        )
}


/**pass-by function to call a [Label], not much to see (logic-wise)*/
@Composable
fun YourPostsLabel(modifier: Modifier = Modifier) {
    Label(
        modifier = modifier.requiredWidth(186.0.dp),
        variation = Variation.YourPosts
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
