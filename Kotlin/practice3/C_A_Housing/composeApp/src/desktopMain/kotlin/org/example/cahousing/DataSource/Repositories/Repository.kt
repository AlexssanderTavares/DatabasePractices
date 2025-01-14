package org.example.cahousing.DataSource.Repositories

import org.example.cahousing.DataSource.Models.Models

interface Repository<T: Models> {

    suspend fun create(model: T) : Int

    suspend fun update(model: T, vararg newData: Any) : Int

    suspend fun get(model: T) : T?

    suspend fun getAll() : ArrayList<T>

    suspend fun delete(model: T) : Int
}