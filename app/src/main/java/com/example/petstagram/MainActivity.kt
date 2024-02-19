package com.example.petstagram

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petstagram.loginenmovil.LoginEnMovil
import com.example.petstagram.menuprincipal.MenuPrincipal
import com.example.petstagram.perfil.Perfil
import com.example.petstagram.perfilpropio.PerfilPropio
import com.example.petstagram.ui.theme.PetstagramConLogicaTheme
import com.example.petstagram.visualizarcategoria.VisualizarCategoria

class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            PetstagramConLogicaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "login"){
                        composable("login"){
                            LoginEnMovil(navController = navController)
                        }
                        composable("categorias"){
                            MenuPrincipal(navController = navController)
                        }
                        composable("publicaciones"){
                            VisualizarCategoria(navController = navController)
                        }
                        composable("perfilAjeno"){
                            Perfil(navController = navController)
                        }
                        composable("perfilPropio"){
                            PerfilPropio(navController = navController)
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PetstagramConLogicaTheme {
        Greeting("Android")
    }
}