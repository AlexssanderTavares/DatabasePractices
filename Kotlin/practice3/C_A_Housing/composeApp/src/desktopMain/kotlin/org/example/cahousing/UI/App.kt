package org.example.cahousing.UI

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.cahousing.DataSource.Models.Dept
import org.example.cahousing.ViewModels.DepartmentsViewModel
import java.sql.SQLException

@Composable
@Preview
fun App() {
    var displayDepartments: Boolean by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.width(300.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var buttonText: String by remember { mutableStateOf("Show Menu") }
            var enableSideMenu: Boolean by remember { mutableStateOf(false) }
            if (enableSideMenu) {
                buttonText = "Hide Menu"
            } else {
                buttonText = "Display Menu"
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (enableSideMenu) {
                        println("Changing Side Menu button state: ${enableSideMenu}")
                        enableSideMenu = false
                    } else {
                        println("Changing Side Menu button state: ${enableSideMenu}")
                        enableSideMenu = true
                    }
                }
            ) {
                Text(text = buttonText)
            }
            AnimatedVisibility(
                visible = enableSideMenu,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    MaterialTheme {

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                if(displayDepartments){
                                    displayDepartments = false
                                } else{
                                    displayDepartments = true
                                }
                            }
                        ) {
                            Text(text = "Departments")
                        }

                        Spacer(Modifier.width(18.dp))

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                displayDepartments = false
                            }
                        ) {
                            Text(text = "Projects")
                        }

                        Spacer(Modifier.width(18.dp))

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                displayDepartments = false

                            }
                        ) {
                            Text(text = "Employees")
                        }

                        Spacer(Modifier.width(18.dp))

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                displayDepartments = false

                            }
                        ) {
                            Text(text = "Contracts")
                        }

                        Spacer(Modifier.width(18.dp))

                        Button(
                            modifier = Modifier,
                            onClick = {
                                
                            }
                        ) {
                            Text(text = "Logout")
                        }
                    }
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = displayDepartments,
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    DepartmentsView(displayDepartments)
                }
            }
        }
    }
}







