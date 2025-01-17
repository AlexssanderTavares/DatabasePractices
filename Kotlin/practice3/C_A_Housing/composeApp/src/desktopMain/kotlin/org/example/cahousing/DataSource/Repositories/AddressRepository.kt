package org.example.cahousing.DataSource.Repositories

import org.example.cahousing.DataSource.Data.DataBaseActions
import org.example.cahousing.DataSource.Models.Address

class AddressRepository : Repository<Address> {

    companion object {
        private val db: DataBaseActions = DataBaseActions()
    }

    override suspend fun create(model: Address): Int {
        return db.createAddress(model)
    }

    override suspend fun get(cep: String): Address? {
        return db.getAddress(cep)
    }

    override suspend fun update(model: Address, data: Address): Int {
        return db.updateAddress(model, data)
    }

    override suspend fun getAll() : ArrayList<Address> {
        return db.getAllAddress()
    }

    override suspend fun delete(model: Address): Int {
        return db.deleteAddress(model)
    }
}