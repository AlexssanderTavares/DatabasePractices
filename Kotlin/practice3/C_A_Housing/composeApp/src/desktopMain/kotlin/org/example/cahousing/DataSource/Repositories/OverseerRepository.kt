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

    override suspend fun get(model: Overseer): Overseer? {
        return db.getOverseer(model)
    }

    override suspend fun update(model: Overseer, vararg newData: Any): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getAll() : ArrayList<Overseer> {
        return db.getAllOverseer()
    }

    override suspend fun delete(model: Overseer): Int {
        return db.deleteOverseer(model.empName)
    }

}