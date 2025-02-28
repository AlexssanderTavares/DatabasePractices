package org.example.cahousing.DataSource.Data.Accessors

import org.example.cahousing.DataSource.Models.Project

interface ProjectAccessorImp {
    suspend fun create(model: Project) : Int

    suspend fun update(model: Project, data: Project) : Int

    suspend fun get(varchar: String) : Project?

    suspend fun getAll() : ArrayList<Project>

    suspend fun delete(model: Project) : Int

    fun turnTestOn()

    fun turnTestOff()
}