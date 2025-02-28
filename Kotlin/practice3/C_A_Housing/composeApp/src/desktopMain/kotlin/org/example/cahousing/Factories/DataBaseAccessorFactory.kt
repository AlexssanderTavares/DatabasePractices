package org.example.cahousing.Factories

import org.example.cahousing.DataSource.Data.Accessors.AddressAccessor
import org.example.cahousing.DataSource.Data.Accessors.AddressAccessorImp
import org.example.cahousing.DataSource.Data.Accessors.DeptAccessor
import org.example.cahousing.DataSource.Data.Accessors.DeptAccessorImp
import org.example.cahousing.DataSource.Data.Accessors.EmployeeAccessor
import org.example.cahousing.DataSource.Data.Accessors.EmployeeAccessorImp
import org.example.cahousing.DataSource.Data.Accessors.OverseerAccessor
import org.example.cahousing.DataSource.Data.Accessors.OverseerAccessorImp
import org.example.cahousing.DataSource.Data.Accessors.ProjectAccessor
import org.example.cahousing.DataSource.Data.Accessors.ProjectAccessorImp
import org.example.cahousing.DataSource.Data.Accessors.ProjectEmployeeContractAccessor
import org.example.cahousing.DataSource.Data.Accessors.ProjectEmployeeContractAccessorImp
import org.example.cahousing.DataSource.Models.Address
import org.example.cahousing.DataSource.Models.Dept
import org.example.cahousing.DataSource.Models.Employee
import org.example.cahousing.DataSource.Models.Models
import org.example.cahousing.DataSource.Models.Overseer
import org.example.cahousing.DataSource.Models.Project
import org.example.cahousing.DataSource.Models.ProjectEmployeeContract

object DataBaseAccessorFactory {

    //TODO("Should be deleted soon")
    /*inline fun<reified T> generate(): T {
        return when(T::class){
            AddressAccessor::class -> AddressAccessorImp
            Dept::class -> DeptAccessor() as DeptAccessorImp
            Employee::class -> EmployeeAccessor() as EmployeeAccessorImp
            Project::class -> ProjectAccessor() as ProjectAccessorImp
            Overseer::class -> OverseerAccessor() as OverseerAccessorImp
            ProjectEmployeeContract::class -> ProjectEmployeeContractAccessor() as ProjectEmployeeContractAccessorImp
            else -> {
                throw IllegalArgumentException("Invalid Model type for this context")
            }
        }
    }*/
}