package com.example.petstagram.ui.petstagram.Pets

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.petstagram.UiData.Pet
import com.example.petstagram.cuadroinfo.IntersectLine
import com.example.petstagram.ui.petstagram.seccioncomentarios.BotonMas
import com.example.petstagram.ui.petstagram.seccioncomentarios.CuadroSumar
import com.google.relay.compose.CrossAxisAlignment
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerArrangement
import com.google.relay.compose.RelayContainerScope

@Composable
fun PetList(modifier :Modifier= Modifier, pets : List<Pet>, onSelect : ()->Unit, onNewPet : ()->Unit){

    PetListContainer (modifier){

        IntersectLine(
            Modifier
                .fillMaxWidth(0.9f)
                .height(8.dp))

        BotonMas(modifier = Modifier
            .clickable {
                onNewPet()
            }) {
            CuadroSumar(
                modifier = Modifier
                    .rowWeight(1.0f)
                    .columnWeight(1.0f)
            )
        }
        BoxWithConstraints(Modifier.padding(16.dp)) {

            LazyVerticalGrid(
                columns = GridCells.FixedSize(maxWidth.times(0.5f)),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(pets){pet->

                    PetCard(pet = pet)

                }
            }
        }


    }

}


@Composable
fun PetListContainer(
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
        mainAxisAlignment = MainAxisAlignment.Start,
        crossAxisAlignment = CrossAxisAlignment.Center,
        arrangement = RelayContainerArrangement.Column,
        content = content,
        itemSpacing = 8.0,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
            .border(
                width = 4.0.dp,
                color = Color(
                    alpha = 255,
                    red = 225,
                    green = 196,
                    blue = 1
                ),
                shape = RoundedCornerShape(5, 5)
            )
            .padding(top = 16.dp)
    )
}