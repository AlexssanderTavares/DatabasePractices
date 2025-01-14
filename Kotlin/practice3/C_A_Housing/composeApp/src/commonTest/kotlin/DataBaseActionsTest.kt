package org.example.cahousing

import androidx.compose.ui.Modifier
import kotlinx.coroutines.runBlocking
import org.example.cahousing.DataSource.Data.DataBaseActions
import org.example.cahousing.DataSource.Models.Address
import org.example.cahousing.DataSource.Models.Dept
import org.example.cahousing.DataSource.Models.Employee
import org.example.cahousing.DataSource.Models.Overseer
import org.example.cahousing.DataSource.Models.Project
import org.example.cahousing.DataSource.Models.ProjectEmployeeContract
import org.junit.Assert.assertNotEquals
import java.sql.SQLException
import kotlin.math.truncate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class DataBaseActionsTest {
    private val dbActions: DataBaseActions = DataBaseActions()

    // SUPPORT METHODS
    @Test
    fun MustReceiveAndFormatAStringNumberToCepAddressFormatAndReturnAString() {
        try {
            println("Trying to format String passed as argument")
            val str: String = dbActions.toCepFormat("11714450")
            println("CEP formated String: ${str}")
            assertEquals("11.714-450", str)
            println("Test succeed!")
        } catch (e: IllegalArgumentException) {
            println("Error due to an invalid argument passed | ${e.message}")
            fail("Test failed!")
        }
    }

    // DEPT CONTEXT

    @Test
    fun ShouldCreateANewDeptAndReturnTheNumberOfAffectedRows() {
        runBlocking {
            try {
                println("Trying to create a new register in Dept table.")
                assertEquals(1, dbActions.createDept("Test"))
                println("Test succeed!")
            } catch (e: Exception) {
                println("Failed to insert data due to a method error | ${e.message}")
                fail("Test failed")
            }
        }
    }

    @Test
    fun ShouldGetADeptDataAndReturnObject() {
        runBlocking {
            try {
                println("Trying to return Dept table data")
                val target: Dept? = dbActions.getDept("UpdatedTestDummy")
                println("Data returned: ${target.toString()}")
                assertNotEquals(null, target)
                assertEquals(1, target?.id)
                assertEquals("UpdatedTestDummy", target?.name)
                println("Test succeed!")
            } catch (e: Exception) {
                e.printStackTrace()
                println("Failed to insert data due to a method error | ${e.message}")
                fail("Test failed")
            }
        }
    }

    @Test
    fun ShouldUpdateDeptFieldsAndReturnTheNumberOfAffectedRows(){
        runBlocking {
            try{
                println("Trying to get Dept and update it fields by passing Strings arguments")
                println("Updating name...")
                assertEquals(1, dbActions.updateDept("", "Super Updated Test Dummy"))
                println("Ok...")
                println("Updating description... ")
                assertEquals(1, dbActions.updateDept("Super Updated Test Dummy", "", "I am super updated again"))
                println("Ok...")
                println("Updating name and description...")
                assertEquals(1, dbActions.updateDept("", "UpdatedTestDummy", "Updated"))
                println("Ok...")
                println("Test Succeed")
            } catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    /*@Test
    fun ShouldUpdateDataAndReturnTheNumberOfAffectedRows() {
        runBlocking {
            try {
                println("Trying to update Dept name")
                assertEquals(1, dbActions.updateDeptName("Test", "UpdatedTestDummy"))
                println("Trying to update Dept description")
                assertEquals(
                    1,
                    dbActions.updateDeptDescription("UpdatedTestDummy", "This is just a test dummy")
                )
                println("Test Succeed")
            } catch (e: Exception) {
                e.printStackTrace()
                fail("Failed to update data due to a method error | ${e.message}")
            }
        }
    }*/

    @Test
    fun ShouldGetAllRegistersFromDeptAsArrayList(){
        runBlocking {
            try{
                println("Trying to retrieve every registers from Dept table...")
                val res = dbActions.getAllDept()
                res.forEach {
                    println(it)
                }
                assertEquals(false, res.isEmpty())
                println("Test Succeed!")
            } catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }


    // ADDRESS CONTEXT

    @Test
    fun ShouldCreateANewAddressAndReturnTheNumberOfAffectedRows() {
        runBlocking {
            try {
                println("Trying to create a new register in Address table.")
                assertEquals(
                    1,
                    dbActions.createAddress(
                        "11715550",
                        "Rua Frei Antonio do Salvamento, 253",
                        "Ribeirão",
                        "Praia Pequena"
                    )
                )
                println("Test succeed!")
            } catch (e: Exception) {
                e.printStackTrace()
                println("Failed to insert data due to a method error | ${e.message}")
                fail("Test failed")
            }
        }
    }

    @Test
    fun ShouldGetAnAdressDataFromDataBaseAndReturnObject() {
        runBlocking {
            try {
                println("Trying to return data from table Address.")
                val target: Address? = dbActions.getAddress("11715550")
                println("Returned object: ${target}")
                assertNotEquals(null, target)
                assertEquals("11.715-550", target?.cep)
                assertEquals("Rua Frei Antonio do Salvamento, 253", target?.road)
                assertEquals("Ribeirão", target?.district)
                assertEquals("Praia Pequena", target?.city)
                println("Test succeed!")
            } catch (e: Exception) {
                e.printStackTrace()
                println("Failed to insert data due to a method error | ${e.message}")
                fail("Test failed")
            }
        }
    }

    @Test
    fun ShouldGetAllRegistersFromAddressAsArrayList(){
        runBlocking {
            try{
                println("Trying to retrieve every Address registers...")
                val res = dbActions.getAllAddress()
                res.forEach {
                    println(it)
                }

                assertEquals(false, res.isEmpty())
                println("Test Succeed!")
            } catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }
    /*

        @Test
        fun ShouldTryToUpdateDataAndReturnTheNumberOfAffectedRows(){
            runBlocking {
                try {
                    println("Trying to update Address without passing newDist and newCity argument values")
                    assertEquals(1, dbActions.updateAddress("11715550", "15716660", "Rua Frei Gasparsinho, 278"))
                    println("Trying to update Address passing all argument values")
                    assertEquals(1, dbActions.updateAddress("15716660", "12854440", "Av. Aldor Sampaio, 315", "Quebra Pedra", "Diabos"))
                    println("Test Succeed")
                } catch (e: Exception) {
                    e.printStackTrace()
                    fail("Test failed due to a method error | ${e.message}")
                }
            }
        }
    */

    // EMPLOYEE CONTEXT

    @Test
    fun ShouldCreateANewEmployeeAndReturnTheNumberOfAffectedRows() {
        runBlocking {
            try {
                println("Trying to create a new register in Employee table.")
                assertEquals(
                    1,
                    dbActions.createEmployee(
                        "TesteBot5",
                        1357.51,
                        "Female",
                        "1996-04-04",
                        "11715550",
                        1
                    )
                )
                println("Test succeed!")
            } catch (e: Exception) {
                e.printStackTrace()
                println("Failed to insert data due to a method error | ${e.message}")
                fail("Test failed")
            }
        }
    }

    @Test
    fun ShouldGetAnEmployeeDataFromDataBaseAndReturnObject() {
        runBlocking {
            try {
                println("Trying to return data from Employee table.")
                val target: Employee? = dbActions.getEmployee("TesteBot5")
                println("Object returned: ${target}")
                assertNotEquals(null, target)
                assertEquals("TesteBot5", target?.name)
                assertEquals(1357.51, target?.wage)
                assertEquals("Female", target?.sex)
                assertEquals("1996-04-04", target?.bornDate)
                assertEquals(null, target?.timeWorked)
                assertEquals("11.715-550", target?.address)
                assertEquals(1, target?.idDept)
                println("Test succeed!")
            } catch (e: Exception) {
                println("Failed to insert data due to a method error | ${e.message}")
                fail("Test failed")
            }
        }
    }

    @Test
    fun ShouldGetAllEmployeesAsArrayList() {
        runBlocking {
            try{
                println("Trying to retrieve every Employee registers...")
                val res = dbActions.getAllEmployee()
                res.forEach {
                    println(it)
                }

                assertEquals(false, res.isEmpty())
                println("Test Succeed!")
            } catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun ShouldDeleteAnEmployeeDataAndReturnTheNumberOfAffectedRows() {
        runBlocking {
          try {
              assertEquals(1, dbActions.deleteEmployee("TesteBot5"))
              println("Test Succeed!")
          }  catch (e: Exception) {
              e.printStackTrace()
              fail("Test failed due to: ${e.message}")
          }
        }
    }

    // PROJECT CONTEXT

    @Test
    fun ShouldCreateANewProjectAndReturnTheNumberOfAffectedRows() {
        runBlocking {
            try {
                println("Trying to create a new register in Project table")
                assertEquals(1, dbActions.createProject("Product Test Environment", 1))
                println("Test Succeed")
            } catch (e: Exception) {
                e.printStackTrace()
                println("Failed to insert data due to a method error | ${e.message}")
                fail("Test failed")
            }
        }
    }

    @Test
    fun ShouldGetAProjectDataFromDataBaseAndReturnObject() {
        runBlocking {
            try {
                println("Trying to return data from Project table.")
                val target: Project? = dbActions.getProject("Product Test Environment")
                println("Object returned: ${target}")
                assertNotEquals(null, target)
                assertEquals(3, target?.id)
                assertEquals("Product Test Environment", target?.name)
                assertEquals(1, target?.dept)
                println("Test Succeed")
            } catch (e: Exception) {
                e.printStackTrace()
                println("Failed to get data due to a method error | ${e.message}")
                fail("Test failed")
            }
        }
    }

    @Test
    fun ShouldGetAllProjectsAsArrayList() {
        runBlocking {
            try{
                println("Trying to retrieve every Project registers...")
                val res = dbActions.getAllProject()
                res.forEach {
                    println(it)
                }

                assertEquals(false, res.isEmpty())
                println("Test Succeed!")
            } catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun ShouldDeleteAProjectRegisterAndReturnTheNumberOfAffectedRows() {
        runBlocking {
            try{
                println("Trying to delete a Project register from database...")
                assertEquals(1, dbActions.deleteProject("Product Test Environment"))
                println("Test Succeed!")
            } catch (e: Exception) {
                e.printStackTrace()
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    // CONTRACT EMPLOYEE_PROJECT CONTEXT

    @Test
    fun ShouldCreateANewContractAndReturnTheNumberOfAffectedRows() {
        runBlocking {
            try {
                println("Trying to create a new Contract register on table Employee_Project")
                assertEquals(1, dbActions.createProjectContract("Product Test Environment", "TesteBot5"))
                println("Test Succeed")
            } catch (e: Exception) {
                e.printStackTrace()
                fail("Failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun ShouldGetContractEmployee_ProjectWhenPassingProjectNameAsArgument(){
        runBlocking {
            try{
                println("Trying to get registered contract by project name...")
                val target: ProjectEmployeeContract? = dbActions.getProjectContract("Product Test Environment")
                assertNotEquals(null, target)
                println("Returned object: ${target}")
                assertEquals(2, target?.id)
                assertEquals(3, target?.projectNum)
                assertEquals("TesteBot5", target?.empName)
            } catch (e: Exception) {
                e.printStackTrace()
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun ShouldGetAllProjectContractAsArrayList() {
        runBlocking {
            try {
                println("Trying to retrieve every Employee_Project registers...")
                val res = dbActions.getAllProjectContracts()
                res.forEach {
                    println(it)
                }

                assertEquals(false, res.isEmpty())
                println("Test Succeed!")
            }catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun ShouldDeleteContractEmployee_ProjectDataAndReturnNumberOfAffectedRows(){
        runBlocking {
            try {
                println("Trying to delete a Contract Employee_Project register...")
                assertEquals(1, dbActions.deleteProjectContract(1))
                println("Teste Succeed!")
            } catch (e: Exception) {
                e.printStackTrace()
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    // OVERSEER CONTEXT

    @Test
    fun ShouldCreateOverseerAndReturnTheNumberOfAffectedRows(){
        runBlocking {
            try{
                assertEquals(1, dbActions.createOverseer(dbActions.getEmployee("TesteBot5")!!.name, 4000.00))
            } catch (e: Exception) {
                e.printStackTrace()
                fail("Failed to create data due to: ${e.message}")
            }
        }
    }

    @Test
    fun ShouldGetOverseerDataAndReturnItsObject(){
        runBlocking {
            try{
                println("Trying to get Overseer data from database...")
                val target: Overseer? = dbActions.getOverseer("TesteBot5")
                assertNotEquals(null, target)
                assertEquals(2, target?.id)
                assertEquals("TesteBot5", target?.empName)
                assertEquals(4000.00, target?.wage)
                //assertEquals("", target?.timeWorked)
            } catch (e: Exception) {
                e.printStackTrace()
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun ShouldGetAllOverseerAsArrayList(){
        runBlocking {
            try {
                println("Trying to retrieve every Overseer registers...")
                val res = dbActions.getAllOverseer()
                res.forEach {
                    println(it)
                }

                assertEquals(false, res.isEmpty())
                println("Test Succeed!")
            } catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun ShouldDeleteOverseerAndReturnTheNumberOfAffectedRows() {
        runBlocking {
            try {
                println("Trying to delete an Overseer register...")
                assertEquals(1, dbActions.deleteOverseer("TesteBot5"))
                println("Teste Succeed!")
            } catch (e: Exception) {
                e.printStackTrace()
                fail("Test failed due to: ${e.message}")
            }
        }
    }
}