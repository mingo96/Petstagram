package com.example.petstagram.publicar

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.petstagram.R
import com.example.petstagram.ViewModels.PublishViewModel
import com.example.petstagram.ViewModels.getMimeType
import com.example.petstagram.barrasuperior.TopBar
import com.example.petstagram.barrasuperior.Variant
import com.example.petstagram.cuadroinfo.DeadPostDownBar
import com.example.petstagram.cuadroinfo.PostDownBar
import com.example.petstagram.cuadrotexto.Label
import com.example.petstagram.cuadrotexto.Variation
import com.example.petstagram.cuadrotexto.inter
import com.example.petstagram.guardar.SavePressed
import com.example.petstagram.publicacion.CuadroInfoInstance
import com.example.petstagram.publicacion.PostSource
import com.example.petstagram.ui.petstagram.DisplayVideoFromSource
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.ScrollAnchor

/**UI screen to send data to the Server, navigates through [navController] and sends/recieves data
 * from [viewModel]*/
@Composable
fun NewPostScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: PublishViewModel
) {
    val loadingText by viewModel.text.collectAsState()

    val isSendingInfo by viewModel.isSendingInfo.observeAsState()

    val context = LocalContext.current
    //launcher for an external activity that returns the URI of the file you selected
    val sourceSelecter = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){ uri ->
        if(uri != null && uri != Uri.EMPTY) {

            viewModel.setResource(uri, context)
        }
        else {
            Toast.makeText(context, "selección vacía", Toast.LENGTH_SHORT).show()
        }

    }
    //uri observed to see it before publishing
    val uriObserver by viewModel.resource.observeAsState()

    BackHandler {
        if (isSendingInfo==false){
            navController.navigateUp()
        }
    }

    BoxWithConstraints {
        val height = maxHeight
        TopLevel(modifier = modifier) {

            if(isSendingInfo==false) {
                TopBarInstance(
                    modifier = Modifier
                        .rowWeight(
                            1.0f
                        )
                        .height(height.times(0.23f)), navController = navController
                )
            }

            BoxWithConstraints(
                Modifier
                    .fillMaxWidth()
                    .height(height.times(0.935f))) {
                val height = maxHeight

                LazyColumn (horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(36.dp),
                    modifier = Modifier.fillMaxHeight()){

                    if(isSendingInfo == false) {
                        item {

                            Column (modifier = Modifier
                                .background(
                                    Color(
                                        alpha = 255,
                                        red = 35,
                                        green = 35,
                                        blue = 35
                                    )
                                )
                                .padding(vertical = 16.dp)
                                .wrapContentHeight()
                                .fillMaxWidth()){

                                CuadroInfoInstance(post = viewModel.newPost)
                                if (getMimeType(context,uriObserver!!)?.startsWith("video") == true){
                                    val source = remember{
                                        MediaItem.fromUri(uriObserver!!)
                                    }
                                    DisplayVideoFromSource(source = source, modifier = modifier)
                                }else if(getMimeType(context,uriObserver!!)?.startsWith("image") == true){
                                    Image(painter = rememberAsyncImagePainter(model = uriObserver, contentScale = ContentScale.Crop),
                                        contentDescription = "foto seleccionada",
                                        contentScale = ContentScale.Crop,
                                        modifier = modifier
                                            .height(height.times(0.5f))
                                            .fillMaxWidth()
                                    )
                                }else{
                                    Image(
                                        painter = painterResource(id = R.drawable.hacer_clic),
                                        contentDescription = "Aún nada",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable {
                                            sourceSelecter.launch("*/*")
                                        }
                                    )
                                }
                                DeadPostDownBar(added = viewModel.newPost)

                            }
                        }
                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {

                                TitleTextInput(
                                    textValue = { viewModel.getTitle() },
                                    changeText = { viewModel.changeTitle(it) })
                                SourceSelectorTextButton(
                                    modifier.clickable {
                                        sourceSelecter.launch("*/*")
                                    }
                                )
                                PublishPostTextButton(
                                    modifier.clickable {
                                        viewModel.postPost(context = context, onSuccess = {
                                            navController.navigateUp()
                                        }
                                        )
                                    }
                                )
                            }

                        }
                    }

                    if (isSendingInfo == true)
                        item {
                            Text(text = loadingText)
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
        variant = Variant.WithMenu,
        modifier = modifier.fillMaxWidth(1.0f),
        navController = navController
    )
}

/**basic in-out info representation text, not much to see (logic-wise)*/
@Composable
fun TitleTextInput(
    modifier: Modifier = Modifier,
    textValue: () -> String,
    changeText: (String) -> Unit
) {
    OutlinedTextField(
        singleLine = true,
        value = textValue.invoke(),
        onValueChange = changeText,
        textStyle = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = 26.0.sp,
            fontFamily = inter,
            lineHeight = 1.2102272033691406.em,
            color = Color.White,
            fontWeight = FontWeight(700.0.toInt())
        ),
        shape = RoundedCornerShape(10),
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight()
            .background(
                Color(
                    alpha = 255,
                    red = 224,
                    green = 164,
                    blue = 0
                ),
                shape = RoundedCornerShape(10)
            )
            .requiredWidth(296.0.dp)
            .requiredHeight(80.0.dp)
    )
}

/**pass-by function to call a [Label], not much to see (logic-wise)*/
@Composable
fun SourceSelectorTextButton(modifier: Modifier = Modifier) {
    Label(
        modifier = modifier
            .requiredWidth(296.0.dp)
            .requiredHeight(80.0.dp),
        variation = Variation.SelectResource
    )
}


/**pass-by function to call a [Label], not much to see (logic-wise)*/
@Composable
fun PublishPostTextButton(modifier: Modifier = Modifier) {
    Label(
        modifier = modifier
            .requiredWidth(296.0.dp)
            .requiredHeight(80.0.dp),
        variation = Variation.Publish
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
        mainAxisAlignment = MainAxisAlignment.Center,
        scrollable = true,
        content = content,
        scrollAnchor = ScrollAnchor.End,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
