package org.example.cahousing.DataSource.Data.Accessors

import org.example.cahousing.DataSource.Models.Overseer

interface OverseerAccessorImp {
    suspend fun create(model: Overseer) : Int

    suspend fun update(model: Overseer, data: Overseer) : Int

    suspend fun get(varchar: String) : Overseer?

    suspend fun getAll() : ArrayList<Overseer>

    suspend fun delete(model: Overseer) : Int
}