package org.example.cahousing.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.cahousing.DataSource.Models.Dept
import org.example.cahousing.DataSource.Models.Models
import org.example.cahousing.DataSource.Repositories.DeptRepository
import org.example.cahousing.DataSource.Repositories.Repository

class DepartmentsViewModel: ViewModel() {

    init {
        println("DepartmentsViewModel class loaded!")
    }

    private lateinit var repo: Repository<Dept>
    private val resultMessageError: String = "ERROR: couldn't retrieve answer for this requisition"

    private val _creationResult: MutableStateFlow<Int?> = MutableStateFlow(null)
    val creationResult = this._creationResult.asStateFlow() ?: resultMessageError

    private val _getResult: MutableStateFlow<Dept?> = MutableStateFlow(null)
    val getResult = this._getResult.asStateFlow() ?: resultMessageError

    private val _getAllResult: MutableStateFlow<ArrayList<Dept>?> = MutableStateFlow(null)
    val getAllResult = this._getAllResult.asStateFlow() ?: resultMessageError

    private val _deleteResult: MutableStateFlow<Int?> = MutableStateFlow(null)
    val deleteResult = this._deleteResult.asStateFlow() ?: resultMessageError


    fun createDept(dept: Dept) {
        var res: Int = 0
        viewModelScope.launch(Dispatchers.Main){
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

    fun getDept(dept: Dept) {
        viewModelScope.launch(Dispatchers.Main) {
            println("DepartmentsViewModel: Accessing repository for getting data...")
            delay(2000)
            repo = DeptRepository()
            val department: Dept? = repo.get(dept.name)
            _getResult.value = department
        }
    }

    fun getAll() {
        viewModelScope.launch(Dispatchers.Main) {
            delay(2000)
            repo = DeptRepository()
            _getAllResult.value = repo.getAll()
        }
    }

    fun delete(dept: Dept) {
        viewModelScope.launch(Dispatchers.Main) {
            delay(2000)
            repo = DeptRepository()
            _deleteResult.value = repo.delete(dept)
        }
    }
}