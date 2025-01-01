package uk.ac.tees.mad.c2249753.presentation.bottomNav.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SentMessage(message: String, time: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .background(Color(0xFFFBB296), shape = RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Text(text = message, color = Color.White, fontSize = 14.sp)
        }
        Text(text = time, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(end = 4.dp))
    }
}
