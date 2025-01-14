package org.example.cahousing.DataSource.Repositories

import androidx.compose.foundation.gestures.DraggableAnchors
import org.example.cahousing.DataSource.Data.DataBaseActions
import org.example.cahousing.DataSource.Models.Dept
import org.example.cahousing.DataSource.Models.Models

class DeptRepository : Repository<Dept> {

    companion object {
        val db: DataBaseActions = DataBaseActions()
    }

    override suspend fun create(model: Dept): Int {
        return db.createDept(model)
    }

    override suspend fun get(model: Dept): Dept? {
        return db.getDept(model.name)
    }

    override suspend fun update(model: Dept, vararg newData: Any): Int {
        return db.updateDept(model.name, newData.toString())
    }

    override suspend fun getAll(): ArrayList<Dept> {
        return db.getAllDept()
    }

    override suspend fun delete(model: Dept): Int {

        return db.deleteDept(model.name)

    }


}