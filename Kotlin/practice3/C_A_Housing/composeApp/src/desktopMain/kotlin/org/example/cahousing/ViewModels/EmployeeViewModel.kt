package org.example.cahousing.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.cahousing.DataSource.Data.Accessors.AddressAccessor
import org.example.cahousing.DataSource.Data.Accessors.AddressAccessorImp
import org.example.cahousing.DataSource.Data.Accessors.EmployeeAccessor
import org.example.cahousing.DataSource.Data.Accessors.EmployeeAccessorImp
import org.example.cahousing.DataSource.Models.Address
import org.example.cahousing.DataSource.Models.Employee
import org.example.cahousing.DataSource.Utilities.Cep
import org.example.cahousing.DataSource.Utilities.Formatter

class EmployeeViewModel: ViewModel() {

    private val empRepo: EmployeeAccessorImp = EmployeeAccessor()
    private val addressRepo: AddressAccessorImp = AddressAccessor()
    private val formatter: Formatter = Cep()

    val _createResult: MutableStateFlow<Int> = MutableStateFlow(0)
    val createResult: StateFlow<Int> = _createResult.asStateFlow()

    val _getList: MutableStateFlow<ArrayList<Employee>> = MutableStateFlow(arrayListOf())
    val getList: StateFlow<ArrayList<Employee>> = _getList.asStateFlow()

    val _getResult: MutableStateFlow<Employee?> = MutableStateFlow(null)
    val getResult: StateFlow<Employee?> = _getResult.asStateFlow()

    val _updateResult: MutableStateFlow<Int> = MutableStateFlow(0)
    val updateResult: StateFlow<Int> = _updateResult.asStateFlow()

    val _deleteResult: MutableStateFlow<Int> = MutableStateFlow(0)
    val deleteResult: StateFlow<Int> = _deleteResult.asStateFlow()

    fun create(model: Employee) {

        viewModelScope.launch(Dispatchers.IO) {
            val res1: Deferred<Int> = async{
                var asyncRes: Int = 0
                if(addressRepo.get(model.address) == null){
                    println("Employee address not found, creating a new register...")
                    //TODO("Must implement an address API finder by CEP")
                    val address: Address = Address(formatter.format(model.address), "Must be implemented","Must be implemented","Must be implemented")
                    asyncRes += addressRepo.create(address)
                }
                asyncRes
            }
            if(res1.await() > 0) {
                println("Creating ${model.name}")
                _createResult.update { empRepo.create(model) }
                println("Creation Successful!")
                println("Process result: ${createResult.value}")
                getList()
            }
        }
    }

    fun get(varchar: String) {
        viewModelScope.launch(Dispatchers.IO) {
            println("Trying to get project with that name...")
            _getResult.update { empRepo.get(varchar) }
            println("Success getting Employee!!!")
            println("Employee found: ${getResult.value}")
        }
    }

    fun getList(){
        viewModelScope.launch(Dispatchers.IO) {
            println("Getting Employee list")
            _getList.update { empRepo.getAll() }
            getList.value.forEach {
                println(it)
            }
        }
    }

    fun update(model: Employee, data: Employee) {
        viewModelScope.launch(Dispatchers.IO) {
            println("Updating ${model.name}...")
            if(model.name == data.name) {

                if(model.address != data.address){
                    if(addressRepo.get(model.address) == null){
                        println("Employee address not found, creating a new register...")
                        //TODO("Must implement an address API finder by CEP")
                        val address: Address = Address(formatter.format(model.address), "Must be implemented","Must be implemented","Must be implemented")
                        addressRepo.create(address)
                    }
                }

                if(model.wage != data.wage){
                    println("${model.wage} to ${data.wage}")
                }

                if(model.timeWorked != data.timeWorked){
                    println("${model.timeWorked} to ${data.timeWorked}")
                }

                if(model.idDept != data.idDept){
                    println("${model.idDept} to ${data.idDept}")
                }

                _updateResult.value = empRepo.update(model, data)
                println("Update Successful!")

                if(updateResult.value > 0){
                    println("Number of updates: ${updateResult.value}")
                }
            }
            getList()
        }
    }

    fun delete(model: Employee) {
        viewModelScope.launch(Dispatchers.IO) {
            println("Deleting ${model}")
            _deleteResult.update { empRepo.delete(model) }
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