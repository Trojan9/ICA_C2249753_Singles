package uk.ac.tees.mad.c2249753

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import uk.ac.tees.mad.c2249753.presentation.splash.SplashSequence
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        // Retrieve extras from the intent
        val userName = intent.getStringExtra("userName")
        val chatId = intent.getStringExtra("chatId")

        setContent {
            SplashSequence(userName = userName, chatId = chatId)
        }
    }
}














