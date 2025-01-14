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

    override suspend fun get(model: Address): Address? {
        return db.getAddress(model.cep)
    }

    override suspend fun update(model: Address, vararg newData: Any): Int {
        return db.updateAddress(model, newData)
    }

    override suspend fun getAll() : ArrayList<Address> {
        return db.getAllAddress()
    }

    override suspend fun delete(model: Address): Int {
        return db.deleteAddress(model)
    }
}