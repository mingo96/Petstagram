package com.example.petstagram

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.example.petstagram.ViewModels.AuthViewModel
import com.example.petstagram.ViewModels.CategoriesViewModel
import com.example.petstagram.ViewModels.PostsViewModel
import com.example.petstagram.ViewModels.OwnProfileViewModel
import com.example.petstagram.ViewModels.PublishViewModel
import com.example.petstagram.loginenmovil.PhoneLogin
import com.example.petstagram.menuprincipal.CategoriesMenu
import com.example.petstagram.perfil.MyProfile
import com.example.petstagram.perfilpropio.SomeonesProfile
import com.example.petstagram.publicar.NewPostScreen
import com.example.petstagram.ui.theme.PetstagramConLogicaTheme
import com.example.petstagram.visualizarcategoria.DisplayCategory
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
        val authViewModel : AuthViewModel by viewModels()
        val categoriesViewModel : CategoriesViewModel by viewModels()
        val publishViewModel :PublishViewModel by viewModels()
        val postsViewModel : PostsViewModel by viewModels()
        val ownProfileViewModel : OwnProfileViewModel by viewModels()
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
                            PhoneLogin(navController = navController, viewModel = authViewModel)
                        }
                        composable("categorias"){
                            categoriesViewModel.fetchCategories()
                            CategoriesMenu(navController = navController, viewModel = categoriesViewModel)
                        }
                        composable("publicaciones"){
                            postsViewModel.statedCategory = categoriesViewModel.selectedCategory
                            DisplayCategory(navController = navController, viewModel = postsViewModel)
                        }
                        composable("publicar"){
                            publishViewModel.user = authViewModel.localProfile
                            publishViewModel.category = categoriesViewModel.selectedCategory
                            NewPostScreen(navController = navController , viewModel = publishViewModel)
                        }
                        composable("perfilAjeno"){
                            MyProfile(navController = navController, viewModel = ownProfileViewModel)
                        }
                        composable("perfilPropio"){
                            ownProfileViewModel.selfId = authViewModel.localProfile.id
                            SomeonesProfile(navController = navController, viewModel = ownProfileViewModel)
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