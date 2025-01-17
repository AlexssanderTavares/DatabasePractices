package org.example.cahousing.DataSource.Data

import androidx.compose.runtime.simulateHotReload
import androidx.compose.ui.graphics.prepareTransformationMatrix
import jdk.internal.net.http.common.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.example.cahousing.DataBaseConnection
import org.example.cahousing.DataSource.Models.Address
import org.example.cahousing.DataSource.Models.Dept
import org.example.cahousing.DataSource.Models.Employee
import org.example.cahousing.DataSource.Models.Overseer
import org.example.cahousing.DataSource.Models.Project
import org.example.cahousing.DataSource.Models.ProjectEmployeeContract
import org.example.cahousing.DataSource.Utilities.PostalCodeFormatter
import org.example.cahousing.getPlatform
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp
import java.util.InvalidPropertiesFormatException
import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction
import kotlin.random.Random
import kotlin.random.nextInt


class DataBaseActions {

    private val db: Connection = DataBaseConnection.CONNECTION
    private val formatter: PostalCodeFormatter = PostalCodeFormatter()


    // DEPT
    suspend fun createDept(dept: Dept): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("INSERT INTO Dept (STR_name, STR_description) VALUES ('${dept.name}','${dept.description}');")
                query.execute()
                rows++
                println("Query ok! Rows affected on table Dept: ${rows}")
            } catch (e: SQLException) {
                println(e.message)
                e.printStackTrace()
                println("Failed to create new register. Rows affected: ${rows}")
            }
        }
        job.join()

        return if (job.isCompleted) {
            rows
        } else {
            throw RuntimeException("Process finished before thread routine")
        }
    }

    suspend fun getDept(deptName: String): Dept? {
        lateinit var data: Dept
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("SELECT * FROM Dept WHERE STR_name = '${deptName}';")
                val res: ResultSet = query.executeQuery()
                res.next()

                if (res.row == 1) {
                    data = Dept(
                        res.getInt("I_num_dept"),
                        res.getString("STR_name"),
                        res.getString("STR_description")
                    )
                    println("Query ok! Object returned: ${data}")
                } else {
                    throw SQLException("This register doesn't exist!")
                }

            } catch (e: SQLException) {
                println("Failed to return data due to: ${e.message}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            data
        } else {
            null
        }
    }

    suspend fun updateDept(dept: Dept, data: Dept): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val oldData: Dept? = getDept(dept.name)
                if(oldData != null && data.name == oldData.name){
                    if(oldData.description != data.description){
                        val query: PreparedStatement = db.prepareStatement("UPDATE Dept SET STR_description='${data.description}' WHERE STR_name='${oldData.name}';")
                        query.execute()
                    }
                    rows++
                    println("Query Ok! Number of Affected rows: ${rows}")
                }else{
                    throw SQLException("Data doesn't match. ${dept} and ${data} is not the same.")
                }

            } catch (e: SQLException) {
                println("Failed to update data due to: ${e.message}.")
                e.printStackTrace()
                println("Failed to update data. Rows affected: ${rows}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            rows
        } else {
            throw RuntimeException("Process finished before thread routine.")
        }

    }

    suspend fun deleteDept(dept: Dept): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("DELETE FROM Dept WHERE STR_name='${dept.name}';")
                query.execute()
                rows++
                println("Query Ok! Number of affected rows: ${rows}")
            } catch (e: SQLException) {
                e.printStackTrace()
                println("Failed to delete data due to: ${e.message}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            rows
        } else {
            rows
        }
    }

    suspend fun getAllDept(): ArrayList<Dept> {
        lateinit var dept: Dept
        val list: ArrayList<Dept> = ArrayList<Dept>()
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement = db.prepareStatement("SELECT * FROM Dept;")
                val res: ResultSet = query.executeQuery()

                while (res.next()) {
                    dept = Dept(
                        res.getInt("I_num_dept"),
                        res.getString("STR_name"),
                        res.getString("STR_description")
                    )
                    list.add(dept)
                }

                println("Query OK! Number of retrieved rows: ${list.size}")
            } catch (e: SQLException) {
                println("Failed to retrieve data due to: ${e.message}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            list
        } else {
            list
        }
    }

    // ADDRESS
    suspend fun createAddress(address: Address): Int {
        var rows: Int = 0
        if (formatter.isValid(address.cep)) {

            val job: Job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val query: PreparedStatement =
                        db.prepareStatement(
                            "INSERT INTO Address (I_cep, STR_road, STR_district, STR_city) VALUES ('${formatter.toCepFormat(address.cep)}','${address.road}','${address.district}','${address.city}');")
                    query.execute()
                    rows++
                    println("Query ok! Rows affected on table Address: ${rows}")
                } catch (e: SQLException) {
                    println(e.message)
                    e.printStackTrace()
                    println("Failed to create new register. Rows affected: ${rows}")
                }
            }
            job.join()

            return if (job.isCompleted) {
                rows
            } else {
                throw RuntimeException("Process finished before thread routine.")
            }
        } else {
            return rows
        }
    }

    suspend fun updateAddress(address: Address, newData: Address): Int {
        var rows: Int = 0
        address.cep = formatter.toCepFormat(address.cep)
        newData.cep = formatter.toCepFormat(newData.cep)

        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            if (formatter.isValid(address.cep)) {
                try {

                    val data: Address? = getAddress(address.cep)
                    if(data != null && data.cep == address.cep){

                        if(data.road != newData.road) {
                            val query: PreparedStatement =
                                db.prepareStatement("UPDATE Address SET STR_road='${newData.road}' WHERE I_cep='${data.cep}';")
                            query.execute()
                        }

                        if(data.city != newData.city){
                            val query: PreparedStatement =
                                db.prepareStatement("UPDATE Address SET STR_city='${newData.city}' WHERE I_cep='${data.cep}';")
                            query.execute()
                        }

                        if(data.district != newData.district){
                            val query: PreparedStatement =
                                db.prepareStatement("UPDATE Address SET STR_district='${newData.district}' WHERE I_cep='${data.district}';")
                            query.execute()
                        }
                        rows++
                        println("Query OK! Number of affected rows: ${rows}.")
                    }

                } catch (e: SQLException) {
                    println("Failed to update data due to: ${e.message}. Rows affected: ${rows}")
                    e.printStackTrace()

                } catch (e: IllegalArgumentException){
                    println("Failed to update data due to: ${e.message}. Rows affected: ${rows}")
                    e.printStackTrace()
                }
            }
        }
        job.join()
        return if (job.isCompleted) {
            rows
        } else {
            rows
        }
    }

    suspend fun getAddress(cep: String): Address? {
        if(!formatter.isValid(cep)) {
            throw IllegalArgumentException("${cep} is not a valid postal code.")
        }
        lateinit var data: Address

        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("SELECT * FROM Address WHERE I_cep='${cep}';")
                val res: ResultSet = query.executeQuery()
                res.next()

                if (res.row == 1) {
                    data = Address(
                        res.getString("I_cep"),
                        res.getString("STR_road"),
                        res.getString("STR_district"),
                        res.getString("STR_city")
                    )
                    println("Query ok! Object returned: ${data}")
                } else {
                    throw SQLException("Register doesn't exist!")
                }
            } catch (e: SQLException) {
                println("Failed to return data due to: ${e.message}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            data
        } else {
            null
        }
    }

    suspend fun getAllAddress(): ArrayList<Address> {
        lateinit var address: Address
        val list: ArrayList<Address> = ArrayList<Address>()
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement = db.prepareStatement("SELECT * FROM Address;")
                val res: ResultSet = query.executeQuery()

                while (res.next()) {
                    address = Address(
                        res.getString("I_cep"),
                        res.getString("STR_road"),
                        res.getString("STR_district"),
                        res.getString("STR_city")
                    )
                    list.add(address)
                }

                println("Query OK! Number of retrieved rows: ${list.size}")
            } catch (e: SQLException) {
                println("Failed to retrieve data due to: ${e.message}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            list
        } else {
            list
        }
    }

    suspend fun deleteAddress(address: Address) : Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try{
                val data: Address = getAddress(address.cep) ?: throw NullPointerException("There is no such data.")
                val query: PreparedStatement = db.prepareStatement("DELETE FROM Address WHERE I_cep='${address.cep}';")
                query.execute()
                rows++
                println("Query Ok! Number of affected rows: ${rows}.")
            } catch (e: SQLException) {
                println("Failed to delete data due to: ${e.message}. Rows affected: ${rows}")
                e.printStackTrace()
            }
        }

        job.join()
        return if(job.isCompleted){
            rows
        } else{
            rows
        }
    }

    // EMPLOYEE
    suspend fun createEmployee(employee: Employee): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement = db.prepareStatement(
                    "INSERT INTO Employee (STR_name, I_ID, F_wage, STR_sex, dt_born_date, I_ADDRESS_cep, I_DEPT_num) VALUES ('${employee.name}', '${
                        Random.nextInt(Math.round(1111F)..Math.round(9999F))
                    }', '${employee.wage}', '${employee.sex}', '${employee.bornDate}', '${
                        formatter.toCepFormat(
                            employee.address
                        )
                    }', '${employee.idDept}');"
                )
                query.execute()
                rows++
                println("Query ok! Rows affected on table Employee: ${rows}")
            } catch (e: SQLException) {
                println(e.message)
                e.printStackTrace()
                println("Failed to create a new register. Rows affected: ${rows}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            rows
        } else {
            rows
        }
    }

    suspend fun updateEmployee(employee: Employee, vararg newData: Any): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try{
                if(getEmployee(employee.name) != null) {
                    when(newData.size){
                        1 -> {
                            if(newData[0] is Double) {
                                val query: PreparedStatement = db.prepareStatement("UPDATE Employee SET F_wage='${newData[0]}' WHERE STR_name='${employee.name}';")
                                query.execute()
                                rows++
                            } else{
                                throw IllegalArgumentException("The data must follow it respective type. The first must be Double.")
                            }
                        }

                        2 -> {
                            if(newData[0] is Double && newData[1] is String){
                                //must create a string formatter for timestamp data pattern
                                val query: PreparedStatement = db.prepareStatement("UPDATE Employee SET F_wage='${newData[0]}', time_worked_journey='${newData[1]}' WHERE STR_name='${employee.name}';")
                                query.execute()
                                rows++
                            } else {
                                throw IllegalArgumentException("The data must follow it respective type. The first must be Double and the second must be String. Number of rows affected: ${rows}")
                            }
                        }

                        3 -> {
                            if(newData[0] is Double && newData[1] is String && newData[2] is String) {
                                val query: PreparedStatement = db.prepareStatement("UPDATE Employee SET F_wage='${newData[0]}', time_worked_journey='${newData[1]}', I_ADDRESS_cep='${formatter.toCepFormat(newData[2].toString())}' WHERE STR_name='${employee.name}';")
                                query.execute()
                                rows++
                            } else {
                                throw IllegalArgumentException("The data must follow it respective type. The first must be Double and the second must be String. Number of rows affected: ${rows}")
                            }
                        }

                        4 -> {
                            if(newData[0] is Double && newData[1] is String && newData[2] is String && newData[3] is Int) {
                                val query: PreparedStatement = db.prepareStatement("UPDATE Employee SET F_wage='${newData[0]}', time_worked_journey='${newData[1]}', I_ADDRESS_cep='${formatter.toCepFormat(newData[2].toString())}', I_DEPT_num='${newData[3]}' WHERE STR_name='${employee.name}';")
                                query.execute()
                                rows++
                            } else {
                                throw IllegalArgumentException("The data must follow it respective type. The first must be Double and the second must be String. Number of rows affected: ${rows}")
                            }
                        }
                        else -> {
                            throw IllegalArgumentException("The arguments must fulfill the right sequence: [DOUBLE,STRING,STRING,INT] in order to be valid. Number of rows affected: ${rows}")
                        }
                    }
                }
            }catch (e: SQLException){
                println("Failed to update data due to: ${e.message}. Rows affected: ${rows}")
                e.printStackTrace()
            }
        }
        job.join()
        return if(job.isCompleted){
            rows
        } else {
            rows
        }
    }

    suspend fun getEmployee(name: String): Employee? {
        lateinit var employee: Employee
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("SELECT * FROM Employee WHERE STR_name = '${name}';")
                val res: ResultSet = query.executeQuery()
                res.next()

                if (res.row == 1) {
                    employee = Employee(
                        res.getInt("I_ID"),
                        res.getString("STR_name"),
                        res.getString("STR_sex"),
                        res.getDouble("F_wage"),
                        res.getDate("dt_born_date")?.toString() ?: "0000-00-00",
                        res.getTimestamp("time_worked_journey")?.toString() ?: "0000-00-00 00:00:00",
                        res.getString("I_ADDRESS_cep"),
                        res.getInt("I_DEPT_num")
                    )
                    println("Query ok! Object returned: ${employee}")
                } else {
                    throw SQLException("Register doesn't exit.!")
                }
            } catch (e: SQLException) {
                println("Failed to return due to: ${e.message}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            employee
        } else {
            null
        }
    }

    suspend fun getAllEmployee(): ArrayList<Employee> {
        lateinit var emp: Employee
        val list: ArrayList<Employee> = ArrayList<Employee>()
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement = db.prepareStatement("SELECT * FROM Employee;")
                val res: ResultSet = query.executeQuery()

                while (res.next()) {
                    emp = Employee(
                        res.getInt("I_ID"),
                        res.getString("STR_name"),
                        res.getString("STR_sex"),
                        res.getDouble("F_wage"),
                        res.getDate("dt_born_date").toString(),
                        res.getTimestamp("time_worked_journey").toString(),
                        res.getString("I_ADDRESS_cep"),
                        res.getInt("I_DEPT_num")
                    )
                    list.add(emp)
                }

                println("Query OK! Number of retrieved rows: ${list.size}")
            } catch (e: SQLException) {
                println("Failed to retrieve data due to: ${e.message}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            list
        } else {
            list
        }
    }

    suspend fun deleteEmployee(employee: Employee): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("DELETE FROM Employee WHERE STR_name='${employee.name}';")
                val task1: Deferred<Int> = async {
                    val subQuery1: PreparedStatement =
                        db.prepareStatement("SELECT * FROM Employee_Project WHERE STR_employee='${employee.name}';")
                    val res1: ResultSet = subQuery1.executeQuery()
                    res1.next()
                    if (res1.row == 0) {
                        0
                    } else {
                        val contract = ProjectEmployeeContract(res1.getInt("id_contract"), getProject(res1.getString("STR_project"))!!, getEmployee(employee.name)!! )
                        deleteProjectContract(contract)
                    }
                }
                val task2: Deferred<Int> = async {
                    val subQuery2: PreparedStatement =
                        db.prepareStatement("SELECT * FROM Overseer WHERE STR_EMP_name='${employee.name}';")
                    val res2: ResultSet = subQuery2.executeQuery()
                    res2.next()

                    if (res2.row == 0) {
                        0
                    } else {
                        val overseer = Overseer(res2.getInt("I_id"), res2.getString("STR_EMP_name"), res2.getDouble("F_wage"), res2.getTimestamp("time_worked_journey")?.toString() ?: "0000-00-00 00:00:00")
                        deleteOverseer(overseer)
                    }

                }
                rows += task1.await()
                rows += task2.await()

                query.execute()
                rows++
                println("Query Ok! Number of affected rows: ${rows}")
            } catch (e: SQLException) {
                e.printStackTrace()
                println("Failed to delete dat due to: ${e.message}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            rows
        } else {
            rows
        }
    }

    // PROJECT
    suspend fun createProject(project: Project): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("INSERT INTO Project (STR_NAME, I_num_dept) VALUES ('${project.name}', '${project.dept}');")
                query.execute()
                rows++
                println("Query ok! Rows affected on table Project: ${rows}")
            } catch (e: SQLException) {
                println(e.message)
                e.printStackTrace()
                println("Failed to create a new register. Rows affected: ${rows}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            rows
        } else {
            rows
        }
    }

    suspend fun updateProject(project: Project, vararg newData: Any) : Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                if(getProject(project.name) != null) {
                    when(newData.size) {
                        1 -> {
                            if(newData[0]  is String) {
                                val query: PreparedStatement = db.prepareStatement("UPDATE Project SET STR_name='${newData[0]}' WHERE STR_name='${project.name}';")
                                query.execute()
                                rows++
                                println("Query Ok! Number of affected rows: ${rows}")
                            }
                        }

                        2 -> {
                            if(newData[0]  is String && newData[1] is Int) {
                                val query: PreparedStatement = db.prepareStatement("UPDATE Project SET STR_name='${newData[0]}', I_num_dept='${newData[1]}' WHERE STR_name='${project.name}';")
                                query.execute()
                                rows++
                                println("Query Ok! Number of affected rows: ${rows}")
                            }
                        }
                        else -> {
                            throw IllegalArgumentException("The arguments must fulfill the right sequence: [STRING, INT] in order to be valid. Number of rows affected: ${rows}")
                        }
                    }
                }

            } catch (e: SQLException) {
                println("Failed to update data due to: ${e.message}. Rows affected: ${rows}")
                e.printStackTrace()
            }
        }
        job.join()
        return if(job.isCompleted) {
            rows
        }else {
            rows
        }
    }

    suspend fun getProject(projectName: String): Project? {
        lateinit var project: Project
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("SELECT * FROM Project WHERE STR_NAME = '${projectName}';")
                val res: ResultSet = query.executeQuery()
                res.next()

                if (res.row == 1) {
                    project = Project(
                        res.getString("STR_NAME"),
                        res.getInt("I_num_dept")
                    )
                    println("Query ok! Object returned: ${project}")
                } else {
                    throw SQLException("Register doesn't exist")
                }
            } catch (e: SQLException) {
                println("Failed to return due to: ${e.message}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            project
        } else {
            null
        }
    }

    suspend fun getAllProject(): ArrayList<Project> {
        lateinit var project: Project
        val list: ArrayList<Project> = ArrayList<Project>()
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement = db.prepareStatement("SELECT * FROM Project;")
                val res: ResultSet = query.executeQuery()

                while (res.next()) {
                    project = Project(
                        res.getString("STR_name"),
                        res.getInt("I_num_dept")
                    )
                    list.add(project)
                }

                println("Query OK! Number of retrieved rows: ${list.size}")
            } catch (e: SQLException) {
                println("Failed to retrieve data due to: ${e.message}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            list
        } else {
            list
        }
    }

    suspend fun deleteProject(project: Project): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("DELETE FROM Project WHERE STR_name='${project.name}';")
                val task: Deferred<Int> = async {
                    val subQuery1: PreparedStatement =
                        db.prepareStatement("SELECT * FROM Project WHERE STR_name='${project.name}';")
                    val res1: ResultSet = subQuery1.executeQuery()
                    res1.next()

                    val subQuery2: PreparedStatement = db.prepareStatement(
                        "SELECT * FROM Employee_Project WHERE STR_project='${
                            res1.getString(getProject(project.name)?.name)
                        }';"
                    )
                    val res2: ResultSet = subQuery2.executeQuery()
                    res2.next()

                    if (res2.row == 0) {
                        0
                    } else {
                        val contract = ProjectEmployeeContract(res2.getInt("id_contract"), getProject(res2.getString(project.name))!!, getEmployee(res2.getString("STR_employee"))!!)
                        deleteProjectContract(contract)
                    }
                }

                rows += task.await()
                query.execute()
                rows++
                println("Query Ok! Number of affected rows: ${rows}")
            } catch (e: SQLException) {
                e.printStackTrace()
                println("Failed to delete data due to: ${e.message}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            rows
        } else {
            rows
        }
    }

    // CONTRACT EMPLOYEE_PROJECT

    suspend fun createProjectContract(project: Project, employee: Employee): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val projectData: Project? = getProject(project.name)
                val employeeData: Employee? = getEmployee(employee.name)

                if (projectData != null && employeeData != null) {
                    val query: PreparedStatement =
                        db.prepareStatement("INSERT INTO Employee_Project (STR_project, STR_employee) VALUES ('${projectData.name}', '${employeeData.name}');")
                    query.execute()
                    rows++
                    println("Query ok! Number of affected rows: ${rows}")
                } else {
                    throw NullPointerException(
                        "Failed to create a new register due to null values returned | Object 1: ${projectData} | Object 2: ${employeeData}|" +
                                "Number of affected rows: ${rows}"
                    )
                }

            } catch (e: SQLException) {
                e.printStackTrace()
                println("Failed to execute query due to some SQL method error | Number of affected rows: ${rows}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            rows
        } else {
            rows
        }
    }

    // TODO("update method")

    suspend fun getProjectContract(project: String): ProjectEmployeeContract? {
        var _contract: ProjectEmployeeContract? = null
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement = db.prepareStatement("SELECT * FROM Employee_Project WHERE STR_project ='${project}';")
                val res: ResultSet = query.executeQuery()
                res.next()

                if (res.row == 1) {
                    val data: ProjectEmployeeContract = ProjectEmployeeContract(
                        res.getInt("id_contract"),
                        getProject(res.getString("STR_project"))!!,
                        getEmployee(res.getString("STR_employee"))!!
                    )
                    _contract = data
                    println("Query Ok! Object returned: ${_contract}")
                } else {
                    throw SQLException("Register doesn't exist.")
                }
            } catch (e: SQLException) {
                println("Failed to return data due to: ${e.message}")
            }
        }

        job.join()
        return if (job.isCompleted) {
            _contract
        } else {
            null
        }
    }

    suspend fun getAllProjectContracts(): ArrayList<ProjectEmployeeContract> {
        lateinit var contract: ProjectEmployeeContract
        val list: ArrayList<ProjectEmployeeContract> = ArrayList<ProjectEmployeeContract>()
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {

                val query: PreparedStatement =
                    db.prepareStatement("SELECT * FROM Employee_Project;")
                val res: ResultSet = query.executeQuery()

                while (res.next()) {
                    contract = ProjectEmployeeContract(
                        res.getInt("id_contract"),
                        getProject(res.getString("STR_project"))!!,
                        getEmployee(res.getString("STR_employee"))!!
                    )

                    list.add(contract)
                }

                println("Query OK! Number of retrieved rows: ${list.size}")
            } catch (e: SQLException) {
                println("Failed to retrieve data due to: ${e.message}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            list
        } else {
            list
        }
    }

    suspend fun deleteProjectContract(contract: ProjectEmployeeContract): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val data: ProjectEmployeeContract? = getProjectContract(contract.project.name)
                val query: PreparedStatement =
                    db.prepareStatement("DELETE FROM Employee_Project WHERE id_contract='${data?.id}';")
                query.execute()
                rows++
                println("Query Ok! Number of affected rows: ${rows}")
            } catch (e: SQLException) {
                e.printStackTrace()
                println("Failed to delete data due to: ${e.message}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            rows
        } else {
            rows
        }
    }

    // OVERSEER

    suspend fun createOverseer(overseer: Overseer): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val employee: Employee? = getEmployee(overseer.empName)

                val query: PreparedStatement =
                    db.prepareStatement("INSERT INTO Overseer (STR_EMP_name, F_wage) VALUES ('${employee?.name}', '${overseer.wage}');")
                query.execute()
                rows++
                println("Query OK! Number of affected rows: ${rows}")
            } catch (e: SQLException) {
                e.printStackTrace()
                println("Failed to execute query due to: ${e.message}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            rows
        } else {
            rows
        }
    }

    suspend fun updateOverseer(overseer: Overseer, data: Overseer) : Int{
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try{
                val oldData: Overseer? = getOverseer(overseer.empName)
                if(oldData != null && oldData.empName == data.empName){
                    if(oldData.wage != data.wage) {
                        val query: PreparedStatement =
                            db.prepareStatement("UPDATE Overseer SET F_wage='${data.wage}' WHERE F_wage='${oldData.wage}';")
                        query.execute()
                    }

                    if(oldData.timeWorked != data.timeWorked){
                        val query: PreparedStatement =
                            db.prepareStatement("UPDATE Overseer SET time_worked_journey='${data.timeWorked}' WHERE time_worked_journey='${oldData.timeWorked}'")
                        query.execute()
                    }
                    rows++
                    println("Query Ok! Number of affected rows: ${rows}")
                }else{
                    throw SQLException("Data doesn't match. ${overseer.empName} and ${data.empName} is not the same.")
                }
            }catch (e: SQLException) {
                println("Failed to update data due to: ${e.message}.")
                e.printStackTrace()
            }
        }

        job.join()

        return if (job.isCompleted){
            rows
        }else{
            rows
        }
    }

    suspend fun getOverseer(name: String): Overseer? {
        lateinit var _overseer: Overseer
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("SELECT * FROM Overseer WHERE STR_EMP_name='${name}';")
                val res: ResultSet = query.executeQuery()
                res.next()

                _overseer = Overseer(
                    res.getInt("I_id"),
                    res.getString("STR_EMP_name"),
                    res.getDouble("F_wage"),
                    res.getTimestamp("time_worked_journey")?.toString() ?: "0000-00-00 00:00:00"
                )
                println("Query OK! Object returned: ${_overseer}")
            } catch (e: SQLException) {
                e.printStackTrace()
                println("Failed to return data due to: ${e.message}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            _overseer
        } else {
            null
        }
    }

    suspend fun getAllOverseer(): ArrayList<Overseer> {
        lateinit var overseer: Overseer
        val list: ArrayList<Overseer> = ArrayList<Overseer>()
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement = db.prepareStatement("SELECT * FROM Overseer;")
                val res: ResultSet = query.executeQuery()

                while (res.next()) {
                    overseer = Overseer(
                        res.getInt("I_id"),
                        res.getString("STR_EMP_name"),
                        res.getDouble("F_wage"),
                        res.getTimestamp("time_worked_journey")?.toString()
                    )
                    list.add(overseer)
                }

                println("Query OK! Number of retrieved rows: ${list.size}")
            } catch (e: SQLException) {
                println("Failed to retrieve data due to: ${e.message}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            list
        } else {
            list
        }
    }

    suspend fun deleteOverseer(overseer: Overseer): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("DELETE FROM Overseer WHERE STR_EMP_name='${overseer.empName}';")
                query.execute()
                rows++
                println("Query OK! Number of affected rows: ${rows}")
            } catch (e: SQLException) {
                e.printStackTrace()
                println("Failed to delete data due to: ${e.message}")
            }
        }

        job.join()

        return if (job.isCompleted) {
            rows
        } else {
            rows
        }
    }
}