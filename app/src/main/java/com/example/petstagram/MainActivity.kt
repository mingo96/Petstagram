package com.example.petstagram

import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
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
import com.example.petstagram.perfil.SomeonesProfile
import com.example.petstagram.perfilpropio.MyProfile
import com.example.petstagram.publicar.NewPostScreen
import com.example.petstagram.ui.theme.PetstagramConLogicaTheme
import com.example.petstagram.visualizarcategoria.DisplayCategory
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics
    @OptIn(UnstableApi::class) @SuppressLint("SourceLockedOrientationActivity")
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
                    val start = if(authViewModel.auth.currentUser == null) "login" else {
                        authViewModel.loadUserFromAuth()
                        "categorias"
                    }

                    val onEnter = slideInHorizontally ()
                    val onExit = slideOutHorizontally {
                        it
                    }

                    NavHost(navController = navController, startDestination = start){
                        composable("login", enterTransition = { onEnter }, exitTransition = {onExit}){
                            PhoneLogin(navController = navController, viewModel = authViewModel)

                        }
                        composable("categorias", enterTransition = { onEnter }, exitTransition = {onExit}){

                            CategoriesMenu(navController = navController, viewModel = categoriesViewModel)

                        }
                        composable("publicaciones", enterTransition = { onEnter }, exitTransition = {onExit}){

                            postsViewModel.statedCategory = categoriesViewModel.selectedCategory
                            postsViewModel.actualUser = authViewModel.localProfile
                            DisplayCategory(navController = navController, viewModel = postsViewModel)

                        }
                        composable("publicar", enterTransition = { onEnter }, exitTransition = {onExit}){
                            publishViewModel.user = authViewModel.localProfile
                            publishViewModel.category = categoriesViewModel.selectedCategory
                            NewPostScreen(navController = navController , viewModel = publishViewModel)

                        }
                        //not implemented (yet)
                        //composable("perfilAjeno"){
                        //    SomeonesProfile(navController = navController, viewModel = ownProfileViewModel)
                        //}
                        composable("perfilPropio", enterTransition = { onEnter }, exitTransition = {onExit}){
                            ownProfileViewModel.selfId = authViewModel.localProfile.id
                            MyProfile(navController = navController, viewModel = ownProfileViewModel)

                        }
                    }

                }
            }
        }
    }
}

@HiltAndroidApp
class ExampleApplication : Application()