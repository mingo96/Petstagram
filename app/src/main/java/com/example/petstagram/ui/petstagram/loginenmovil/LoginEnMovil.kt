package com.example.petstagram.loginenmovil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.petstagram.R
import com.example.petstagram.ViewModels.ViewModelPrueba
import com.example.petstagram.cuadrotexto.CuadroTexto
import com.example.petstagram.cuadrotexto.Variacion
import com.example.petstagram.cuadrotexto.inter
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
    viewModel: ViewModelPrueba
) {
    var usuario by rememberSaveable {
        mutableStateOf("usuario@gmail.com")
    }
    var passwd by rememberSaveable {
        mutableStateOf("usuario@gmail.com")
    }

    val accesoUsuario : ()->String = {usuario}

    val cambioTextoUsuario : (String)->Unit = {usuario = it}

    val accesoPasswd : ()->String = {passwd}

    val cambioTextoPasswd : (String)->Unit = {passwd = it}

    TopLevel(modifier = modifier) {
        ImagenInicioSesion(modifier = Modifier
            .rowWeight(1.0f)
            .columnWeight(1.0f))
        RectanguloDeCorte(modifier = Modifier
            .rowWeight(1.0f)
            .columnWeight(1.0f))
        TopLevelSynth(modifier = Modifier
            .rowWeight(1.0f)
            .columnWeight(1.0f)) {
            CuadroTextoInstance()
            CuadroTextoNombreUsuario(modifier = Modifier.rowWeight(1.0f), accesoTexto = accesoUsuario, cambiarTexto = cambioTextoUsuario)
            CuadroTextoPassword(modifier = Modifier.rowWeight(1.0f), accesoTexto = accesoPasswd, cambiarTexto = cambioTextoPasswd)
            CuadroTexto3(
                modifier = Modifier
                    .rowWeight(1.0f)
                    .clickable {
                        viewModel.registrarse(
                            usuario,
                            passwd
                        ) { navController.navigate("categorias") }
                    }
            )
            CuadroTexto4(modifier = Modifier
                .rowWeight(1.0f)
                .clickable {
                    viewModel.iniciarSesion(
                        usuario,
                        passwd
                    ) { navController.navigate("categorias") }
                }
            )
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
    CuadroTexto(
        variacion = Variacion.Bienvenida,
        modifier = modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuadroTextoNombreUsuario(modifier: Modifier = Modifier, accesoTexto: ()->String, cambiarTexto: (String) -> Unit) {
    OutlinedTextField(
        value = accesoTexto.invoke(),
        onValueChange = cambiarTexto,
        textStyle = TextStyle(fontSize = 20.0.sp,
        fontFamily = inter,
        lineHeight = 1.2102272033691406.em),
        modifier = modifier
            .fillMaxWidth(1.0f)
            .wrapContentHeight(
                align = Alignment.CenterVertically,
                unbounded = true
            )
            .padding(PaddingValues(all = 8.0.dp))
            .background(
                Color(
                    alpha = 255,
                    red = 225,
                    green = 196,
                    blue = 1
                )
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
        textStyle = TextStyle(fontSize = 20.0.sp,
            fontFamily = inter,
            lineHeight = 1.2102272033691406.em),
        modifier = modifier
            .fillMaxWidth(1.0f)
            .wrapContentHeight(
                align = Alignment.CenterVertically,
                unbounded = true
            )
            .padding(PaddingValues(all = 8.0.dp))
            .background(
                Color(
                    alpha = 255,
                    red = 225,
                    green = 196,
                    blue = 1
                )
            ),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

@Composable
fun CuadroTexto3(modifier: Modifier = Modifier) {
    CuadroTexto(
        variacion = Variacion.Registro,
        modifier = modifier.fillMaxWidth(1.0f)
    )
}

@Composable
fun CuadroTexto4(modifier: Modifier = Modifier) {
    CuadroTexto(
        variacion = Variacion.InicioSesion,
        modifier = modifier.fillMaxWidth(1.0f)
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
