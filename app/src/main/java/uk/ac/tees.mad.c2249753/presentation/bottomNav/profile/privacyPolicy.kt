package uk.ac.tees.mad.c2249753.presentation.bottomNav.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrivacyPolicyPage(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Privacy Policy",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Policy Content
        Text(
            text = """
                We value your privacy. This Privacy Policy explains how we collect, use, and protect your information:
                
                1. **Information Collection**:
                   - We collect information you provide, such as your name, email, and profile details.
                   - We may collect data on app usage to improve our services.

                2. **Use of Information**:
                   - Your data is used to provide personalized experiences and improve our app.
                   - We may send notifications related to app updates and features.

                3. **Data Sharing**:
                   - We do not share your personal data with third parties without your consent, except as required by law.

                4. **Data Security**:
                   - We take reasonable measures to protect your data from unauthorized access.
                   - Please use strong passwords and secure devices to enhance your privacy.

                5. **Your Rights**:
                   - You can update or delete your information at any time from the app settings.
                   - You can contact us to request data deletion or updates.

                6. **Changes to Policy**:
                   - This Privacy Policy may be updated. Check this page regularly for changes.
                   
                For questions or concerns about your privacy, please contact us at support@example.com.
            """.trimIndent(),
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Back Button
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBB296))
        ) {
            Text("Back", color = Color.White)
        }
    }
}
