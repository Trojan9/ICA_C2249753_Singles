package com.example.singles.presentation.onboarding

import androidx.compose.foundation.layout.Box
import com.example.singles.presentation.onboarding.components.OnboardingPage
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.singles.ui.theme.SinglesTheme

@Composable
fun OnboardingScreen(
    onGetStartedClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val pagerState = rememberPagerState(initialPage = 0) {
                pages.size
            }
            val buttonsState = remember {
                derivedStateOf {
                    when (pagerState.currentPage) {
                        0 -> listOf("", "Next")
                        1 -> listOf("Back", "Next")
                        else -> listOf("", "")
                    }
                }
            }
            HorizontalPager(state = pagerState) { index ->
                OnboardingPage(page = pages[index],
                    pagerState = pagerState,
                    buttonsState=buttonsState,onGetStartedClick=onGetStartedClick)
            }
        }
    }
    }

