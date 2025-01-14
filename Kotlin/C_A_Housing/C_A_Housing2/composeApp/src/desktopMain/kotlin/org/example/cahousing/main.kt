package org.example.cahousing

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "C_A_Housing2",
    ) {
        App()
    }
}