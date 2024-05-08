package com.example.petstagram

import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petstagram.ViewModels.AuthViewModel
import com.example.petstagram.ViewModels.CategoriesViewModel
import com.example.petstagram.ViewModels.DataFetchViewModel
import com.example.petstagram.ViewModels.PostsViewModel
import com.example.petstagram.ViewModels.OwnProfileViewModel
import com.example.petstagram.ViewModels.PublishViewModel
import com.example.petstagram.ViewModels.SavedPostsViewModel
import com.example.petstagram.loginenmovil.PhoneLogin
import com.example.petstagram.menuprincipal.CategoriesMenu
import com.example.petstagram.perfilpropio.MyProfile
import com.example.petstagram.publicar.NewPostScreen
import com.example.petstagram.ui.petstagram.publicacionesguardadas.SavedPosts
import com.example.petstagram.ui.theme.PetstagramConLogicaTheme
import com.example.petstagram.visualizarcategoria.DisplayCategory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.i("Fetching FCM registration token failed", task.exception.toString())
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result


            })
        } else {
            Toast.makeText(this,"Po no hay notificaciones, shulo", Toast.LENGTH_SHORT).show()
        }
    }
    @OptIn(UnstableApi::class) @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
        val authViewModel : AuthViewModel by viewModels()
        val categoriesViewModel : CategoriesViewModel by viewModels()
        val publishViewModel :PublishViewModel by viewModels()
        val postsViewModel : PostsViewModel by viewModels()
        val ownProfileViewModel : OwnProfileViewModel by viewModels()
        val dataFetchViewModel : DataFetchViewModel by viewModels()
        val savedPostsViewModel : SavedPostsViewModel by viewModels()


        postsViewModel.base = dataFetchViewModel
        ownProfileViewModel.base = dataFetchViewModel
        savedPostsViewModel.base = dataFetchViewModel

        askNotificationPermission()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            PetstagramConLogicaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val start = if(authViewModel.auth.currentUser == null) {
                        "login"
                    } else {
                        authViewModel.loadUserFromAuth()
                        "categorias"
                    }

                    val onEnter = slideInHorizontally ()
                    val onExit = slideOutHorizontally {
                        it
                    }

                    var lastStep : String? by rememberSaveable {
                        mutableStateOf(null)
                    }

                    NavHost(navController = navController, startDestination = start){
                        composable("login", enterTransition = { onEnter }, exitTransition = {onExit}){
                            LaunchedEffect(key1 = lastStep) {
                                if (!lastStep.isNullOrBlank()){
                                    ownProfileViewModel.clear()
                                }
                            }
                            lastStep = route
                            PhoneLogin(navController = navController, viewModel = authViewModel)

                        }
                        composable("categorias", enterTransition = { onEnter }, exitTransition = {onExit}){

                            val context = LocalContext.current
                            LaunchedEffect(key1 = Unit) {
                                postsViewModel.actualUser = authViewModel.localProfile
                                dataFetchViewModel.selfId = authViewModel.auth.currentUser!!.uid
                                dataFetchViewModel.startLoadingPosts(context)
                            }
                            lastStep = route
                            CategoriesMenu(navController = navController, viewModel = categoriesViewModel)

                        }
                        composable("publicaciones", enterTransition = { onEnter }, exitTransition = {onExit}){

                            lastStep = route
                            postsViewModel.statedCategory = categoriesViewModel.selectedCategory

                            DisplayCategory(navController = navController, viewModel = postsViewModel)

                        }
                        composable("publicar", enterTransition = { onEnter }, exitTransition = {onExit}){
                            publishViewModel.user = authViewModel.localProfile
                            publishViewModel.category = categoriesViewModel.selectedCategory
                            NewPostScreen(navController = navController , viewModel = publishViewModel)

                            lastStep = route
                        }
                        //not implemented (yet)
                        //composable("perfilAjeno"){
                        //    SomeonesProfile(navController = navController, viewModel = ownProfileViewModel)
                        //}
                        composable("perfilPropio", enterTransition = { onEnter }, exitTransition = {onExit}){

                            lastStep = route
                            ownProfileViewModel.selfId = authViewModel.localProfile.id
                            MyProfile(navController = navController, viewModel = ownProfileViewModel)

                        }
                        composable("guardadas", enterTransition = { onEnter }, exitTransition = {onExit}){
                            lastStep = route
                            savedPostsViewModel.actualUser = authViewModel.localProfile
                            SavedPosts(navController = navController, viewModel = savedPostsViewModel)

                        }
                    }

                }
            }
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // FCM SDK (and your app) can post notifications.
        } else  {
            // Directly ask for the permission
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}



@HiltAndroidApp
class ExampleApplication : Application()