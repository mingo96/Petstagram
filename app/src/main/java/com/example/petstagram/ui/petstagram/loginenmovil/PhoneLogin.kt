package com.example.petstagram.loginenmovil

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.petstagram.R
import com.example.petstagram.ViewModels.AuthViewModel
import com.example.petstagram.cuadrotexto.Label
import com.example.petstagram.cuadrotexto.Variation
import com.example.petstagram.cuadrotexto.inter
import com.example.petstagram.data.AuthUiState
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayImage
import com.google.relay.compose.RelayVector

/**
 * login
 *
 * This composable was generated from the UI Package 'login_en_movil'.
 * Generated code; do not edit directly
 */
@Composable
fun PhoneLogin(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: AuthViewModel
) {


    val userValue: () -> String = { viewModel.user }

    val userOnChange: (String) -> Unit = { viewModel.userTextChange(it) }

    val passwordValue: () -> String = { viewModel.password }

    val passwordOnChange: (String) -> Unit = { viewModel.passwordTextChange(it) }

    val context =
        LocalContext.current.applicationContext

    val state by viewModel.state.observeAsState()


    BoxWithConstraints {
        val statedWidth = maxWidth

        TopLevel(modifier = Modifier.fillMaxSize()) {
            LoginImage(
                modifier = Modifier
                    .rowWeight(1.0f)
                    .columnWeight(1.0f)
            )
            DiagonalRectangle(modifier = Modifier.width(statedWidth))
            TopLevelSynth(
                modifier = Modifier
                    .rowWeight(1.0f)
                    .columnWeight(1.0f)
            ) {
                WelcomeText()

                if(state == AuthUiState.IsLoading){
                    CircularProgressIndicator(modifier = Modifier
                        .size(150.dp))
                }else {
                    UserText(
                        modifier = Modifier.rowWeight(1.0f),
                        textAccess = userValue,
                        changeText = userOnChange
                    )
                    PasswordText(
                        modifier = Modifier.rowWeight(1.0f),
                        textAccess = passwordValue,
                        changeText = passwordOnChange
                    )
                }
                RegisterTextButton(
                    modifier = Modifier
                        .rowWeight(1.0f)
                        .clickable {
                            viewModel.register(
                                context = context
                            ) { navController.navigate("categorias") }
                        }
                )
                LogInTextButton(modifier = Modifier
                    .rowWeight(1.0f)
                    .clickable {
                        viewModel.login(
                            context = context
                        ) { navController.navigate("categorias") }
                    }
                )
            }
        }
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
fun WelcomeText(modifier: Modifier = Modifier) {
    Label(
        modifier = modifier.fillMaxWidth(),
        variation = Variation.Petstagram
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserText(
    modifier: Modifier = Modifier,
    textAccess: () -> String,
    changeText: (String) -> Unit
) {
    OutlinedTextField(
        value = textAccess.invoke(),
        onValueChange = changeText,
        textStyle = TextStyle(
            fontSize = 20.0.sp,
            fontFamily = inter,
            lineHeight = 1.2102272033691406.em,
            color = Color.Black
        ),
        shape = RoundedCornerShape(10),
        modifier = modifier
            .fillMaxWidth(1.0f)
            .wrapContentHeight(
                align = Alignment.CenterVertically,
                unbounded = true
            )
            .border(
                width = 2.dp,
                shape = RoundedCornerShape(10.dp),
                color = Color(
                    alpha = 38,
                    red = 0,
                    green = 0,
                    blue = 0
                )
            )
            .background(
                Color(
                    alpha = 255,
                    red = 225,
                    green = 196,
                    blue = 1
                ),
                shape = RoundedCornerShape(10)
            )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordText(
    modifier: Modifier = Modifier,
    textAccess: () -> String,
    changeText: (String) -> Unit
) {
    OutlinedTextField(
        value = textAccess.invoke(),
        onValueChange = changeText,
        textStyle = TextStyle(
            fontSize = 20.0.sp,
            fontFamily = inter,
            lineHeight = 1.2102272033691406.em,
            color = Color.Black
        ),
        modifier = modifier
            .fillMaxWidth(1.0f)
            .wrapContentHeight(
                align = Alignment.CenterVertically,
                unbounded = true
            )
            .border(
                width = 2.dp,
                shape = RoundedCornerShape(10.dp),
                color = Color(
                    alpha = 38,
                    red = 0,
                    green = 0,
                    blue = 0
                )
            )
            .background(
                Color(
                    alpha = 255,
                    red = 225,
                    green = 196,
                    blue = 1
                ),
                shape = RoundedCornerShape(10),
            ),
        shape = RoundedCornerShape(10),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

@Composable
fun RegisterTextButton(modifier: Modifier = Modifier) {
    Label(
        modifier = modifier.fillMaxWidth(1.0f),
        variation = Variation.Register
    )
}

@Composable
fun LogInTextButton(modifier: Modifier = Modifier) {
    Label(
        modifier = modifier.fillMaxWidth(1.0f),
        variation = Variation.Login
    )
}

@Composable
fun TopLevelSynth(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        mainAxisAlignment = MainAxisAlignment.SpaceBetween,
        padding = PaddingValues(
            start = 24.0.dp,
            top = 200.0.dp,
            end = 24.0.dp,
            bottom = 70.0.dp
        ),
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
            .alpha(alpha = 100.0f)
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
        isStructured = false,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
    )
}
