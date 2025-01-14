package org.example.cahousing.DataSource.Repositories

import org.example.cahousing.DataSource.Data.DataBaseActions
import org.example.cahousing.DataSource.Models.ProjectEmployeeContract

class EmployeeProjectContractRepository : Repository<ProjectEmployeeContract> {

    companion object{
        val db: DataBaseActions = DataBaseActions()
    }

    override suspend fun create(model: ProjectEmployeeContract): Int {
        return db.createProjectContract(model.project, model.employee)
    }

    override suspend fun get(model: ProjectEmployeeContract): ProjectEmployeeContract? {
        return db.getProjectContract(model)
    }

    override suspend fun update(model: ProjectEmployeeContract, vararg newData: Any): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getAll() : ArrayList<ProjectEmployeeContract> {
        return db.getAllProjectContracts()
    }

    override suspend fun delete(model: ProjectEmployeeContract): Int {
        return db.deleteProjectContract(model)
    }

}