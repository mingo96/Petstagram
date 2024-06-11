package com.example.petstagram.ui.petstagram.Pets

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.petstagram.R
import com.example.petstagram.UiData.Pet
import com.example.petstagram.ui.petstagram.seccioncomentarios.BotonMas
import com.example.petstagram.ui.petstagram.seccioncomentarios.CuadroSumar
import com.example.petstagram.ui.theme.Secondary
import com.google.relay.compose.CrossAxisAlignment
import com.google.relay.compose.MainAxisAlignment
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope

@Composable
fun PetList(
    modifier: Modifier = Modifier,
    pets: List<Pet>,
    onSelect: (Pet) -> Unit = {},
    onNewPet: (() -> Unit)? = null,
    selected: Pet? = null
) {

    PetListContainer(modifier) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(all = 16.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                if (onNewPet != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        BotonMas(modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .clickable {
                                onNewPet()
                            }) {
                            CuadroSumar(
                                modifier = Modifier
                                    .rowWeight(1.0f)
                                    .columnWeight(1.0f)
                            )
                        }
                        if (pets.isEmpty()) {
                            Text(text = "No hay mascotas!!")
                        }
                    }
                } else {
                    if (pets.isEmpty()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.grillo),
                                contentDescription = "grillo"
                            )
                            Text(text = "Parece que alguien necesita una mascota...")
                        }
                    }
                }

            }
            items(pets) { pet ->

                PetCard(pet = pet, onSelect = { onSelect(pet) }, selected = pet == selected)

            }

        }


    }

}


@Composable
fun PetListContainer(
    modifier: Modifier = Modifier, content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255, red = 35, green = 35, blue = 35
        ),
        mainAxisAlignment = MainAxisAlignment.Start,
        crossAxisAlignment = CrossAxisAlignment.Center,
        content = content,
        itemSpacing = 8.0,
        modifier = modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
            .border(
                width = 4.dp, Brush.verticalGradient(
                    0.7f to Secondary, 0.9f to Color.Transparent, 1.0f to Color(
                        alpha = 255, red = 35, green = 35, blue = 35
                    )
                ), RoundedCornerShape(15.dp, 15.dp)
            )
    )
}