package com.example.petstagram.publicaciones

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.petstagram.Controllers.PostsUIController
import com.example.petstagram.guardar.SavePressed
import com.example.petstagram.like.Pressed
import com.example.petstagram.publicacion.Post
import com.example.petstagram.ui.petstagram.seccioncomentarios.BotonMas
import com.example.petstagram.ui.petstagram.seccioncomentarios.CuadroSumar

/**
 * publicaciones
 *
 * This composable was generated from the UI Package 'publicaciones'.
 * Generated code; do not edit directly
 */
@Composable
fun Posts(
    modifier: Modifier = Modifier,
    controller : PostsUIController,
    navController: NavController? = null
) {

    val postsState by controller.posts.collectAsState()
    val isLoading by controller.isLoading.observeAsState()
    val dots by controller.funnyAhhString.collectAsState()

    LaunchedEffect(key1 = Unit) {
        controller.startRollingDots()
    }

    BoxWithConstraints {

        val localwidth by rememberSaveable {
            mutableFloatStateOf(maxWidth.value)
        }

        LazyColumn(
            modifier = modifier
                .width(Dp(localwidth))
                .fillMaxHeight(1.0f)
                .background(
                    Color(
                        alpha = 255,
                        red = 224,
                        green = 164,
                        blue = 0
                    )
                )

        ){
            item{
                Column {

                    for (post in postsState) {

                        var seen by rememberSaveable {
                            mutableStateOf(false)
                        }

                        AnimatedVisibility(visible = seen, enter = slideInHorizontally { it }) {

                            Post(
                                modifier = Modifier
                                    .width(Dp(localwidth))
                                    .padding(vertical = 4.dp),
                                post = post,
                                controller = controller
                            )
                        }
                        LaunchedEffect(key1 = seen) {
                            seen = true
                        }
                    }
                }
            }
            item {
                LaunchedEffect(key1 = true) {
                    if (isLoading==false)
                        controller.scroll()
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                    .fillMaxHeight(0.3f)
                    .fillMaxWidth()) {
                    if (isLoading == true){

                        CircularProgressIndicator(
                            Modifier
                                .width(200.dp)
                                .height(200.dp)
                                .padding(top = 16.dp))
                        Text(text = "Cargando$dots",color = Color.Black)

                    }else{
                        if (navController != null){
                            Text(text = "No hay más, prueba a publicar tú mismo!", color = Color.Black)

                            BotonMas(modifier = Modifier
                                .clickable {
                                    navController.navigate("publicar")
                                }
                                .padding(vertical = 8.dp)) {
                                CuadroSumar(
                                    modifier = Modifier
                                        .rowWeight(1.0f)
                                        .columnWeight(1.0f)
                                )
                            }
                        }else{
                            Text(text = "No hay más publicaciones!", color = Color.Black, modifier = Modifier.padding(vertical = 50.dp))
                        }
                    }
                }
            }

        }



    }
}

