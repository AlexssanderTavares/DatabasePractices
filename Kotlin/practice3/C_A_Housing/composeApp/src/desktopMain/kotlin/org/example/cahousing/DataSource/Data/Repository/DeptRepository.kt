package org.example.cahousing.DataSource.Data.Repository


import org.example.cahousing.DataSource.Data.Accessors.DataBaseAccessor
import org.example.cahousing.DataSource.Models.Dept
import org.example.cahousing.DataSource.Models.Models
import org.example.cahousing.Factories.DataBaseAccessorFactory

class DeptRepository : Repository<Dept> {

    suspend fun create(dept: Dept): Int{
        return DataBaseAccessorFactory.generate<Dept>().create(dept)
    }

    suspend fun get(name: String) : Dept?{
        return DataBaseAccessorFactory.generate<Dept>().get(name)
    }

    suspend fun getAll() : ArrayList<Dept>{
        return DataBaseAccessorFactory.generate<Dept>().getAll()
    }

    suspend fun update(dept: Dept, data: Dept): Int{
        return DataBaseAccessorFactory.generate<Dept>().update(dept, data)
    }

    suspend fun delete(dept: Dept): Int{
        return DataBaseAccessorFactory.generate<Dept>().delete(dept)
    }


}