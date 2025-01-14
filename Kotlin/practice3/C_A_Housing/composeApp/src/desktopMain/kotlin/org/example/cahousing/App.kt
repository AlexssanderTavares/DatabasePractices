package org.example.cahousing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.FloatingActionButtonElevation
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragData
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import c_a_housing.composeapp.generated.resources.Res
import c_a_housing.composeapp.generated.resources.compose_multiplatform


var displayMenuButton: Boolean = false

@Composable
@Preview
fun App() {
    SideMenu()
}


@Composable
fun SideMenu() {
    var visibility: Boolean by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier.width(300.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var buttonText: String by remember { mutableStateOf("Show Menu") }
        Button(
            modifier = Modifier.background(Color.Red).fillMaxWidth(),
            onClick = {
                if (!visibility) {
                    visibility = true
                    buttonText = "Hide Menu"
                } else {
                    visibility = false
                    buttonText = "Show Menu"
                }
            }
        ) {
            Text(text = buttonText)
        }
        AnimatedVisibility(
            visible = visibility,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.Red),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                MaterialTheme {
                    //enable button interactions
                    var lockDepartmentBtn by remember { mutableStateOf(true) }
                    var lockProjectsBtn by remember { mutableStateOf(true) }
                    var lockEmployeeBtn by remember { mutableStateOf(true) }
                    var lockContractsBtn by remember { mutableStateOf(true) }

                    Button(
                        enabled = lockDepartmentBtn,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            lockDepartmentBtn = false
                            lockProjectsBtn = true
                            lockEmployeeBtn = true
                            lockContractsBtn = true
                        }
                    ) {
                        Text(text = "Departments")
                    }

                    Spacer(Modifier.width(18.dp))

                    Button(
                        enabled = lockProjectsBtn,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            lockDepartmentBtn = true
                            lockProjectsBtn = false
                            lockEmployeeBtn = true
                            lockContractsBtn = true
                        }
                    ) {
                        Text(text = "Projects")
                    }

                    Spacer(Modifier.width(18.dp))

                    Button(
                        enabled = lockEmployeeBtn,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            lockDepartmentBtn = true
                            lockProjectsBtn = true
                            lockEmployeeBtn = false
                            lockContractsBtn = true
                        }
                    ) {
                        Text(text = "Employees")
                    }

                    Spacer(Modifier.width(18.dp))

                    Button(
                        enabled = lockContractsBtn,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            lockDepartmentBtn = true
                            lockProjectsBtn = true
                            lockEmployeeBtn = true
                            lockContractsBtn = false
                        }
                    ) {
                        Text(text = "Contracts")
                    }

                    Spacer(Modifier.width(18.dp))

                    Button(
                        modifier = Modifier,
                        onClick = {}
                    ) {
                        Text(text = "Logout")
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayDepartments(visibility: Boolean){
    var visible: Boolean by remember { mutableStateOf(false) }
    if(visibility){
        visible = true
        AnimatedVisibility(
            visible = visible,
            ){
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                LazyColumn {

                }
            }
        }

    }

}



