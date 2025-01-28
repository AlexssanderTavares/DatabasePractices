package org.example.cahousing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.text.TextLayoutInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import c_a_housing.composeapp.generated.resources.Res
import c_a_housing.composeapp.generated.resources.deleteicon
import c_a_housing.composeapp.generated.resources.updateicon
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import org.example.cahousing.DataSource.Models.Dept
import org.example.cahousing.ViewModels.AppViewModel
import org.example.cahousing.ViewModels.DepartmentsViewModel

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
                modifier = Modifier.background(Color.Red).fillMaxWidth(),
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
                    modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.Red),
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

                            }
                        ) {
                            Text(text = "Projects")
                        }

                        Spacer(Modifier.width(18.dp))

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {

                            }
                        ) {
                            Text(text = "Employees")
                        }

                        Spacer(Modifier.width(18.dp))

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {

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

@Composable
fun DepartmentsView(visible: Boolean) {
    val deptViewModel: DepartmentsViewModel = viewModel()
    deptViewModel.getDeptList()
    val updatedList: ArrayList<Dept> by deptViewModel.deptList.collectAsState()

    AnimatedVisibility(
        visible = visible
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.Blue),
            verticalArrangement = Arrangement.Center,
        ) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(items = updatedList, itemContent = {
                    Spacer(Modifier.height(8.dp))
                    DeptItem(dept = it)
                })
            }
        }
    }
}

@Composable
fun DeptItem(dept: Dept) {
    Row(
        modifier = Modifier.border(4.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .padding(4.dp).background(Color.White).fillMaxSize().height(56.dp)
            .clickable(onClick = {}),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "Department: ${dept.name}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            text = "Number: ${dept.id}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            text = "Description: ${dept.description}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(18.dp))


    }
    //DeleteUpdateButtons(dept)
}
/*
@Composable
fun DeleteUpdateButtons(dept: Dept){
    val viewModel = DepartmentsViewModel()
    var showUpdateForm: Boolean by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        FloatingActionButton(
            modifier = Modifier.width(48.dp).height(48.dp).padding(8.dp),
            onClick = {
                viewModel.delete(dept)
            }
        ){
            Icon(painter = painterResource(Res.drawable.deleteicon), contentDescription = "")
        }

        FloatingActionButton(
            modifier = Modifier.width(48.dp).height(48.dp).padding(8.dp),
            onClick = {
                if(!showUpdateForm) {
                    showUpdateForm = true
                    println("Opening Update Form...$showUpdateForm")
                }
            }
        ){
            Icon(painter = painterResource(Res.drawable.updateicon), contentDescription = "")
        }
    }

    if(showUpdateForm){
        AnimatedVisibility(
            visible = showUpdateForm
        ) {

            UpdateForm(showUpdateForm, dept)
        }
    }
}*/
/*

@Composable
fun UpdateForm(visibility: Boolean, dept: Dept) {
    val viewModel = DepartmentsViewModel()
    var visible: Boolean by remember { mutableStateOf(visibility) }
    var newDeptName: String by remember { mutableStateOf(dept.name) }
    var newDeptDescription: String by remember { mutableStateOf(dept.description) }
    var displayErrorDialog: Boolean by remember { mutableStateOf(false) }
    lateinit var newDept: Dept

    AnimatedVisibility(
        visible = visible){
        Box(
            modifier = Modifier.height(500.dp).width(500.dp),
        ) {
            Column(
                modifier = Modifier.height(500.dp).width(500.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = newDeptName,
                    onValueChange = { newDeptName = it },
                    label = { Text(text = "New Department Name: ") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = newDeptDescription,
                    onValueChange = { newDeptDescription = it },
                    label = { Text(text = "New Department Description: ") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            newDept = Dept(name = newDeptName, description = newDeptDescription)
                            viewModel.updateDept(dept, newDept)

                            if (viewModel.updateResult.value == 1 || viewModel.updateResult.value == 2) {
                                visible = false
                            } else {
                                displayErrorDialog = true
                            }
                        }
                    ) {
                        Text(text = "Update")
                    }

                    Button(
                        onClick = {
                            visible = false
                        }
                    ) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
        if(displayErrorDialog){
            AnimatedVisibility(
                visible = displayErrorDialog
            ) {
                ErrorDialog("Update Error", "Unable to update data due to an error.", displayErrorDialog)
            }
        }

    }
}
*/

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





