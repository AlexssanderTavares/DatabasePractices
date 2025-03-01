package org.example.cahousing.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.cahousing.DataSource.Data.Accessors.DeptAccessor
import org.example.cahousing.DataSource.Data.Accessors.DeptAccessorImp
import org.example.cahousing.DataSource.Models.Dept


class DeptViewModel: ViewModel(){

    private val repo: DeptAccessorImp = DeptAccessor()

    val _createResult: MutableStateFlow<Int> = MutableStateFlow(0)
    val createResult: StateFlow<Int> = _createResult.asStateFlow()

    val _getList: MutableStateFlow<ArrayList<Dept>> = MutableStateFlow(arrayListOf())
    val getList: StateFlow<ArrayList<Dept>> = _getList.asStateFlow()

    val _getResult: MutableStateFlow<Dept?> = MutableStateFlow(null)
    val getResult: StateFlow<Dept?> = _getResult.asStateFlow()

    val _updateResult: MutableStateFlow<Int> = MutableStateFlow(0)
    val updateResult: StateFlow<Int> = _updateResult.asStateFlow()

    val _deleteResult: MutableStateFlow<Int> = MutableStateFlow(0)
    val deleteResult: StateFlow<Int> = _deleteResult.asStateFlow()

    fun create(model: Dept) {
        viewModelScope.launch(Dispatchers.IO) {
            println("Creating ${model.name}")
            _createResult.update { repo.create(model) }
            println("Creation Successful!")
            println("Process result: ${createResult.value}")
            getList()
        }
    }

    fun get(varchar: String) {
        viewModelScope.launch(Dispatchers.IO) {
            println("Trying to get department with that name...")
            _getResult.update { repo.get(varchar) }
            println("Success getting Department!!!")
            println("Department found: ${getResult.value}")
        }
    }

    fun getList(){
        viewModelScope.launch(Dispatchers.IO) {
            println("Getting Departments list")
            _getList.update { repo.getAll() }
            getList.value.forEach {
                println(it)
            }
        }
    }

    fun update(model: Dept, data: Dept) {
        viewModelScope.launch(Dispatchers.IO) {
            println("Updating ${model.name}...")
            if(model.name != data.name) {
                println("${model.name} to ${data.name}")
            }
            if(model.description != data.description){
                println("${model.description} to ${data.description}")
            }
            _updateResult.value = repo.update(model, data)
            if(updateResult.value > 0){
                println("Number of updates: ${updateResult.value}")
            }
            getList()
        }
    }

    fun delete(model: Dept) {
        viewModelScope.launch(Dispatchers.IO) {
            println("Deleting ${model}")
            _deleteResult.update { repo.delete(model) }
            _getList.update {
                it.remove(model)
                it
            }
            if(deleteResult.value > 0) {
                println("Data deleted successfully.")
                println("Number of deletions: ${deleteResult.value}")
            }
            getList()
        }
    }

}