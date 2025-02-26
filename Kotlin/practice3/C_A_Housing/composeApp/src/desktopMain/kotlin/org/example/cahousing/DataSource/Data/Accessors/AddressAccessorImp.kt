package org.example.cahousing.DataSource.Data.Accessors

import org.example.cahousing.DataSource.Models.Address
import org.example.cahousing.DataSource.Models.Models

interface AddressAccessorImp {
    suspend fun create(model: Address) : Int

    suspend fun update(model: Address, data: Address) : Int

    suspend fun get(varchar: String) : Address?

    suspend fun getAll() : ArrayList<Address>

    suspend fun delete(model: Address) : Int

    fun turnTestOn()

    fun turnTestOff()
}