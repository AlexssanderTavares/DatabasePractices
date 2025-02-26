package org.example.cahousing.Accessors

import kotlinx.coroutines.runBlocking
import org.example.cahousing.DataSource.Data.Accessors.EmployeeAccessor
import org.example.cahousing.DataSource.Data.Accessors.EmployeeAccessorImp
import org.example.cahousing.DataSource.Models.Employee
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class EmployeeAccessorTest {

    private val accessor: EmployeeAccessorImp = EmployeeAccessor()

    @Before
    fun setup(){
        accessor.turnTestOn()
    }

    @After
    fun tearDown(){
        accessor.turnTestOff()
    }

    @Test
    fun `Should create Employee and return the number of affected rows`(){
        runBlocking {
            try{
                println("Trying to create a new Employee...")
                val employee = Employee(name = "test", sex = "male", wage = 1000.0, bornDate = "2000-01-01", idDept = 1, address = "01001000", timeWorked = "2000-01-01 00:00:00")
                assertEquals(1, accessor.create(employee))
                println("Success!")
            }catch(e: Exception){
                println("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should get and return Employee object from database by name`(){
        runBlocking {
            try{
                println("Trying to get Employee by name...")
                assertEquals(Employee(id = 4120, name = "test", sex = "male", wage = 1000.0, bornDate = "2000-01-01", idDept = 1, address = "01.001-000", timeWorked = "2000-01-01 00:00:00") , accessor.get("test"))
                println("Success!")
            }catch(e: Exception){
                println("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should return all Employees from database`(){
        runBlocking {
            try {
                println("Trying to get all Employees...")
                val list: ArrayList<Employee> = accessor.getAll()
                println(list)
                assert(list.size > 0)
                println("Success!")
            } catch (e: Exception) {
                println("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should update and return the number of affected rows`(){
        runBlocking {
            try {
                println("Trying to update an Employee...")
                val emp: Employee = Employee(
                    name = "test",
                    sex = "male",
                    wage = 1000.0,
                    bornDate = "2000-01-01",
                    idDept = 1,
                    address = "01001000",
                    timeWorked = "2000-01-01 00:00:00"
                )
                val data: Employee = Employee(
                    name = "test",
                    sex = "male",
                    wage = 1000.0,
                    bornDate = "2000-01-01",
                    idDept = 1,
                    address = "01001000",
                    timeWorked = "2000-01-01 10:05:00"
                )
                assertEquals(1, accessor.update(emp, data))
                println("Success!")
            } catch (e: Exception) {
                println("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should delete and return the number of affected rows`(){
        runBlocking {
            try {
                println("Trying to delete an Employee...")
                val emp: Employee = Employee(name = "test", sex = "male", wage = 1000.0, bornDate = "2000-01-01", idDept = 1, address = "01001000", timeWorked = "2000-01-01 00:00:00")
                assertEquals(1, accessor.delete(emp))
                println("Success!")
            } catch (e: Exception) {
                println("Test failed due to: ${e.message}")
            }
        }
    }
}