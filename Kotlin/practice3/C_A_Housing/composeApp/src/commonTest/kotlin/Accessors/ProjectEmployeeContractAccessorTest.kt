package org.example.cahousing.Accessors

import kotlinx.coroutines.processNextEventInCurrentThread
import kotlinx.coroutines.runBlocking
import org.example.cahousing.DataSource.Data.Accessors.EmployeeAccessor
import org.example.cahousing.DataSource.Data.Accessors.EmployeeAccessorImp
import org.example.cahousing.DataSource.Data.Accessors.ProjectAccessor
import org.example.cahousing.DataSource.Data.Accessors.ProjectAccessorImp
import org.example.cahousing.DataSource.Data.Accessors.ProjectEmployeeContractAccessor
import org.example.cahousing.DataSource.Models.Employee
import org.example.cahousing.DataSource.Models.Project
import org.example.cahousing.DataSource.Models.ProjectEmployeeContract
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.DefaultAsserter.assertNotNull
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.fail

class ProjectEmployeeContractAccessorTest {

    private val empAccessor: EmployeeAccessorImp = EmployeeAccessor()
    private val projectAccessor: ProjectAccessorImp = ProjectAccessor()
    private val contractAccessor: ProjectEmployeeContractAccessor = ProjectEmployeeContractAccessor()

    @Before
    fun setup(){
        contractAccessor.turnTestOn()
        empAccessor.turnTestOn()
        projectAccessor.turnTestOn()
    }

    @After
    fun tearDown(){
        contractAccessor.turnTestOff()
        empAccessor.turnTestOff()
        projectAccessor.turnTestOff()
    }

    @Test
    fun `Should create a contract and return the number of affected rows`(){
        runBlocking {
            try{
                println("Trying to create a contract...")
                val project: Project = projectAccessor.get("ProjectTest")!!
                val emp: Employee = empAccessor.get("test2")!!

                println("Project: $project")
                println("Employee: $emp")

                val contract: ProjectEmployeeContract = ProjectEmployeeContract(project = project, employee = emp, description = "Test contract2")
                assertEquals(1, contractAccessor.create(contract))
                println("Success!!!")
            } catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should get a contract from database and return it as an object`(){
        runBlocking {
            try{
                println("Trying to get a contract...")
                val contract: ProjectEmployeeContract? = contractAccessor.get("ProjectTest")
                assertNotNull("Null returned", contract)
                println("Success!!")
            }catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should get all contracts and return it as a list`(){
        runBlocking {
            try{
                println("Trying to get all contracts...")
                val list: ArrayList<ProjectEmployeeContract> = contractAccessor.getAll()
                println(list)
                assert(list.size > 0)
                println("Success!!!")
            }catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should update contract and return the number of affected rows`(){
        runBlocking {
            try{
                println("Trying to update a contract...")
                val contract: ProjectEmployeeContract = contractAccessor.get("ProjectTest")!!
                val data = contract
                contract.description = "Update description test"
                assertEquals(1, contractAccessor.update(data,contract))
                println("Success!!!")
            }catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should delete contract and return the number of affected rows`(){
        runBlocking {
            try{
                println("Trying to delete a contract...")
                val contract: ProjectEmployeeContract = contractAccessor.get("ProjectTest")!!
                assertEquals(1, contractAccessor.delete(contract))
                println("Success!!!")
            }catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }
        }
    }
}