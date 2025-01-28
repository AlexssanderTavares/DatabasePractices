package org.example.cahousing.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.cahousing.DataSource.Models.Dept
import org.example.cahousing.DataSource.Models.Models
import org.example.cahousing.DataSource.Repositories.DeptRepository
import org.example.cahousing.DataSource.Repositories.Repository
import org.example.cahousing.ViewModels.UIStates.DepartmentsViewUIState

class DepartmentsViewModel : ViewModel(){

    private lateinit var repo: Repository<Dept>

    private val _deptList: MutableStateFlow<ArrayList<Dept>> = MutableStateFlow(arrayListOf())
    val deptList: StateFlow<ArrayList<Dept>> = _deptList.asStateFlow()

    fun getDeptList() {
        repo = DeptRepository()
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                println("Getting Departments list")
                _deptList.value = repo.getAll()
                println("Departments List:")
                deptList.value.forEach{
                    println("Department: ${it.name}")
                }
                delay(5000)
            }
        }
    }


}