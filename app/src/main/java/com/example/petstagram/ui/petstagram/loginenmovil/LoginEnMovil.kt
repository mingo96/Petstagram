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
import com.example.petstagram.cuadrotexto.Variacion
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
fun LoginEnMovil(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: AuthViewModel
) {


    val accesoUsuario: () -> String = { viewModel.user }

    val cambioTextoUsuario: (String) -> Unit = { viewModel.userTextChange(it) }

    val accesoPasswd: () -> String = { viewModel.password }

    val cambioTextoPasswd: (String) -> Unit = { viewModel.passwordTextChange(it) }

    val context =
        LocalContext.current.applicationContext

    val state by viewModel.state.observeAsState()


    BoxWithConstraints {
        val maxAncho = maxWidth

        TopLevel(modifier = Modifier.fillMaxSize()) {
            ImagenInicioSesion(
                modifier = Modifier
                    .rowWeight(1.0f)
                    .columnWeight(1.0f)
            )
            RectanguloDeCorte(modifier = Modifier.width(maxAncho))
            TopLevelSynth(
                modifier = Modifier
                    .rowWeight(1.0f)
                    .columnWeight(1.0f)
            ) {
                CuadroTextoInstance()

                if(state == AuthUiState.IsLoading){
                    CircularProgressIndicator(modifier = Modifier
                        .size(150.dp))
                }else {
                    CuadroTextoNombreUsuario(
                        modifier = Modifier.rowWeight(1.0f),
                        accesoTexto = accesoUsuario,
                        cambiarTexto = cambioTextoUsuario
                    )
                    CuadroTextoPassword(
                        modifier = Modifier.rowWeight(1.0f),
                        accesoTexto = accesoPasswd,
                        cambiarTexto = cambioTextoPasswd
                    )
                }
                CuadroTexto3(
                    modifier = Modifier
                        .rowWeight(1.0f)
                        .clickable {
                            viewModel.register(
                                context = context
                            ) { navController.navigate("categorias") }
                        }
                )
                CuadroTexto4(modifier = Modifier
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
fun ImagenInicioSesion(modifier: Modifier = Modifier) {
    RelayImage(
        image = painterResource(R.drawable.login_en_movil_imagen_inicio_sesion),
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
fun RectanguloDeCorte(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.login_en_movil_rectangulo_de_corte),
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
fun CuadroTextoInstance(modifier: Modifier = Modifier) {
    Label(
        modifier = modifier.fillMaxWidth(),
        variacion = Variacion.Bienvenida
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
        value = accesoTexto.invoke(),
        onValueChange = cambiarTexto,
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
fun CuadroTextoPassword(
    modifier: Modifier = Modifier,
    accesoTexto: () -> String,
    cambiarTexto: (String) -> Unit
) {
    OutlinedTextField(
        value = accesoTexto.invoke(),
        onValueChange = cambiarTexto,
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
fun CuadroTexto3(modifier: Modifier = Modifier) {
    Label(
        modifier = modifier.fillMaxWidth(1.0f),
        variacion = Variacion.Registro
    )
}

@Composable
fun CuadroTexto4(modifier: Modifier = Modifier) {
    Label(
        modifier = modifier.fillMaxWidth(1.0f),
        variacion = Variacion.InicioSesion
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
