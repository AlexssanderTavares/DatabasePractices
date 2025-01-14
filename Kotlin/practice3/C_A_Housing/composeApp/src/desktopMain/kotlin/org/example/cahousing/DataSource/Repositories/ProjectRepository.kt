package org.example.cahousing.DataSource.Repositories

import org.example.cahousing.DataSource.Data.DataBaseActions
import org.example.cahousing.DataSource.Models.Project

class ProjectRepository : Repository<Project> {

    companion object{
        val db: DataBaseActions = DataBaseActions()
    }


    override suspend fun create(model: Project): Int {
        return db.createProject(model)
    }

    override suspend fun get(model: Project): Project? {
        return db.getProject(model.name)
    }

    override suspend fun update(model: Project, vararg newData: Any): Int {
        return db.updateProject(model, newData)
    }

    override suspend fun getAll() : ArrayList<Project> {
        return db.getAllProject()
    }

    override suspend fun delete(model: Project): Int {
        return db.deleteProject(model)
    }

}