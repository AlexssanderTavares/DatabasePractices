package org.example.cahousing.DataSource.Data.Accessors

import org.example.cahousing.DataSource.Models.Employee

interface EmployeeAccessorImp {

    suspend fun create(model: Employee) : Int

    suspend fun update(model: Employee, data: Employee) : Int

    suspend fun get(varchar: String) : Employee?

    suspend fun getAll() : ArrayList<Employee>

    suspend fun delete(model: Employee) : Int

    fun turnTestOn()

    fun turnTestOff()
}