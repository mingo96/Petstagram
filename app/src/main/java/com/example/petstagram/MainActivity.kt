package com.example.petstagram

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Menu
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.petstagram.loginenmovil.LoginEnMovil
import com.example.petstagram.menuprincipal.MenuPrincipal
import com.example.petstagram.perfil.Perfil
import com.example.petstagram.perfilpropio.PerfilPropio
import com.example.petstagram.ui.petstagram.seccioncomentarios.SeccionComentarios
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
                    SeccionComentarios()
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