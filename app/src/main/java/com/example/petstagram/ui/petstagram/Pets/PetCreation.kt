package com.example.petstagram.ui.petstagram.Pets

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.petstagram.R
import com.example.petstagram.ViewModels.PetCreationViewModel
import com.example.petstagram.barrasuperior.TopBar
import com.example.petstagram.barrasuperior.Variant
import com.example.petstagram.categorias.CategoryList
import com.example.petstagram.cuadrotexto.inter
import com.example.petstagram.ui.theme.Primary
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayImage
import com.google.relay.compose.RelayText
import com.google.relay.compose.ScrollAnchor
import com.example.petstagram.cuadrotexto.TopLevelVariacionTusPublicaciones as ImageBackground
import com.example.petstagram.perfilpropio.EditUsernameButton as Send
import com.example.petstagram.perfilpropio.EditUsernameImageContainer as SendImageContainer
import com.example.petstagram.perfilpropio.YourUserName as PetName

@Composable
fun PetCreation(viewModel: PetCreationViewModel, navController: NavHostController) {

    val categories by viewModel.categories.collectAsState()

    val resource by viewModel.resource.observeAsState()

    val thisContext = (LocalContext.current)

    /**external activity that returns the local uri of the file the user selects*/
    val sourceSelector =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            if (uri != null && uri != Uri.EMPTY) {
                viewModel.setResource(uri, thisContext)
            } else Toast.makeText(thisContext, "selección vacía", Toast.LENGTH_SHORT).show()
        }

    LaunchedEffect(key1 = true) {
        viewModel.startLoading()
    }

    val focusManager = LocalFocusManager.current

    DisposableEffect(key1 = true) {
        onDispose {
            focusManager.clearFocus()
        }
    }

    if (viewModel.sending) {
        Dialog(onDismissRequest = {
            Toast.makeText(
                thisContext,
                "Espera por favor, estamos registrando a ${viewModel.getPetName()}",
                Toast.LENGTH_SHORT
            ).show()
        }) {
            val dots by viewModel.dots.collectAsState()
            Text(text = "Enviando$dots")
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }
    }

    BoxWithConstraints {
        val height = maxHeight

        TopLevel {

            TopBarInstance(
                modifier = Modifier
                    .rowWeight(1.0f)
                    .height(180.dp), navController = navController
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.height(height - 60.dp)
            ) {

                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(height.times(0.06f) + 24.dp)
                            .padding(top = 24.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val context = LocalContext.current
                        PetName(added = " de tu mascota",
                            modifier = Modifier.fillMaxWidth(0.7f),
                            textValue = { viewModel.getPetName() },
                            changeText = { viewModel.setPetName(it) },
                            editing = true,
                            onSend = {
                                viewModel.send(context = context) {

                                    navController.navigateUp()
                                }
                            })
                        Send(
                            Modifier
                                .clickable {
                                    viewModel.send(context = context) {

                                        navController.navigateUp()
                                    }
                                }
                                .wrapContentHeight()) {
                            SendImageContainer {
                                SendImage()
                            }
                        }
                    }
                }
                item {

                    ImageBackground(modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(0.5f)
                        .clickable { sourceSelector.launch("image/*") }) {


                        if (resource == Uri.EMPTY) {
                            Image(
                                painter = painterResource(id = R.drawable.hacer_clic),
                                contentDescription = "clica para cambiar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(height * 0.2f)
                            )
                        } else {
                            SubcomposeAsyncImage(
                                modifier = Modifier.size(height * 0.2f),
                                model = resource,
                                loading = {
                                    CircularProgressIndicator(
                                        Modifier
                                            .size(height * 0.2f)
                                            .height(400.dp)
                                    )
                                },
                                contentDescription = "imagen",
                                contentScale = ContentScale.Crop
                            )
                        }
                        IndicativeText()
                    }
                }
                item {

                    SelectCategoryTexts(
                        modifier = Modifier.height(height.times(0.11f))
                    )
                }
                item {
                    CategoryList(
                        onSelect = { viewModel.setCategory(it) },
                        categoryList = categories,
                        modifier = Modifier.height(height.times(0.42f)),
                        selected = viewModel.selectedCategory
                    )
                }


            }
        }
    }


}

@Composable
fun IndicativeText() {

    Text(
        "Pulsa para cambiar la foto de tu mascota", textAlign = TextAlign.Center
    )
}

@Composable
fun SelectCategoryTexts(modifier: Modifier) {
    Column(
        modifier, verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RelayText(
            content = "Selecciona la categoría",
            fontSize = 24.0.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = inter,
            color = Color.White,
            height = 1.2102272033691406.em,
            maxLines = -1,
            modifier = Modifier
                .fillMaxWidth(1.0f)
                .wrapContentHeight(
                    align = Alignment.CenterVertically, unbounded = true
                )
        )
        RelayText(
            content = "Ten en cuenta\nque esto no se podrá cambiar",
            fontSize = 20.0.sp,
            fontFamily = inter,
            color = Primary,
            fontWeight = FontWeight.Bold,
            height = 1.2102272033691406.em,
            maxLines = -1,
            modifier = Modifier
                .fillMaxWidth(1.0f)
                .wrapContentHeight(
                    align = Alignment.CenterVertically, unbounded = true
                )
        )
    }
}

@Composable
fun SendImage(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.enviar_por_correo),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .requiredWidth(40.0.dp)
            .requiredHeight(40.0.dp)
            .padding(8.dp)
    )
}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier, content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255, red = 35, green = 35, blue = 35
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


/**top bar with which you can move in the app*/
@Composable
fun TopBarInstance(modifier: Modifier = Modifier, navController: NavHostController) {
    TopBar(
        modifier = modifier.fillMaxWidth(1.0f),
        navController = navController,
        variant = Variant.WithMenu
    )
}
