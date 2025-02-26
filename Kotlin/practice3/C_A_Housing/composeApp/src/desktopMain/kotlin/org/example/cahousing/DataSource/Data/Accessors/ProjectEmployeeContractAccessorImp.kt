package org.example.cahousing.DataSource.Data.Accessors

import org.example.cahousing.DataSource.Models.ProjectEmployeeContract

interface ProjectEmployeeContractAccessorImp{
    suspend fun create(model: ProjectEmployeeContract) : Int

    suspend fun update(model: ProjectEmployeeContract, data: ProjectEmployeeContract) : Int

    suspend fun get(varchar: String) : ProjectEmployeeContract?

    suspend fun getAll() : ArrayList<ProjectEmployeeContract>

    suspend fun delete(model: ProjectEmployeeContract) : Int
}