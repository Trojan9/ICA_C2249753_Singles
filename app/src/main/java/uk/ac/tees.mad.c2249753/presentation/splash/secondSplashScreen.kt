package uk.ac.tees.mad.c2249753.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.c2249753.R
import uk.ac.tees.mad.c2249753.ui.theme.SinglesTheme

@Composable
fun SecondSplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Adjust the background color if needed
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_splash2), // Second image resource
            contentDescription = "Hot Singles Secondary Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.Center)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SecondSplashPreview() {
    SinglesTheme {
        SecondSplashScreen()
    }
}