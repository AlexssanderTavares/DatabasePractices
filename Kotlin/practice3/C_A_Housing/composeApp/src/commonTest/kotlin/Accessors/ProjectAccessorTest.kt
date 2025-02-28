package org.example.cahousing.Accessors

import kotlinx.coroutines.runBlocking
import org.example.cahousing.DataSource.Data.Accessors.ProjectAccessor
import org.example.cahousing.DataSource.Data.Accessors.ProjectAccessorImp
import org.example.cahousing.DataSource.Models.Project
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ProjectAccessorTest {

    private val accessor: ProjectAccessorImp = ProjectAccessor()

    @Before
    fun setup(){
        accessor.turnTestOn()
    }

    @After
    fun tearDown(){
        accessor.turnTestOff()
    }

    @Test
    fun `Should create a new Project and return the number of affected rows`(){
        runBlocking {
            try{
                println("Trying to create a new Project...")
                val project: Project = Project("ProjectTest", 1)
                assertEquals(1, accessor.create(project))
                println("Success!!!")
            }catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should get a Project from database by its name and return it as object`(){
        runBlocking {
            try{
                println("Trying to get a Project...")
                val project: Project? = accessor.get("ProjectTest")
                println("Success!!!")
                assertEquals("ProjectTest", project?.name)
            }catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should get all Projects from database and return them as an ArrayList`(){
        runBlocking {
            try{
                println("Trying to get all Projects...")
                val list: ArrayList<Project> = accessor.getAll()
                println(list)
                assert(list.size > 0)
                println("Success!!!")
            }catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun `Should update a Project and return the number of affected rows`(){
        runBlocking {
            try {
                println("Trying to update a Project...")
                val project: Project = Project("ProjectTest", 2)
                val data: Project = Project("UpdatedProjectTest", 2)
                assertEquals(1, accessor.update(project, data))
            } catch (e: Exception){
                fail("Test failed due to: ${e.message}")

            }
        }
    }

    @Test
    fun `Should delete a Project and return the number of affected rows`() {
        runBlocking {
            try {
                println("Trying to delete a Project...")
                val project: Project = Project("UpdatedProjectTest", 2)
                assertEquals(1, accessor.delete(project))
                println("Success!!!")
            } catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }
}