package org.example.cahousing.UI

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ErrorDialog(title: String, errorMessage: String, visible: Boolean) {
    var display: Boolean by remember { mutableStateOf(visible) }
    AnimatedVisibility(visible = display) {
        AlertDialog(
            onDismissRequest = { display = false },
            title = { Text(text = title) },
            text = { Text(text = errorMessage) },
            confirmButton = {
                Button(onClick = { display = false }) {
                    Text(text = "OK")
                }
            }
        )
    }
}

@Composable
fun UIToastNotification(message: String, visible: Boolean, duration: Long){
    var visibilityState: Boolean by remember { mutableStateOf(visible) }
    println("Showing ${message} notification: ${visibilityState}")
    AnimatedVisibility(
        enter = fadeIn() + scaleIn(),
        visible = visibilityState
    ){
        Card(backgroundColor = Color.Yellow){
            Row(
                modifier = Modifier.border(width = 3.dp, color = Color.Red, shape = RoundedCornerShape(6.dp)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = message.uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
        }
    }

    CoroutineScope(Dispatchers.Unconfined).launch {
        delay(duration)
        visibilityState = false
        println("Dismissing ${message} notification: ${visibilityState}")
    }
}