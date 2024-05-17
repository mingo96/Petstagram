package com.example.petstagram.ui.petstagram.Pets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.petstagram.UiData.Pet
import com.example.petstagram.fotoperfil.ProfilePic
import com.example.petstagram.ui.theme.Primary
import com.google.relay.compose.CrossAxisAlignment
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope

@Composable
fun PetCard(modifier: Modifier = Modifier, pet: Pet, onSelect: () -> Unit = {}, selected : Boolean = false) {

    PetContainer(
        modifier
        .clickable(onClickLabel = "vas a seleccionar a ${pet.name}") { onSelect() }
    ) {

        BoxWithConstraints() {

            PhotoContainer(
                Modifier
                    .height(maxWidth)
                    .width(maxWidth)) {
                ProfilePic(url = pet.profilePic)
            }
        }

        NameContainer(selected = selected) {
            Text(text = pet.name, modifier = Modifier.padding(horizontal = 8.dp), color = if (selected)Color.Black else Color.White)
        }

    }


}

@Composable
fun PetContainer(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(

        mainAxisAlignment = MainAxisAlignment.Center,
        crossAxisAlignment = CrossAxisAlignment.Center,
        arrangement = RelayContainerArrangement.Column,
        itemSpacing = 16.0,
        radius = 20.0,
        content = content,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
            .background(
                Brush.verticalGradient(
                    0.0f to Primary,
                    1.0f to Color.Transparent
                ),
                RoundedCornerShape(15.dp)
            )
            .padding(8.dp)
    )
}

@Composable
fun PhotoContainer(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 217,
            green = 217,
            blue = 217
        ),
        isStructured = false,
        radius = 200.0,
        content = content,
        modifier = modifier
            .border(4.dp, Color.Black, RoundedCornerShape(100))
    )
}

@Composable
fun NameContainer(
    modifier: Modifier = Modifier,
    selected: Boolean,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = if (selected) Color.White else Color(
            alpha = 255,
            red = 35,
            green = 35,
            blue = 35
        ),

        mainAxisAlignment = MainAxisAlignment.Center,
        crossAxisAlignment = CrossAxisAlignment.Center,
        arrangement = RelayContainerArrangement.Column,
        content = content,
        radius = 10.0,
        modifier = modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .border(
                width = 1.0.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(40)
            )
    )
}