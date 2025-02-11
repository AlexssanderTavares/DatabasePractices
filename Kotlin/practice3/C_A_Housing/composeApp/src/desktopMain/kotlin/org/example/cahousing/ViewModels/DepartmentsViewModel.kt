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
import org.example.cahousing.DataSource.Repositories.Repository


class DepartmentsViewModel : ViewModel() {

    init{
        //getDeptList()
    }

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

    private val _deleteResult: MutableStateFlow<Int> = MutableStateFlow(0)
    val deleteResult: StateFlow<Int> = _deleteResult.asStateFlow()

    fun create(dept: Dept) {
        //repo = DeptRepository()
        viewModelScope.launch(Dispatchers.IO) {
            println("Creating ${dept.name}")
            _creationResult.update { repo.create(dept) }
            println("Creation Successful!")
            println("Process result: ${creationResult.value}")
          //  getDeptList()
        }
    }

    /*fun getDept(name: String) {
        repo = DeptRepository()
        viewModelScope.launch(Dispatchers.IO) {
            println("Trying to get department with that name...")
            _getResult.update { repo.get(name) }
            println("Success getting Department!!!")
            println("Department found: ${getResult.value}")
        }
    }

    fun getDeptList(){
        repo = DeptRepository()
        viewModelScope.launch(Dispatchers.IO) {
            println("Getting Departments list")
            _deptList.update { repo.getAll() }
            deptList.value.forEach {
                println(it)
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
            getDeptList()
        }
    }

    fun delete(dept: Dept) {
        repo = DeptRepository()
        viewModelScope.launch(Dispatchers.IO) {
            println("Deleting ${dept}")
            _deleteResult.update { repo.delete(dept) }
            _deptList.update {
                it.remove(dept)
                it
            }
            if(deleteResult.value > 0) {
                println("Data deleted successfully.")
                println("Number of deletions: ${deleteResult.value}")
            }
            getDeptList()
        }
    }*/

}