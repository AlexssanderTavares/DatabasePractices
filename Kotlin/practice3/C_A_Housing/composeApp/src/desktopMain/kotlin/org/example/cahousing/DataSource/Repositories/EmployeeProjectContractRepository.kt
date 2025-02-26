package org.example.cahousing.DataSource.Repositories


import org.example.cahousing.DataSource.Models.ProjectEmployeeContract

/*
class EmployeeProjectContractRepository : Repository<ProjectEmployeeContract> {

    companion object{
        val db: DataBaseActions = DataBaseActions()
    }

    override suspend fun create(model: ProjectEmployeeContract): Int {
        return db.createProjectContract(model.project, model.employee)
    }

    override suspend fun get(varchar: String): ProjectEmployeeContract? {
        return db.getProjectContract(varchar)
    }

    override suspend fun update(model: ProjectEmployeeContract, data: ProjectEmployeeContract): Int {
        return db.updateProjectContract(model, data)
    }

    override suspend fun getAll() : ArrayList<ProjectEmployeeContract> {
        return db.getAllProjectContracts()
    }

    override suspend fun delete(model: ProjectEmployeeContract): Int {
        return db.deleteProjectContract(model)

    }

}
*/
