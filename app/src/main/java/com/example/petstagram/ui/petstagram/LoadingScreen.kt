package com.example.petstagram.ui.petstagram

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.petstagram.R
import com.example.petstagram.ui.theme.Primary

@Composable
fun LoadingScreen() {

    Column(
        Modifier
            .fillMaxSize()
            .background(
                Color(
                    alpha = 255,
                    red = 35,
                    green = 35,
                    blue = 35
                )
            ),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            Modifier.fillMaxWidth(0.5f)
        )

        LinearProgressIndicator(Modifier.fillMaxWidth(0.9f), color = Primary)


    }
}