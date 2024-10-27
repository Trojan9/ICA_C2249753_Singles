package com.example.singles.presentation.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.singles.R
import com.example.singles.presentation.onboarding.Page
import com.example.singles.presentation.onboarding.pages
import com.example.singles.ui.theme.SinglesTheme
import kotlinx.coroutines.launch

@Composable
fun OnboardingPage(
    modifier: Modifier = Modifier,
    page: Page,
    pagerState:PagerState,
    buttonsState: State<List<String>>,
    onGetStartedClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = page.image), // Replace with your background image
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp)) // Space for top padding
            // Title
            Text(
                text = "Hot Singles",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFBB296) // Use the color code you mentioned
            )

            // Dots Indicator
            Row(
                modifier = Modifier.padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DotIndicator(isSelected = pagerState.currentPage==0)
                DotIndicator(isSelected = pagerState.currentPage==1)
                DotIndicator(isSelected = pagerState.currentPage==2)
            }

            // Description Text
            Text(
                text = "Hot singles looking to link up with their taste, join now to meet up with various people.",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 32.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Get Started Button
            Button(
                onClick = onGetStartedClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBB296)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp)
            ) {
                Text(text = "Get Started", color = Color.White)
            }

            // Bottom Buttons (Skip and Next)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),

                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val scope = rememberCoroutineScope()
                if (buttonsState.value[0].isNotEmpty()) {

                TextButton(  onClick = {

                    scope.launch {
                        pagerState.animateScrollToPage(
                            page = pagerState.currentPage - 1
                        )
                    }


                }) {
                    Text(text  = buttonsState.value[0], color = Color.White)
                }
            }else{
                Text(text = "")
            }
                TextButton(onClick = { scope.launch {
                    if (pagerState.currentPage == 2){
                        onGetStartedClick()
                        }else{
                        pagerState.animateScrollToPage(
                            page = pagerState.currentPage + 1
                        )
                    }
                }}) {
                    Text(text = buttonsState.value[1], color = Color.White)
                }
            }
        }
    }
}

@Composable
fun DotIndicator(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(if (isSelected) Color(0xFFFBB296) else Color.White)
            .padding(2.dp)
    )
}

