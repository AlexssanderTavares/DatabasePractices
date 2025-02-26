package org.example.cahousing.DataSource.Data.Accessors

import org.example.cahousing.DataSource.Models.Dept

interface DeptAccessorImp {
    suspend fun create(model: Dept) : Int

    suspend fun update(model: Dept, data: Dept) : Int

    suspend fun get(varchar: String) : Dept?

    suspend fun getAll() : ArrayList<Dept>

    suspend fun delete(model: Dept) : Int

    fun turnTestOn()

    fun turnTestOff()
}