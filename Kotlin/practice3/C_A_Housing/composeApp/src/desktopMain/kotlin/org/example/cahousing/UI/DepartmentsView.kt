package org.example.cahousing.UI

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
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
fun DepartmentsView(visible: Boolean) {
    val deptViewModel: DepartmentsViewModel = viewModel()
    val updatedList: ArrayList<Dept> by deptViewModel.deptList.collectAsState()
    var dept: Dept? by remember { mutableStateOf(null) }
    var newDeptName: String by remember { mutableStateOf("") }
    var newDeptDescription: String by remember { mutableStateOf("") }
    var displayErrorDialog: Boolean by remember { mutableStateOf(false) }
    var errorMessage: String by remember { mutableStateOf("") }
    var displayUIToast: Boolean by remember { mutableStateOf(false) }
    var toastMessage: String by remember { mutableStateOf("") }
    var invalidText: Boolean by remember { mutableStateOf(false) }


    AnimatedVisibility(
        visible = visible
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(start = 64.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    modifier = Modifier.width(400.dp) ,
                    value = newDeptName,
                    isError = invalidText,
                    onValueChange = { newDeptName = it },
                    label = { Text(text = "New Department Name: ") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    modifier = Modifier.width(400.dp).height(250.dp),
                    value = newDeptDescription,
                    isError = invalidText,
                    onValueChange = { newDeptDescription = it },
                    label = { Text(text = "New Department Description: ") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        modifier = Modifier.padding(8.dp),
                        onClick = {
                            try {
                                dept = Dept(name = newDeptName, description = newDeptDescription)
                                deptViewModel.getDept(dept!!.name)
                                if (deptViewModel.getResult.value == null) {
                                    deptViewModel.create(dept!!)
                                    displayUIToast = true
                                    invalidText = false
                                    toastMessage = "Data Creation Success!!"
                                    newDeptName = ""
                                    newDeptDescription = ""
                                } else {
                                    displayUIToast = true
                                    invalidText = true
                                    toastMessage = "Data Already Exists!!!"
                                    newDeptName = ""
                                    newDeptDescription = ""
                                }

                            } catch (e: SQLException) {
                                displayErrorDialog = true
                                errorMessage = e.message!!
                            }
                        }
                    ) {
                        Text(text = "Create")
                    }
                    Button(
                        modifier = Modifier.padding(8.dp),
                        onClick = {
                            try {
                                dept = Dept(name = newDeptName, description = newDeptDescription)
                                if (dept?.name != deptViewModel.getResult.value?.name || dept?.description != deptViewModel.getResult.value?.description) {
                                    deptViewModel.update(deptViewModel.getResult.value!!, dept!!)
                                    displayUIToast = true
                                    invalidText = false
                                    toastMessage = "Data Update Success!!"
                                    newDeptName = ""
                                    newDeptDescription = ""
                                }
                            } catch (e: SQLException) {
                                invalidText = true
                                displayErrorDialog = true
                                errorMessage = e.message!!
                            }

                        }
                    ) {
                        Text(text = "Update")
                    }

                    Button(
                        modifier = Modifier.padding(8.dp),
                        onClick = {
                            try {
                                dept = deptViewModel.getResult.value!!
                                deptViewModel.delete(dept!!)

                                if (deptViewModel.deleteResult.value >= 1) {
                                    displayUIToast = true
                                    invalidText = false
                                    toastMessage = "Data Deletion Success!!!"
                                    newDeptName = ""
                                    newDeptDescription = ""
                                }
                            } catch (e: SQLException) {
                                displayErrorDialog = true
                                errorMessage = e.message!!
                            }
                        }
                    ) {
                        Text(text = "Delete")
                    }
                }

            }

            LazyColumn(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(items = updatedList, itemContent = {
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        DeptItem(
                            modifier = Modifier.border(
                                4.dp, Color.Black, shape = RoundedCornerShape(8.dp)
                            ).padding(4.dp).background(Color.White).height(56.dp).fillMaxWidth()
                                .clickable {
                                    newDeptName = it.name
                                    newDeptDescription = it.description
                                    deptViewModel.getDept(it.name)
                                }.animateEnterExit(enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkHorizontally()),
                            dept = it
                        )
                    }
                })
            }

        }
    }

    if (displayErrorDialog) {
        ErrorDialog("Something goes wrong!", errorMessage, displayErrorDialog)
    }

    if(displayUIToast) {
        UIToastNotification(toastMessage, displayUIToast, 5000)
    }
}

@Composable
fun DeptItem(dept: Dept, modifier: Modifier) {
    Row(
        modifier = modifier,
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
}