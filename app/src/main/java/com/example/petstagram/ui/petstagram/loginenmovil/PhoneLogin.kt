package com.example.petstagram.loginenmovil

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.petstagram.R
import com.example.petstagram.ViewModels.AuthViewModel
import com.example.petstagram.cuadrotexto.Label
import com.example.petstagram.cuadrotexto.Variation
import com.example.petstagram.cuadrotexto.inter
import com.example.petstagram.data.AuthUiState
import com.example.petstagram.perfilpropio.StateSelector
import com.example.petstagram.ui.theme.Primary
import com.example.petstagram.ui.theme.Secondary
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayImage
import com.google.relay.compose.RelayVector

/**
 * login, interactuates with [viewModel] to get the user authenticated
 * @param viewModel controller class for auth
 */
@Composable
fun PhoneLogin(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    BackHandler {

    }

    val userValue: () -> String = { viewModel.user }

    val userOnChange: (String) -> Unit = { viewModel.userTextChange(it) }

    val passwordValue: () -> String = { viewModel.password }

    val passwordOnChange: (String) -> Unit = { viewModel.passwordTextChange(it) }

    val context =
        LocalContext.current.applicationContext

    //state of the authentication
    val state by viewModel.state.observeAsState()

    val registering by viewModel.registering.observeAsState(true)

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                viewModel.signInWithGoogleCredential(credential, {
                    navController.navigate("categorias")
                }) {

                    navController.navigate("añadirMascota")
                }
            } catch (e: Exception) {
                Toast.makeText(context, "AYUDA", Toast.LENGTH_SHORT).show()
            }

        }

    val onGoogleClick = {
        val token = "750182229870-5m2rv6tlkg0j97n0jjoc5fpqd345rssg.apps.googleusercontent.com"
        val options = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken(token)
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, options)
        googleSignInClient.signOut()
        launcher.launch(googleSignInClient.signInIntent)

    }


    if (state == AuthUiState.IsLoading) {
        Dialog(onDismissRequest = { }) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(text = "Procesando")
                LinearProgressIndicator(color = Primary, modifier = Modifier.fillMaxWidth(0.8f))
            }
        }
    }

    BoxWithConstraints(
        Modifier.background(
            Color(
                alpha = 255,
                red = 35,
                green = 35,
                blue = 35
            )
        )
    ) {
        val statedWidth = maxWidth
        val statedHeight = maxHeight

        TopLevel(modifier = Modifier.fillMaxSize()) {

            Box {
                AnimatedVisibility(visible = registering,
                    enter = slideInHorizontally { it },
                    exit = slideOutHorizontally { it }) {
                    WelcomeText(
                        Modifier.height(statedHeight.times(0.16f)),
                        "Bienvenido a Petstagram!"
                    )
                }
                AnimatedVisibility(visible = !registering,
                    enter = slideInHorizontally { -it },
                    exit = slideOutHorizontally { -it }) {
                    WelcomeText(Modifier.height(statedHeight.times(0.16f)), "Hola de nuevo!")
                }
            }

            StateSelector(
                state = !registering,
                onClick = { viewModel.toggleAuthType() },
                text1 = "Iniciar sesión",
                text2 = "Registrarse"
            )

            DataFields(
                userValue = userValue,
                userOnChange = userOnChange,
                passwordOnChange = passwordOnChange,
                passwordValue = passwordValue
            )

            Box {

                AnimatedVisibility(
                    visible = registering,
                    enter = slideInHorizontally { it },
                    exit = slideOutHorizontally { it }) {

                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                        SignInButtons(
                            onGoogleButtonClick = onGoogleClick,
                            registering = true,
                            viewModel = viewModel,
                            navController = navController
                        )

                        val helpDisplayed by viewModel.helpDisplayed.observeAsState(false)
                        AnimatedVisibility(visible = helpDisplayed) {
                            Column(
                                Modifier
                                    .border(
                                        BorderStroke(2.dp, Color.White),
                                        RoundedCornerShape(5.dp)
                                    )
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "Tu nombre de usuario será tu prefijo de correo, si ya está pillado lo modificaremos un poco, pero luego lo puedes cambiar!",
                                    color = Color.White
                                )
                                Text(
                                    text = "¡Al registrarte irás a registrar a tu primera mascota así que ve preparando alguna foto en la que salga guapa!",
                                    color = Color.White
                                )
                            }
                        }
                    }


                }
                AnimatedVisibility(
                    visible = !registering,
                    enter = slideInHorizontally { -it },
                    exit = slideOutHorizontally { -it }
                ) {
                    SignInButtons(
                        onGoogleButtonClick = onGoogleClick,
                        registering = false,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }


        }
    }
}

@Composable
fun DataFields(
    userValue: () -> String,
    userOnChange: (String) -> Unit,
    passwordOnChange: (String) -> Unit,
    passwordValue: () -> String
) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Column {

            Row {
                Text(
                    text = "Introduce tu correo",
                    style = myTextStyle(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                )
            }
            UserText(
                textAccess = userValue,
                changeText = userOnChange
            )
        }
        Column {

            Text(
                text = "Introduce tu contraseña",
                style = myTextStyle(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            )
            PasswordText(
                textAccess = passwordValue,
                changeText = passwordOnChange
            )
        }
    }
}

