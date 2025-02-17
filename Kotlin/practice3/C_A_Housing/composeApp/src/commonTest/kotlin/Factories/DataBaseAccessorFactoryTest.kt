package org.example.cahousing.Factories

import kotlinx.coroutines.runBlocking
import org.example.cahousing.DataSource.Data.Accessors.AddressAccessor
import org.example.cahousing.DataSource.Data.Accessors.DeptAccessor
import org.example.cahousing.DataSource.Data.Accessors.EmployeeAccessor
import org.example.cahousing.DataSource.Data.Accessors.OverseerAccessor
import org.example.cahousing.DataSource.Data.Accessors.ProjectAccessor
import org.example.cahousing.DataSource.Data.Accessors.ProjectEmployeeContractAccessor
import org.example.cahousing.DataSource.Models.Address
import org.example.cahousing.DataSource.Models.Dept
import org.example.cahousing.DataSource.Models.Employee
import org.example.cahousing.DataSource.Models.Overseer
import org.example.cahousing.DataSource.Models.Project
import org.example.cahousing.DataSource.Models.ProjectEmployeeContract
import kotlin.test.Test
import kotlin.test.fail

class DataBaseAccessorFactoryTest {

    @Test
    fun shouldCertifyThatIsReturningEachDataBaseAccessorClassWithItRespectiveModels(){
        runBlocking {
            try{
                val addressAccessor = DataBaseAccessorFactory.generate<Address>()
                println("Testing AddressAccessor...")
                assert(addressAccessor is AddressAccessor)

                println("Testing DeptAccessor...")
                val deptAccessor = DataBaseAccessorFactory.generate<Dept>()
                assert(deptAccessor is DeptAccessor)

                println("Testing EmployeeAccessor...")
                val employeeAccessor = DataBaseAccessorFactory.generate<Employee>()
                assert(employeeAccessor is EmployeeAccessor)

                println("Testing ProjectAccessor...")
                val projectAccessor = DataBaseAccessorFactory.generate<Project>()
                assert(projectAccessor is ProjectAccessor)

                println("Testing OverseerAccessor...")
                val overseerAccessor = DataBaseAccessorFactory.generate<Overseer>()
                assert(overseerAccessor is OverseerAccessor)

                println("Testing ProjectEmployeeContractAccessor...")
                val projectEmployeeContractAccessor = DataBaseAccessorFactory.generate<ProjectEmployeeContract>()
                assert(projectEmployeeContractAccessor is ProjectEmployeeContractAccessor)
                println("Test Completed!")
                println("Success!!!")
            }catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }

        }
    }
}