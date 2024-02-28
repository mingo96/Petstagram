package com.example.petstagram.publicar

import android.net.Uri
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.petstagram.ViewModels.PublishViewModel
import com.example.petstagram.barrasuperior.BarraSuperior
import com.example.petstagram.barrasuperior.Variante
import com.example.petstagram.cuadrotexto.Label
import com.example.petstagram.cuadrotexto.Variation
import com.example.petstagram.cuadrotexto.inter
import com.example.petstagram.ui.petstagram.DisplayVideo
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope

/**
 * pantalla para publicar
 *
 *
 * This composable was generated from the UI Package 'publicar'.
 * Generated code; do not edit directly
 */
@Composable
fun NewPostScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: PublishViewModel
) {

    val sourceSelecter = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){ uri ->
        if(uri != null)viewModel.setResource(uri)
    }

    val uriObserver by viewModel.resource.observeAsState()

    BoxWithConstraints {
        val maxAltura = maxHeight
        TopLevel(modifier = modifier) {

            BarraSuperiorInstance(
                modifier = Modifier
                    .rowWeight(
                        1.0f
                    )
                    .height(maxAltura.times(0.23f)), navController = navController
            )
            CuadroTextoInstance()
            CuadroTextoNombreUsuario(accesoTexto = { viewModel.getTitle()}, cambiarTexto = {viewModel.changeTitle(it)})
            CuadroTexto2(modifier.clickable {
                sourceSelecter.launch("*/*") })
            CuadroTexto3(modifier.clickable {
                viewModel.postPost{
                    navController.navigateUp()
                }
            })

            if(uriObserver == null||uriObserver== Uri.EMPTY){
                CircularProgressIndicator(Modifier.padding(bottom = 16.dp).height(40.dp))
            }else{
                if (uriObserver!!.uriFormat().contains("video")){
                    DisplayVideo(source = uriObserver.toString(), modifier = modifier)
                }else{
                    Image(painter = rememberAsyncImagePainter(model = uriObserver),
                        contentDescription = "foto seleccionada",
                        contentScale = ContentScale.Fit,
                        modifier = modifier.height(maxAltura.times(0.3f))
                    )
                }
            }

        }
    }


}


fun Uri.uriFormat(): String {
    return this.toString().split("/").last().split(".").last()
}

@Composable
fun BarraSuperiorInstance(modifier: Modifier = Modifier, navController: NavHostController) {
    BarraSuperior(
        variante = Variante.ConMenu,
        modifier = modifier.fillMaxWidth(1.0f),
        navController = navController
    )
}

@Composable
fun CuadroTextoInstance(modifier: Modifier = Modifier) {
    Label(
        modifier = modifier
            .requiredWidth(296.0.dp)
            .requiredHeight(80.0.dp),
        variation = Variation.CreatePost
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuadroTextoNombreUsuario(
    modifier: Modifier = Modifier,
    accesoTexto: () -> String,
    cambiarTexto: (String) -> Unit
) {
    OutlinedTextField(
        singleLine = true,
        value = accesoTexto.invoke(),
        onValueChange = cambiarTexto,
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

@Composable
fun CuadroTexto1(modifier: Modifier = Modifier) {
    Label(
        modifier = modifier
            .requiredWidth(296.0.dp)
            .requiredHeight(80.0.dp),
        variation = Variation.PostTitle
    )
}

@Composable
fun CuadroTexto2(modifier: Modifier = Modifier) {
    Label(
        modifier = modifier
            .requiredWidth(296.0.dp)
            .requiredHeight(80.0.dp),
        variation = Variation.SelectResource
    )
}

@Composable
fun CuadroTexto3(modifier: Modifier = Modifier) {
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
