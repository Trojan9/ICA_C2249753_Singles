package uk.ac.tees.mad.c2249753.presentation.bottomNav.chats



import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(
    inputMessage: String,
    onInputChanged: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = inputMessage,
            onValueChange = onInputChanged,
            placeholder = { Text("Type a message...") },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.outlinedTextFieldColors()
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onSendMessage) {
            Text("Send")
        }
    }
}