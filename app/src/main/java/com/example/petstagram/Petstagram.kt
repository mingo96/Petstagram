package com.example.petstagram

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petstagram.ViewModels.AuthViewModel
import com.example.petstagram.ViewModels.CategoriesViewModel
import com.example.petstagram.ViewModels.DataFetchViewModel
import com.example.petstagram.ViewModels.PostsViewModel
import com.example.petstagram.ViewModels.OwnProfileViewModel
import com.example.petstagram.ViewModels.PetCreationViewModel
import com.example.petstagram.ViewModels.PetObserverViewModel
import com.example.petstagram.ViewModels.ProfileObserverViewModel
import com.example.petstagram.ViewModels.PublishViewModel
import com.example.petstagram.ViewModels.SavedPostsViewModel
import com.example.petstagram.loginenmovil.PhoneLogin
import com.example.petstagram.menuprincipal.CategoriesMenu
import com.example.petstagram.notifications.PetstagramNotificationService
import com.example.petstagram.perfil.SomeonesProfile
import com.example.petstagram.perfilpropio.MyProfile
import com.example.petstagram.publicar.NewPostScreen
import com.example.petstagram.ui.petstagram.LoadingScreen
import com.example.petstagram.ui.petstagram.Pets.PetCreation
import com.example.petstagram.ui.petstagram.Pets.PetProfile
import com.example.petstagram.ui.petstagram.publicacionesguardadas.SavedPosts
import com.example.petstagram.ui.theme.PetstagramConLogicaTheme
import com.example.petstagram.visualizarcategoria.DisplayCategory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay

class Petstagram : ComponentActivity() {

