package org.example.cahousing.DataSource.Repositories

import org.example.cahousing.DataSource.Data.DataBaseActions
import org.example.cahousing.DataSource.Models.Overseer

class OverseerRepository : Repository<Overseer>{

    companion object{
        val db: DataBaseActions = DataBaseActions()
    }


    override suspend fun create(model: Overseer): Int {
        TODO("Not yet implemented")
    }

    override suspend fun get(model: Overseer): Overseer? {
        TODO("Not yet implemented")
    }

    override suspend fun update(model: Overseer, vararg newData: Any): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getAll() : ArrayList<Overseer> {
        return db.getAllOverseer()
    }

    override suspend fun delete(model: Overseer): Int {
        TODO("Not yet implemented")
    }

}