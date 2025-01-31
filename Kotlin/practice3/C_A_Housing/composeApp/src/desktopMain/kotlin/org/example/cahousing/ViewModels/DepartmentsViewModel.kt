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

class DepartmentsViewModel : ViewModel() {

    private lateinit var repo: Repository<Dept>

    // List mutable state
    private val _deptList: MutableStateFlow<ArrayList<Dept>> = MutableStateFlow(arrayListOf())
    val deptList: StateFlow<ArrayList<Dept>> = _deptList.asStateFlow()

    // Mutable return value of new dept creation
    private val _creationResult: MutableStateFlow<Int> = MutableStateFlow(0)
    val creationResult: StateFlow<Int> = _creationResult.asStateFlow()

    // Get and Set dept caught by get method
    private val _getResult: MutableStateFlow<Dept?> = MutableStateFlow(null)
    val getResult: StateFlow<Dept?> = _getResult.asStateFlow()

    // Update method must be called from UI and change updateResult value
    private val _updateResult: MutableStateFlow<Int> = MutableStateFlow(0)
    val updateResult: StateFlow<Int> = _updateResult.asStateFlow()

    fun create(dept: Dept) {
        repo = DeptRepository()
        viewModelScope.launch(Dispatchers.IO) {
            println("Creating ${dept.name}")
            _creationResult.value = repo.create(dept)
            println("Creation Successful!")
            println("Process result: ${creationResult.value}")
        }
    }

    fun getDept(name: String) {
        repo = DeptRepository()
        viewModelScope.launch(Dispatchers.IO) {
            println("Trying to get department with that name...")
            _getResult.value = repo.get(name)
            println("Success getting Department!!!")
            println("Department found: ${getResult.value}")
        }
    }

    fun getDeptList() {
        repo = DeptRepository()
        viewModelScope.launch(Dispatchers.IO) {
            println("Getting Departments list")
            _deptList.value = repo.getAll()
            println("Departments List:")
            deptList.value.forEach {
                println("Department: ${it.name}")
            }
        }
    }

    fun update(dept: Dept, data: Dept) {
        repo = DeptRepository()
        viewModelScope.launch(Dispatchers.IO) {
            println("Updating ${dept.name}...")
            if(dept.name != data.name) {
                println("${dept.name} to ${data.name}")
            }
            if(dept.description != data.description){
                println("${dept.description} to ${data.description}")
            }
            _updateResult.value = repo.update(dept, data)
            if(updateResult.value > 0){
                println("Number of updates: ${updateResult.value}")
            }
        }
    }

}