@Composable
fun SignInButtons(
    onGoogleButtonClick: () -> Unit,
    registering: Boolean,
    viewModel: AuthViewModel,
    navController: NavHostController
) {


    val context = LocalContext.current
    Row(
        Modifier
            .fillMaxWidth()
            .height(40.dp),
        horizontalArrangement = Arrangement.spacedBy(
            8.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        GoogleAuthButton(
            onClick = { onGoogleButtonClick() }
        )
        if (registering) {

            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "info",
                Modifier
                    .clickable { viewModel.clickHelp() }
                    .size(40.dp)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.cheque),
            contentDescription = "Send",
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    if (registering) {
                        viewModel.register(
                            context = context
                        ) {
                            navController.navigate("categorias")
                            navController.navigate("añadirMascota")
                        }
                    } else {
                        viewModel.login(
                            context = context
                        ) { navController.navigate("categorias") }
                    }
                }
        )
    }
}

@Composable
fun myTextStyle(): TextStyle {
    return TextStyle(
        color = Secondary,
        fontSize = 20.sp,
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun GoogleAuthButton(onClick: () -> Unit, text: String = "Iniciar con Google") {

    Row(
        Modifier
            .clickable { onClick() }
            .background(Color.White, RoundedCornerShape(100))
            .fillMaxHeight()
            .fillMaxWidth(0.7f)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(painter = painterResource(id = R.drawable.google), contentDescription = "google")
        Text(text = text, color = Color.Black, style = TextStyle(fontSize = 14.sp))
    }

}

@Composable
fun LoginImage(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.login_image),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .padding(
                paddingValues = PaddingValues(
                    start = 0.0.dp,
                    top = 0.0.dp,
                    end = 0.0.dp,
                    bottom = 484.0.dp
                )
            )
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}

@Composable
fun DiagonalRectangle(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.diagonal_rectangle),
        modifier = modifier
            .padding(
                paddingValues = PaddingValues(
                    start = 0.0.dp,
                    top = 206.5.dp,
                    end = 0.0.dp,
                    bottom = 480.0.dp
                )
            )
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}

@Composable
fun WelcomeText(modifier: Modifier = Modifier, text: String = "") {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val borderSize = 1.dp.toPx()
                drawLine(
                    color = Secondary,
                    start = Offset(x = 0f, y = size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = borderSize
                )
            }
    ) {

        Text(
            text = text,
            style = TextStyle(fontSize = 35.sp, color = Secondary, textAlign = TextAlign.Center)
        )

    }
}

/**just some in-out text field, not too much to see (logic wise)*/
@Composable
fun UserText(
    modifier: Modifier = Modifier,
    textAccess: () -> String,
    changeText: (String) -> Unit
) {
    TextField(
        label = { Text("Correo", color = Primary) },
        value = textAccess.invoke(),
        onValueChange = changeText,
        textStyle = TextStyle(
            fontSize = 12.0.sp,
            fontFamily = inter,
            lineHeight = 1.2102272033691406.em,
            color = Primary
        ),
        colors = myTextFieldColors(),
        singleLine = true,
        shape = RoundedCornerShape(10),
        modifier = modifier
            .fillMaxWidth(1.0f)
            .wrapContentHeight(
                align = Alignment.CenterVertically,
                unbounded = true
            )
    )
}

/**just some in-out text field, password formatted, not too much to see (logic wise)*/
@Composable
fun PasswordText(
    modifier: Modifier = Modifier,
    textAccess: () -> String,
    changeText: (String) -> Unit
) {
    TextField(
        label = { Text("Contraseña", color = Primary) },
        value = textAccess.invoke(),
        onValueChange = changeText,
        textStyle = TextStyle(
            fontSize = 12.0.sp,
            fontFamily = inter,
            lineHeight = 1.2102272033691406.em,
            color = Primary
        ),
        colors = myTextFieldColors(),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .wrapContentHeight(
                align = Alignment.CenterVertically,
                unbounded = true
            ),
        shape = RoundedCornerShape(10),
        visualTransformation = PasswordVisualTransformation('*'),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

@Composable
fun myTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors(
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Primary,
        unfocusedIndicatorColor = Primary,
        focusedTextColor = Primary,
        unfocusedTextColor = Primary
    )
}

/**just some ouput text, not too much to see (logic wise)*/
@Composable
fun RegisterTextButton(modifier: Modifier = Modifier) {
    Label(
        modifier = modifier.fillMaxWidth(1.0f),
        variation = Variation.Register
    )
}

/**just some ouput text, not too much to see (logic wise)*/
@Composable
fun LogInTextButton(modifier: Modifier = Modifier) {
    Label(
        modifier = modifier.fillMaxWidth(1.0f),
        variation = Variation.Login
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
        itemSpacing = 24.0,
        isStructured = true,
        arrangement = RelayContainerArrangement.Column,
        mainAxisAlignment = MainAxisAlignment.Start,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
            .padding(vertical = 24.dp, horizontal = 16.dp)
    )
}
