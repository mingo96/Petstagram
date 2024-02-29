package com.example.petstagram.publicar

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.net.toFile
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.petstagram.ViewModels.PublishViewModel
import com.example.petstagram.ViewModels.getMimeType
import com.example.petstagram.barrasuperior.TopBar
import com.example.petstagram.barrasuperior.Variant
import com.example.petstagram.cuadrotexto.Label
import com.example.petstagram.cuadrotexto.Variation
import com.example.petstagram.cuadrotexto.inter
import com.example.petstagram.ui.petstagram.DisplayVideo
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope

/**UI screen to send data to the Server, navigates through [navController] and sends/recieves data
 * from [viewModel]*/
@Composable
fun NewPostScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: PublishViewModel
) {


    val context = LocalContext.current
    //launcher for an external activity that returns the URI of the file you selected
    val sourceSelecter = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){ uri ->
        if(uri != null && uri != Uri.EMPTY) {

            viewModel.setResource(uri)
        }
        else {
            Toast.makeText(context, "selección vacía", Toast.LENGTH_SHORT).show()
        }

    }
    //uri observed to see it before publishing
    val uriObserver by viewModel.resource.observeAsState()

    BoxWithConstraints {
        val height = maxHeight
        TopLevel(modifier = modifier) {

            TopBarInstance(
                modifier = Modifier
                    .rowWeight(
                        1.0f
                    )
                    .height(height.times(0.23f)), navController = navController
            )
            CreatePostText()
            TitleTextInput(textValue = { viewModel.getTitle()}, changeText = {viewModel.changeTitle(it)})
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

            //if the source isnt available yet, just CircularProgressIndicator
            if(uriObserver == null||uriObserver== Uri.EMPTY){
                CircularProgressIndicator(
                    Modifier
                        .padding(bottom = 16.dp)
                        .height(40.dp))
            }else{
                if (getMimeType(context, uriObserver!!)?.startsWith("/video") == true){
                    DisplayVideo(source = uriObserver.toString(), modifier = modifier)
                }else{
                    Image(painter = rememberAsyncImagePainter(model = uriObserver),
                        contentDescription = "foto seleccionada",
                        contentScale = ContentScale.Fit,
                        modifier = modifier.height(height.times(0.3f))
                    )
                }
            }

        }
    }


}

/**@return the format this URI has*/
fun Uri.uriFormat(): String {
    return this.toString().split("/").last()
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

/**pass-by function to call a [Label], not much to see (logic wise)*/
@Composable
fun CreatePostText(modifier: Modifier = Modifier) {
    Label(
        modifier = modifier
            .requiredWidth(296.0.dp)
            .requiredHeight(80.0.dp),
        variation = Variation.CreatePost
    )
}

/**basic in-out info representation text, not much to see (logic-wise)*/
@OptIn(ExperimentalMaterial3Api::class)
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
        mainAxisAlignment = MainAxisAlignment.End,
        scrollable = true,
        itemSpacing = 72.0,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
