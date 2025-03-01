package org.example.cahousing.UI

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun App() {
    var displayDepartments: Boolean by remember { mutableStateOf(false) }
    var displayProjects: Boolean by remember { mutableStateOf(false) }

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
                                    displayProjects = false
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
                                displayProjects = true
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
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkOut()
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    DepartmentsView(displayDepartments)
                }
            }

            AnimatedVisibility(
                visible = displayProjects,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkOut()
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    ProjectView(displayProjects)
                }
            }
        }
    }
}







