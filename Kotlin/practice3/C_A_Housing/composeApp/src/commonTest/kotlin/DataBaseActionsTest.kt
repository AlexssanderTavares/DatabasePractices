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
import org.example.cahousing.DataSource.Utilities.PostalCodeFormatter
import org.junit.Assert.assertNotEquals
import java.sql.SQLException
import kotlin.math.truncate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class DataBaseActionsTest {
    private val dbActions: DataBaseActions = DataBaseActions()
    private val formatter: PostalCodeFormatter = PostalCodeFormatter()

    // DEPT CONTEXT
    
    @Test
    fun ShouldCreateANewDeptAndReturnTheNumberOfAffectedRows() {
        runBlocking {
            try {
                val dept: Dept = Dept(name = "Test Department", description = "A department for tests purposes.")
                println("Trying to create a new register in Dept table.")
                assertEquals(1, dbActions.createDept(dept))
                println("Test succeed!")
            } catch (e: Exception) {
                println("Failed to insert data due to a method error | ${e.message}")
                fail("Test failed")
            }
        }
    }

    @Test
    fun ShouldUpdateDeptAndReturnTheNumberOfAffectedRows(){
        runBlocking {
            try{
                println("Trying to get Dept and update it fields by passing Strings arguments")
                val oldData: Dept = Dept(name = "Test Department", description = "A department for test purpose.")
                val newData: Dept = Dept(name = "Test Department", description = "Updated description")
                assertEquals(1, dbActions.updateDept(oldData, newData))
                println("Test Succeed!")
            } catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun ShouldGetADeptDataAndReturnObject() {
        runBlocking {
            try {
                println("Trying to return Dept table data")
                val oldData: Dept = Dept(name = "Test Department", description = "A department for test purpose.")
                val target: Dept? = dbActions.getDept(oldData.name)
                println("Data returned: ${target.toString()}")
                assertNotEquals(null, target)
                assertEquals(1, target?.id)
                assertEquals("Test Department", target?.name)
                println("Test succeed!")
            } catch (e: Exception) {
                e.printStackTrace()
                println("Failed to insert data due to a method error | ${e.message}")
                fail("Test failed")
            }
        }
    }

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

    @Test
    fun ShouldDeleteDeptAndReturnTheNumberOfAffectedRows(){
        runBlocking {
            try{
                println("Trying to delete Dept data...")
                val oldData: Dept = Dept(name = "Test Department", description = "A department for test purpose.")
                assertEquals(1, dbActions.deleteDept(oldData))
                println("Test Succeed")
            }catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    // ADDRESS CONTEXT

    @Test
    fun ShouldCreateANewAddressAndReturnTheNumberOfAffectedRows() {
        runBlocking {
            try {
                val address: Address = Address("11715550", "Rua Frei Antonio do Salvamento, 253", "Ribeirão", "Praia Pequena")
                println("Trying to create a new register in Address table.")
                assertEquals(1, dbActions.createAddress(address))
                println("Test succeed!")
            } catch (e: Exception) {
                e.printStackTrace()
                println("Failed to insert data due to a method error | ${e.message}")
                fail("Test failed")
            }
        }
    }

    @Test
    fun ShouldUpdateAddressAndReturnTheNumberOfAffectedRows() {
        runBlocking {
            try {
                println("Trying to update Address data...")
                val address: Address = Address("11715550","Rua Frei Antonio do Salvamento, 253", "Ribeirão", "Praia Pequena")
                val address2: Address = Address("11715550","Rua Frei Padre do Balão, 257", "Ribeiros", "Praia Longa")
                assertEquals(1, dbActions.updateAddress(address, address2))
                println("Test Succeed")
            }catch (e:Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun ShouldGetAnAdressDataFromDataBaseAndReturnObject() {
        runBlocking {
            try {
                println("Trying to return data from table Address.")
                val address: Address = Address("11715550","Rua Frei Antonio do Salvamento, 253", "Ribeirão", "Praia Pequena")
                val target: Address? = dbActions.getAddress(address.cep)
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

    @Test
    fun ShouldDeleteAddressAndReturnTheNumberOfAffectedRows(){
        runBlocking {
            try {
                println("Trying to delete an Address data...")
                val address: Address = Address(formatter.toCepFormat("11715550"),"Rua Frei Antonio do Salvamento, 253", "Ribeirão", "Praia Pequena")
                assertEquals(1, dbActions.deleteAddress(address))
                println("Test Succeed!")
            } catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    // EMPLOYEE CONTEXT

    @Test
    fun ShouldCreateANewEmployeeAndReturnTheNumberOfAffectedRows() {
        runBlocking {
            try {
                val employee: Employee = Employee(name ="TestBot", sex = "Female", wage = 1357.51, bornDate = "1996-04-04", address = "11715550", idDept = 1)
                println("Trying to create a new register in Employee table.")
                assertEquals(
                    1,
                    dbActions.createEmployee(employee)
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
    fun ShouldUpdateEmployeeAndReturnTheNumberOfAffectedRows() {
        runBlocking {
          try {
              println("Trying to update Employee data...")
              val employee: Employee = dbActions.getEmployee("TestBot")!!
              val newEmp = Employee(name = "TestBot", wage = 6000.00, address = "11715550", idDept = 1, sex = "Male")
              assertEquals(1, dbActions.updateEmployee(employee, newEmp))
              println("Test Succeed")
          } catch (e:Exception){
              fail("Test failed due to: ${e.message}")
          }
        }
    }

    @Test
    fun ShouldGetAnEmployeeDataFromDataBaseAndReturnObject() {
        runBlocking {
            try {
                println("Trying to return data from Employee table.")
                val target: Employee? = dbActions.getEmployee("TestBot")
                println("Object returned: ${target}")
                assertNotEquals(null, target)
                assertEquals("TestBot", target?.name)
                assertEquals(1357.51, target?.wage)
                assertEquals("Female", target?.sex)
                assertEquals("1996-04-04", target?.bornDate)
                assertEquals("0000-00-00 00:00:00", target?.timeWorked)
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
              println("Trying to delete Employee data...")
              val employee: Employee = Employee(name ="TestBot", sex = "Female", wage = 1357.51, bornDate = "1996-04-04", address = "11715550", idDept = 1)
              assertEquals(1, dbActions.deleteEmployee(employee))
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
                val project: Project = Project("Test Project", 1)
                println("Trying to create a new register in Project table")
                assertEquals(1, dbActions.createProject(project))
                println("Test Succeed")
            } catch (e: Exception) {
                e.printStackTrace()
                println("Failed to insert data due to a method error | ${e.message}")
                fail("Test failed")
            }
        }
    }

    @Test
    fun ShouldUpdateProjectAndReturnTheNumberOfAffectedRows() {
        runBlocking {
            try {
                println("Trying to update Project data...")
                val project: Project = dbActions.getProject("Test Project")!!
                assertEquals(1, dbActions.updateProject(project, Project("Updated test", 1)))
                println("Test Succeed")
            }catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun ShouldGetAProjectDataFromDataBaseAndReturnObject() {
        runBlocking {
            try {
                println("Trying to return data from Project table.")
                val target: Project? = dbActions.getProject("Test Project")
                println("Object returned: ${target}")
                assertNotEquals(null, target)
                assertEquals("Test Project", target?.name)
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
                val project: Project = Project("Updated Test", 1)
                println("Trying to delete a Project register from database...")
                assertEquals(1, dbActions.deleteProject(project))
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
                val project: Project = Project("Test Project", 1)
                val employee: Employee = Employee(name ="TestBot", sex = "Female", wage = 1357.51, bornDate = "1996-04-04", address = "11715550", idDept = 1)
                println("Trying to create a new Contract register on table Employee_Project")
                assertEquals(1, dbActions.createProjectContract(project, employee))
                println("Test Succeed")
            } catch (e: Exception) {
                e.printStackTrace()
                fail("Failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun ShouldUpdateContractAndReturnTheNumberOfAffectedRows() {
        runBlocking {
            try {
                println("Trying to update Contract data...")
                val storedContract: ProjectEmployeeContract = dbActions.getProjectContract("Test Project")!!
                val newContract = storedContract
                newContract.description = "Test updated description"
                assertEquals(1, dbActions.updateProjectContract(storedContract, newContract))
                println("Test Succeed")
            } catch (e: Exception) {
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun ShouldGetContractEmployee_ProjectWhenPassingProjectNameAsArgument(){
        runBlocking {
            try{
                println("Trying to get registered contract by project name...")
                val project: Project = Project("Test Project", 1)
                val employee: Employee = Employee(name ="TestBot", sex = "Female", wage = 1357.51, bornDate = "1996-04-04", address = formatter.toCepFormat("11715550"), idDept = 1)

                val target: ProjectEmployeeContract? = dbActions.getProjectContract(project.name)
                assertNotEquals(null, target)
                println("Returned object: ${target}")
                employee.id = target?.employee?.id
                //employee.timeWorked = target?.employee?.timeWorked ?: "0000-00-00 00:00:00"
                assertEquals(project, target?.project)
                assertEquals(employee, target?.employee)
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
                val project: Project = Project("Test Project", 1)
                val employee: Employee = Employee(name ="TestBot", sex = "Female", wage = 1357.51, bornDate = "1996-04-04", address = "11715550", idDept = 1)
                val contract: ProjectEmployeeContract = ProjectEmployeeContract(project = project, employee = employee)
                assertEquals(1, dbActions.deleteProjectContract(contract))
                println("Test Succeed!")
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
                println("Trying to create an Overseer register using an existent Employee data...")
                val overseer: Overseer = Overseer(empName = "TestBot", wage = 4000.00)
                println(overseer)
                assertEquals(1, dbActions.createOverseer(overseer))
            } catch (e: Exception) {
                e.printStackTrace()
                fail("Failed to create data due to: ${e.message}")
            }
        }
    }

    @Test
    fun ShouldUpdateOverseerAndReturnTheNumberOfAffectedRows(){
        runBlocking {
            try{
                println("Trying to update overseer data...")
                val oldData = Overseer(empName = "TestBot", wage = 4000.00)
                val newData = Overseer(empName = "TestBot", wage = 5000.00, timeWorked = "2025-01-15 08:00:00")
                println("Objects tested | Old Data: ${oldData} | New Data: ${newData}")
                assertEquals(1,dbActions.updateOverseer(oldData, newData))
                println("Test Succeed!")
            } catch (e: Exception){
                fail("Test failed due to: ${e.message}")
            }
        }
    }

    @Test
    fun ShouldGetOverseerDataAndReturnItsObject(){
        runBlocking {
            try{
                println("Trying to get Overseer data from database...")
                val overseer: Overseer = Overseer(empName="TestBot", wage = 4000.00)
                val target: Overseer? = dbActions.getOverseer(overseer.empName)
                assertNotEquals(null, target)
                assertEquals("TestBot", target?.empName)
                assertEquals(4000.00, target?.wage)
                assertEquals("0000-00-00 00:00:00", target?.timeWorked)
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
                val data: Overseer = Overseer(empName = "TestBot", wage = 4000.00)
                assertEquals(1, dbActions.deleteOverseer(data))
                println("Test Succeed!")
            } catch (e: Exception) {
                e.printStackTrace()
                fail("Test failed due to: ${e.message}")
            }
        }
    }
}