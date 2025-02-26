package org.example.cahousing.Accessors

import kotlinx.coroutines.processNextEventInCurrentThread
import kotlinx.coroutines.runBlocking
import org.example.cahousing.DataSource.Data.Accessors.DeptAccessor
import org.example.cahousing.DataSource.Data.Accessors.DeptAccessorImp
import org.example.cahousing.DataSource.Models.Dept
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class DeptAccessorImpTest {

    private val accessor: DeptAccessorImp = DeptAccessor()

    @Before
    fun setup() {
        accessor.turnTestOn()
    }

    @After
    fun tearDown() {
        accessor.turnTestOff()
    }

    @Test
    fun `Should create a department and return the number of affected rows`(){
        runBlocking {
            try{
                println("Trying to create a new department...")
                val dept: Dept = Dept(name = "Test", description = "Test")
                assertEquals(1, accessor.create(dept))
                println("Success!!!")
            }catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should get and return a Dept object from database`(){
        runBlocking {
            try {
                println("Trying to get Dept from database...")
                assertEquals(Dept(id = 1, name = "Test", description = "Test"), accessor.get("Test"))
                println("Success!!!")
            } catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should get and return all Dept objects from database`(){
        runBlocking {
            try {
                println("Trying to get all Depts from database...")
                val list: ArrayList<Dept> = accessor.getAll()
                println(list)
                assert(list.size > 0)
                println("Success!!!")
            }catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should update and return the number of affected rows`(){
        runBlocking {
            try {
                println("Trying to update a department...")
                val dept: Dept = Dept(name = "Test", description = "Test")
                val data: Dept = Dept(name = "Test2", description =  "Test2")
                assertEquals(2, accessor.update(dept, data))
                println("Success!!!")
            } catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should delete and return the number of affected rows`(){
        runBlocking {
            try {
                println("Trying to delete a department...")
                val dept: Dept = Dept(name = "Test2", description = "Test2")
                assertEquals(1, accessor.delete(dept))
                println("Success!!!")
            } catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }
}