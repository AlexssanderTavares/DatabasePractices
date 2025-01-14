package org.example.cahousing.DataSource.Repositories

import org.example.cahousing.DataSource.Data.DataBaseActions
import org.example.cahousing.DataSource.Models.Employee

class EmployeeRepository : Repository<Employee>{

    companion object {
        val db: DataBaseActions = DataBaseActions()
    }

    override suspend fun create(model: Employee): Int {
        return db.createEmployee(model)
    }

    override suspend fun get(model: Employee): Employee? {
        return db.getEmployee(model.name)
    }

    override suspend fun update(model: Employee, vararg newData: Any): Int {
        return db.updateEmployee(model, newData)
    }

    override suspend fun getAll() : ArrayList<Employee> {
        return db.getAllEmployee()
    }

    override suspend fun delete(model: Employee): Int {
        return db.deleteEmployee(model)
    }

}