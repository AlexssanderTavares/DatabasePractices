package org.example.cahousing.DataSource.Repositories

import org.example.cahousing.DataSource.Data.DataBaseActions
import org.example.cahousing.DataSource.Models.Overseer

class OverseerRepository : Repository<Overseer>{

    companion object{
        val db: DataBaseActions = DataBaseActions()
    }


    override suspend fun create(model: Overseer): Int {
        return db.createOverseer(model)
    }

    override suspend fun get(name: String): Overseer? {
        return db.getOverseer(name)
    }

    override suspend fun update(model: Overseer, data: Overseer): Int {
        return db.updateOverseer(model, data)
    }

    override suspend fun getAll() : ArrayList<Overseer> {
        return db.getAllOverseer()
    }

    override suspend fun delete(model: Overseer): Int {
        return db.deleteOverseer(model)
    }

}