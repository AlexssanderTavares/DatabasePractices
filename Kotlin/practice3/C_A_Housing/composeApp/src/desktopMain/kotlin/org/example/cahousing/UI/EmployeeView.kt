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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.cahousing.DataSource.Models.Dept
import org.example.cahousing.DataSource.Models.Employee
import org.example.cahousing.ViewModels.EmployeeViewModel
import java.sql.SQLException

@Composable
fun EmployeesView(visible: Boolean) {
    val empViewModel: EmployeeViewModel = viewModel()
    val updatedList: ArrayList<Employee> by empViewModel.getList.collectAsState()
    var emp: Employee? by remember { mutableStateOf(null) }
    var newEmpName: String by remember { mutableStateOf("") }
    var newEmpAddress: String by remember { mutableStateOf("") }
    var newEmpSex: String by remember { mutableStateOf("") }
    var newEmpWage: Double by remember { mutableStateOf(0.0) }
    var newEmpDept: Int by remember { mutableStateOf(0) }
    var newEmpTimeWorked: String by remember { mutableStateOf("") }
    var displayErrorDialog: Boolean by remember { mutableStateOf(false) }
    var errorMessage: String by remember { mutableStateOf("") }
    var displayUIToast: Boolean by remember { mutableStateOf(false) }
    var toastMessage: String by remember { mutableStateOf("") }
    var invalidText: Boolean by remember { mutableStateOf(false) }

    empViewModel.getList()

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
                    value = newEmpName,
                    isError = invalidText,
                    onValueChange = { newEmpName = it },
                    label = { Text(text = "New Employee Name: ") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    OutlinedTextField(
                        modifier = Modifier.width(195.dp),
                        value = newEmpWage.toString(),
                        isError = invalidText,
                        onValueChange = { newEmpWage = it.toDouble() },
                        label = { Text(text = "New Employee wage: ") }
                    )

                    Spacer(modifier = Modifier.width(10.dp))


                    OutlinedTextField(
                        modifier = Modifier.width(195.dp),
                        value = newEmpDept.toString(),
                        isError = invalidText,
                        onValueChange = { newEmpDept = it.toInt() },
                        label = { Text(text = "Department: ") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                   verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedTextField(
                        modifier = Modifier.width(100.dp),
                        value = newEmpSex,
                        isError = invalidText,
                        onValueChange = { newEmpSex = it },
                        label = { Text(text = "Sex: ") }
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedTextField(
                        modifier = Modifier.width(290.dp),
                        value = newEmpAddress,
                        isError = invalidText,
                        onValueChange = { newEmpAddress = it },
                        label = { Text(text = "New Employee address(CEP): ") }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    modifier = Modifier.width(400.dp),
                    value = newEmpTimeWorked,
                    isError = invalidText,
                    onValueChange = { newEmpTimeWorked = it },
                    label = { Text(text = "Time Worked: ") }
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
                                emp = Employee(name = newEmpName, sex = newEmpSex, wage = newEmpWage, address = newEmpAddress, timeWorked = newEmpTimeWorked, idDept = newEmpDept)
                                empViewModel.get(emp!!.name)
                                if (empViewModel.getResult.value == null) {
                                    empViewModel.create(emp!!)
                                    displayUIToast = true
                                    invalidText = false
                                    toastMessage = "Data Creation Success!!"
                                    newEmpName = ""
                                    newEmpAddress = ""
                                    newEmpSex = ""
                                    newEmpDept = 0
                                    newEmpWage = 0.0
                                    newEmpTimeWorked = ""
                                } else {
                                    displayUIToast = true
                                    invalidText = true
                                    toastMessage = "Data Already Exists!!!"
                                    newEmpName = ""
                                    newEmpAddress = ""
                                    newEmpSex = ""
                                    newEmpDept = 0
                                    newEmpWage = 0.0
                                    newEmpTimeWorked = ""
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
                                emp = Employee(name = newEmpName, sex = newEmpSex, wage = newEmpWage, address = newEmpAddress, timeWorked = newEmpTimeWorked, idDept = newEmpDept)
                                if (emp?.name == empViewModel.getResult.value?.name) {
                                    empViewModel.update(empViewModel.getResult.value!!, emp!!)
                                    displayUIToast = true
                                    invalidText = false
                                    toastMessage = "Data Update Success!!"
                                    newEmpName = ""
                                    newEmpAddress = ""
                                    newEmpSex = ""
                                    newEmpDept = 0
                                    newEmpWage = 0.0
                                    newEmpTimeWorked = ""
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
                                emp = empViewModel.getResult.value!!
                                empViewModel.delete(emp!!)

                                if (empViewModel.deleteResult.value >= 1) {
                                    displayUIToast = true
                                    invalidText = false
                                    toastMessage = "Data Deletion Success!!!"
                                    newEmpName = ""
                                    newEmpAddress = ""
                                    newEmpSex = ""
                                    newEmpDept = 0
                                    newEmpWage = 0.0
                                    newEmpTimeWorked = ""
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
                        EmployeeItem(
                            modifier = Modifier.border(
                                4.dp, Color.Black, shape = RoundedCornerShape(8.dp)
                            ).padding(4.dp).background(Color.White).height(56.dp).fillMaxWidth()
                                .clickable {
                                    newEmpName = it.name
                                    newEmpAddress = it.address
                                    newEmpSex = it.sex
                                    newEmpDept = it.idDept
                                    newEmpWage = it.wage
                                    newEmpTimeWorked = it.timeWorked!!
                                    empViewModel.get(it.name)
                                }.animateEnterExit(enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkHorizontally()),
                            emp = it
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
fun EmployeeItem(emp: Employee, modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Number: ${emp.id}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "Employee: ${emp.name}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            text = "Wage: ${emp.wage}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "Sex: ${emp.sex}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "Birthday: ${emp.bornDate}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "Address: ${emp.address}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "Department: ${emp.idDept}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "Time worked: ${emp.timeWorked}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(18.dp))
    }
}