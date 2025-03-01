package org.example.cahousing.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.cahousing.DataSource.Data.Accessors.ProjectAccessor
import org.example.cahousing.DataSource.Data.Accessors.ProjectAccessorImp
import org.example.cahousing.DataSource.Models.Project

class ProjectViewModel : ViewModel() {

    private val repo: ProjectAccessorImp = ProjectAccessor()

    val _createResult: MutableStateFlow<Int> = MutableStateFlow(0)
    val createResult: StateFlow<Int> = _createResult.asStateFlow()

    val _getList: MutableStateFlow<ArrayList<Project>> = MutableStateFlow(arrayListOf())
    val getList: StateFlow<ArrayList<Project>> = _getList.asStateFlow()

    val _getResult: MutableStateFlow<Project?> = MutableStateFlow(null)
    val getResult: StateFlow<Project?> = _getResult.asStateFlow()

    val _updateResult: MutableStateFlow<Int> = MutableStateFlow(0)
    val updateResult: StateFlow<Int> = _updateResult.asStateFlow()

    val _deleteResult: MutableStateFlow<Int> = MutableStateFlow(0)
    val deleteResult: StateFlow<Int> = _deleteResult.asStateFlow()

    fun create(model: Project) {
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
            println("Trying to get project with that name...")
            _getResult.update { repo.get(varchar) }
            println("Success getting Project!!!")
            println("Project found: ${getResult.value}")
        }
    }

    fun getList(){
        viewModelScope.launch(Dispatchers.IO) {
            println("Getting Project list")
            _getList.update { repo.getAll() }
            getList.value.forEach {
                println(it)
            }
        }
    }

    fun update(model: Project, data: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            println("Updating ${model.name}...")
            if(model.name != data.name) {
                println("${model.name} to ${data.name}")
            }
            if(model.dept != data.dept){
                println("${model.dept} to ${data.dept}")
            }
            _updateResult.value = repo.update(model, data)
            if(updateResult.value > 0){
                println("Number of updates: ${updateResult.value}")
            }
            getList()
        }
    }

    fun delete(model: Project) {
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