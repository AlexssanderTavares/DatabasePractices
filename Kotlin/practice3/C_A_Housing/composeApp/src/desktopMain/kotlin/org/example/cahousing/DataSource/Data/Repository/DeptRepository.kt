package org.example.cahousing.DataSource.Data.Repository


import org.example.cahousing.DataSource.Data.Accessors.DataBaseAccessor
import org.example.cahousing.DataSource.Data.Accessors.DeptAccessorImp
import org.example.cahousing.DataSource.Models.Dept
import org.example.cahousing.DataSource.Models.Models
import org.example.cahousing.Factories.DataBaseAccessorFactory

class DeptRepository {

    private lateinit var accessor: DeptAccessorImp

    suspend fun create(dept: Dept): Int{
        return accessor.create(dept)
    }

    suspend fun get(name: String) : Dept?{
        return accessor.get(name)
    }

    suspend fun getAll() : ArrayList<Dept>{
        return accessor.getAll()
    }

    suspend fun update(dept: Dept, data: Dept): Int{
        return accessor.update(dept, data)
    }

    suspend fun delete(dept: Dept): Int{
        return accessor.delete(dept)
    }


}