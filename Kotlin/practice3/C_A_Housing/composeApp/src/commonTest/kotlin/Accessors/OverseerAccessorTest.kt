package org.example.cahousing.Accessors

import kotlinx.coroutines.runBlocking
import org.example.cahousing.DataSource.Data.Accessors.EmployeeAccessor
import org.example.cahousing.DataSource.Data.Accessors.EmployeeAccessorImp
import org.example.cahousing.DataSource.Data.Accessors.OverseerAccessor
import org.example.cahousing.DataSource.Data.Accessors.OverseerAccessorImp
import org.example.cahousing.DataSource.Models.Employee
import org.example.cahousing.DataSource.Models.Overseer
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class OverseerAccessorTest {

    private val overseerAccessor: OverseerAccessorImp = OverseerAccessor()
    private val empAccessor: EmployeeAccessorImp = EmployeeAccessor()


    @Before
    fun setup(){
        overseerAccessor.turnTestOn()
        empAccessor.turnTestOn()
    }

    @After
    fun tearDown(){
        overseerAccessor.turnTestOff()
        empAccessor.turnTestOff()
    }

    @Test
    fun `Should create a Overseer and return the number of affected rows`(){
        runBlocking {
            try{
                println("Trying to create an Overseer...")
                val employee = Employee(name = "test2", sex = "male", wage = 1000.0, bornDate = "2000-01-01", idDept = 1, address = "01001000", timeWorked = "2000-01-01 00:00:00")
                println(employee)
                empAccessor.create(employee)
                val overseer = Overseer(empName = employee.name, wage = employee.wage, timeWorked = employee.timeWorked)
                assertEquals(1, overseerAccessor.create(overseer))
                println("Success!!!")
            }catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should get an Overseer by name and return it as object`(){
        runBlocking {
            try{
                println("Trying to get an Overseer...")
                assertEquals(Overseer(id = 1, empName = "test2", wage = 1000.0, timeWorked = "2000-01-01 00:00:00"), overseerAccessor.get("test2"))
                println("Success!!!")
            }catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should get all Overseers and return them as list of objects`(){
        runBlocking{
            try{
                println("Trying to get all Overseers...")
                val list = overseerAccessor.getAll()
                println(list)
                assert(list.size > 0)
                println("Success!!!")
            }catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should update Overseer and return the number of affected rows`(){
        runBlocking {
            try {
                println("Trying to update an Overseer...")
                val overseer: Overseer = overseerAccessor.get("test2")!!
                val data: Overseer = Overseer(
                    id = 1,
                    empName = "test2",
                    wage = 3000.0,
                    timeWorked = "2000-01-01 10:30:00"
                )
                assertEquals(1, overseerAccessor.update(overseer, data))
                println("Success!!!")
            } catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should delete Overseer and return the number of affected rows`(){
        runBlocking {
            try {
                println("Trying to delete an Overseer...")
                val overseer: Overseer = Overseer(
                    id = 1,
                    empName = "test2",
                    wage = 3000.0,
                    timeWorked = "2000-01-01 10:30:00"
                )
                assertEquals(1, overseerAccessor.delete(overseer))
                println("Success!!!")
            } catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }
}