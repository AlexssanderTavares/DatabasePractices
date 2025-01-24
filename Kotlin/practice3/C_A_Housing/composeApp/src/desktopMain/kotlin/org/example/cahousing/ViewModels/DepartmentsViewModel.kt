package org.example.cahousing.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
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

    init {
        println("DepartmentsViewModel class loaded!")
    }

    private lateinit var repo: Repository<Dept>

    private val _uiState: MutableStateFlow<DepartmentsViewUIState> = MutableStateFlow(
        DepartmentsViewUIState()
    )
    val uiState: StateFlow<DepartmentsViewUIState> = _uiState.asStateFlow()

    private val _creationResult: MutableStateFlow<Int?> = MutableStateFlow(null)
    val creationResult: StateFlow<Int?> = _creationResult.asStateFlow()

    private val _updateResult: MutableStateFlow<Int?> = MutableStateFlow(null)
    val updateResult: StateFlow<Int?> = _updateResult.asStateFlow()

    private val _getResult: MutableStateFlow<Dept?> = MutableStateFlow(null)
    val getResult: StateFlow<Dept?> = _getResult.asStateFlow()

    private val _getAllResult: MutableStateFlow<ArrayList<Dept>?> = MutableStateFlow(null)
    val getAllResult: StateFlow<ArrayList<Dept>?> = _getAllResult.asStateFlow()

    private val _deleteResult: MutableStateFlow<Int?> = MutableStateFlow(null)
    val deleteResult: StateFlow<Int?> = _deleteResult.asStateFlow()


    fun createDept(dept: Dept) {
        var res: Int = 0
        CoroutineScope(Dispatchers.IO).launch{
            println("DepartmentsViewModel: Accessing repository for new data creation...")
            delay(2000)
            repo = DeptRepository()
            res = repo.create(dept)
            when(res) {
                1 -> {
                    println("Result: $res")
                    _creationResult.value = res
                }
                0 -> {
                    println("Result: $res")
                    _creationResult.value = res
                }
                -1 -> {
                    println("Result: $res")
                    _creationResult.value = null
                }
            }
        }
    }

    fun updateDept(target: Dept, newData: Dept) {
        var res: Int = 0
        CoroutineScope(Dispatchers.IO).launch{
            println("DepartmentsViewModel: Accessing repository to update data...")
            delay(2000)
            repo = DeptRepository()
            res = repo.update(target, newData)
            when(res) {
                1 -> {
                    println("Result: $res")
                    _updateResult.value = res
                }
                2 -> {
                    println("Result: $res")
                    _updateResult.value = res
                }
                -1 -> {
                    println("Result: $res")
                    _updateResult.value = null
                }
            }
        }
    }

    fun getDept(dept: Dept) {
        CoroutineScope(Dispatchers.IO).launch {
            println("DepartmentsViewModel: Accessing repository for getting data...")
            delay(2000)
            repo = DeptRepository()
            val department: Dept? = repo.get(dept.name)
            _getResult.value = department
        }
    }

    fun getAll() {
        CoroutineScope(Dispatchers.IO).launch{
            while (true) {
                repo = DeptRepository()
                println("Getting all registers...")
                _getAllResult.value = repo.getAll()
                _getAllResult.value!!.forEach {
                    println("Dept: ${it}")
                }
                delay(5000)
            }
        }
    }

    fun changeVisibility(visibility: Boolean){
        _uiState.update { currentState ->
            currentState.copy(
                onVisibilityChange = {
                    _uiState.value = currentState.copy(visibility = visibility)
                }
            )
        }
    }

    fun delete(dept: Dept) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            repo = DeptRepository()
            _deleteResult.value = repo.delete(dept)
        }
    }
}