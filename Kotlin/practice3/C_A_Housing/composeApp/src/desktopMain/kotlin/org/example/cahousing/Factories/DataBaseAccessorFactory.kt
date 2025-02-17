package org.example.cahousing.Factories

import org.example.cahousing.DataSource.Data.Accessors.AddressAccessor
import org.example.cahousing.DataSource.Data.Accessors.DataBaseAccessor
import org.example.cahousing.DataSource.Data.Accessors.DeptAccessor
import org.example.cahousing.DataSource.Data.Accessors.EmployeeAccessor
import org.example.cahousing.DataSource.Data.Accessors.OverseerAccessor
import org.example.cahousing.DataSource.Data.Accessors.ProjectAccessor
import org.example.cahousing.DataSource.Data.Accessors.ProjectEmployeeContractAccessor
import org.example.cahousing.DataSource.Models.Address
import org.example.cahousing.DataSource.Models.Dept
import org.example.cahousing.DataSource.Models.Employee
import org.example.cahousing.DataSource.Models.Models
import org.example.cahousing.DataSource.Models.Overseer
import org.example.cahousing.DataSource.Models.Project
import org.example.cahousing.DataSource.Models.ProjectEmployeeContract
import kotlin.reflect.KClass

object DataBaseAccessorFactory {

    inline fun<reified T: Models> generate(): DataBaseAccessor<T> {
        return when(T::class){
            Address::class -> AddressAccessor() as DataBaseAccessor<T>
            Dept::class -> DeptAccessor() as DataBaseAccessor<T>
            Employee::class -> EmployeeAccessor() as DataBaseAccessor<T>
            Project::class -> ProjectAccessor() as DataBaseAccessor<T>
            Overseer::class -> OverseerAccessor() as DataBaseAccessor<T>
            ProjectEmployeeContract::class -> ProjectEmployeeContractAccessor() as DataBaseAccessor<T>
            else -> {
                throw IllegalArgumentException("Invalid Model type for this context")
            }
        }
    }
}