    /**activity that asks for notification permission*/
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
                PetstagramNotificationService.hasPermission = true


            })
        } else {
            Toast.makeText(this, "Po no hay notificaciones, shulo", Toast.LENGTH_SHORT).show()
            PetstagramNotificationService.hasPermission = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val notificationChannel= NotificationChannel(
            "petstagram_notifications",
            "Petstagram",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)


        val authViewModel: AuthViewModel by viewModels()
        val categoriesViewModel: CategoriesViewModel by viewModels()
        val publishViewModel: PublishViewModel by viewModels()
        val postsViewModel: PostsViewModel by viewModels()
        val ownProfileViewModel: OwnProfileViewModel by viewModels()
        val dataFetchViewModel: DataFetchViewModel by viewModels()
        val savedPostsViewModel: SavedPostsViewModel by viewModels()
        val petCreationViewModel: PetCreationViewModel by viewModels()
        val profileObserverViewModel: ProfileObserverViewModel by viewModels()
        val petObserverViewModel: PetObserverViewModel by viewModels()

        postsViewModel.base = dataFetchViewModel
        categoriesViewModel.base = dataFetchViewModel
        ownProfileViewModel.base = dataFetchViewModel
        savedPostsViewModel.base = dataFetchViewModel
        publishViewModel.base = dataFetchViewModel
        petCreationViewModel.base = dataFetchViewModel
        profileObserverViewModel.base = dataFetchViewModel
        petObserverViewModel.base = dataFetchViewModel

        askNotificationPermission()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            PetstagramConLogicaTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(
                        alpha = 255,
                        red = 35,
                        green = 35,
                        blue = 35
                    ),
                    contentColor = Color.White
                ) {
                    val navController = rememberNavController()

                    postsViewModel.navController = navController
                    ownProfileViewModel.navController = navController
                    savedPostsViewModel.navController = navController
                    profileObserverViewModel.navController = navController
                    petObserverViewModel.navController = navController

                    val start = if (authViewModel.auth.currentUser == null) {
                        "login"
                    } else {
                        "pantallaCarga"
                    }

                    if (start == "pantallaCarga") {
                        authViewModel.loadUserFromAuth()
                    }

                    val onEnter = slideInHorizontally { -it } + scaleIn()
                    val onExit = slideOutHorizontally {
                        it
                    } + scaleOut()

                    val focusManager = LocalFocusManager.current

                    NavHost(navController = navController, startDestination = start) {
                        composable(
                            "pantallaCarga",
                            enterTransition = { onEnter },
                            exitTransition = { onExit }) {


                            LaunchedEffect(key1 = Unit) {

                                while (authViewModel.localProfile == null) {
                                    delay(1000)
                                }
                                postsViewModel.actualUser = authViewModel.localProfile!!
                                dataFetchViewModel.selfId = authViewModel.auth.currentUser!!.uid
                                ownProfileViewModel.selfId = authViewModel.localProfile!!.id
                                dataFetchViewModel.startLoadingData(applicationContext)
                                publishViewModel.user = authViewModel.localProfile!!
                                navController.navigate("categorias")
                            }
                            focusManager.clearFocus(true)
                            LoadingScreen()

                        }
                        composable(
                            "login",
                            enterTransition = { onEnter },
                            exitTransition = { onExit }) {
                            ownProfileViewModel.clean()
                            dataFetchViewModel.clear()

                            LaunchedEffect(key1 = Unit){

                                focusManager.clearFocus(true)
                            }

                            PhoneLogin(navController = navController, viewModel = authViewModel)

                        }
                        composable(
                            "categorias",
                            enterTransition = { onEnter },
                            exitTransition = { onExit }) {

                            LaunchedEffect(key1 = true) {
                                categoriesViewModel.fetchCategories()
                            }

                            LaunchedEffect(key1 = Unit){

                                focusManager.clearFocus(true)
                            }

                            focusManager.clearFocus(true)
                            CategoriesMenu(
                                navController = navController,
                                viewModel = categoriesViewModel
                            )

                        }
                        composable(
                            "publicaciones",
                            enterTransition = { onEnter },
                            exitTransition = { onExit }) {

                            LaunchedEffect(key1 = Unit){

                                focusManager.clearFocus(true)
                            }
                            postsViewModel.statedCategory = categoriesViewModel.selectedCategory

                            focusManager.clearFocus(true)
                            DisplayCategory(
                                navController = navController,
                                viewModel = postsViewModel
                            )

                        }
                        composable(
                            "publicar",
                            enterTransition = { onEnter },
                            exitTransition = { onExit }) {

                            publishViewModel.category = categoriesViewModel.selectedCategory
                            publishViewModel.user = authViewModel.localProfile!!

                            LaunchedEffect(key1 = Unit){

                                focusManager.clearFocus(true)
                            }
                            focusManager.clearFocus(true)
                            NewPostScreen(
                                navController = navController,
                                viewModel = publishViewModel
                            )

                        }
                        composable(
                            "perfilPropio",
                            enterTransition = { onEnter },
                            exitTransition = { onExit }) {
                            ownProfileViewModel.selfId = authViewModel.localProfile!!.id

                            LaunchedEffect(key1 = Unit){

                                focusManager.clearFocus(true)
                            }
                            focusManager.clearFocus(true)
                            MyProfile(
                                navController = navController,
                                viewModel = ownProfileViewModel
                            )
                        }
                        composable(
                            "perfilAjeno",
                            enterTransition = { onEnter },
                            exitTransition = { onExit }) {

                            LaunchedEffect(key1 = Unit){

                                focusManager.clearFocus(true)
                            }
                            focusManager.clearFocus(true)
                            profileObserverViewModel.selfId = authViewModel.localProfile!!.id

                            SomeonesProfile(
                                navController = navController,
                                viewModel = profileObserverViewModel
                            )
                        }
                        composable(
                            "mascota",
                            enterTransition = { onEnter },
                            exitTransition = { onExit }) {

                            LaunchedEffect(key1 = Unit){

                                focusManager.clearFocus(true)
                            }
                            focusManager.clearFocus(true)
                            petObserverViewModel.selfId = authViewModel.localProfile!!.id

                            PetProfile(
                                navController = navController,
                                viewModel = petObserverViewModel
                            )
                        }
                        composable(
                            "añadirMascota",
                            enterTransition = { onEnter },
                            exitTransition = { onExit }) {
                            petCreationViewModel.selfId = authViewModel.localProfile!!.id

                            petCreationViewModel.selectedCategory = publishViewModel.category

                            LaunchedEffect(key1 = Unit){

                                focusManager.clearFocus(true)
                            }
                            focusManager.clearFocus(true)
                            PetCreation(
                                viewModel = petCreationViewModel,
                                navController = navController
                            )
                        }
                        composable(
                            "guardadas",
                            enterTransition = { onEnter },
                            exitTransition = { onExit }) {
                            savedPostsViewModel.actualUser = authViewModel.localProfile!!

                            LaunchedEffect(key1 = Unit){

                                focusManager.clearFocus(true)
                            }
                            focusManager.clearFocus(true)
                            SavedPosts(
                                navController = navController,
                                viewModel = savedPostsViewModel
                            )

                        }
                    }

                }
            }

        }

    }


    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {

            PetstagramNotificationService.hasPermission = true
            // FCM SDK (and your app) can post notifications.
        } else {
            // Directly ask for the permission
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
