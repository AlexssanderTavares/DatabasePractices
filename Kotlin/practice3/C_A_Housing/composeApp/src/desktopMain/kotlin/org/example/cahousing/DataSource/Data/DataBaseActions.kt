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
import org.example.cahousing.DataSource.Models.Models
import org.example.cahousing.DataSource.Models.Overseer
import org.example.cahousing.DataSource.Models.Project
import org.example.cahousing.DataSource.Models.ProjectEmployeeContract
import org.example.cahousing.DataSource.Utilities.PostalCodeFormatter
import org.example.cahousing.getPlatform
import org.jetbrains.skia.TextBlob
import java.sql.Connection
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.InvalidPropertiesFormatException
import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction
import kotlin.random.Random
import kotlin.random.nextInt


class DataBaseActions {

    suspend fun create(model: Models): Int {
        val db: Connection = DataBaseConnection.CONNECTION
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            when (model) {
                //Dept
                is Dept -> {
                    try {
                        val query1: PreparedStatement =
                            db.prepareStatement("SELECT * FROM Dept WHERE STR_name = '${model.name}';")
                        val res: ResultSet = query1.executeQuery()
                        res.next()

                        if (res.row == 1) {
                            throw SQLException("Every Department name has an unique name. Can't create another department with that name.")
                        }

                        val query2: PreparedStatement =
                            db.prepareStatement("INSERT INTO Dept (STR_name, STR_description) VALUES ('${model.name}','${model.description}');")
                        query2.execute()
                        rows++
                        println("Query ok! Number of affected rows: ${rows}")
                    } catch (e: SQLException) {
                        println("Failed to create create ${model.javaClass} data due to ${e.message}. Rows affected: ${rows}")
                        e.printStackTrace()
                    }
                }

                //Address

                is Address -> {
                    val formatter: PostalCodeFormatter = PostalCodeFormatter()

                    if (formatter.isValid(model.cep)) {
                        try {
                            val query1: PreparedStatement = db.prepareStatement(
                                "SELECT * FROM Address WHERE I_cep = '${
                                    formatter.toCepFormat(model.cep)
                                }';"
                            )
                            val res: ResultSet = query1.executeQuery()
                            res.next()

                            if (res.row == 1) {
                                throw SQLException("Address with such CEP is already stored.")
                            }

                            val query2: PreparedStatement = db.prepareStatement(
                                "INSERT INTO Address (I_cep, STR_road, STR_district, STR_city) VALUES ('${
                                    formatter.toCepFormat(model.cep)
                                }','${model.road}','${model.district}','${model.city}');"
                            )
                            query2.execute()
                            rows++
                            println("Query ok! Rows affected on table Address: ${rows}")
                        } catch (e: SQLException) {
                            println("Failed to create create ${model.javaClass} data due to ${e.message}. Rows affected: ${rows}")
                            e.printStackTrace()
                        }
                    }
                }

                //Employee
                is Employee -> {
                    val formatter: PostalCodeFormatter = PostalCodeFormatter()

                    try {
                        val query1: PreparedStatement =
                            db.prepareStatement("SELECT * FROM Employee WHERE STR_name = '${model.name}';")
                        val res: ResultSet = query1.executeQuery()
                        res.next()

                        if (res.row == 1) {
                            throw SQLException("Every Employee name is unique and can't be repeated.")
                        }

                        val query2: PreparedStatement = db.prepareStatement(
                            "INSERT INTO Employee (STR_name, I_ID, F_wage, STR_sex, dt_born_date, I_ADDRESS_cep, I_DEPT_num) VALUES ('${model.name}', '${
                                Random.nextInt(Math.round(1111F)..Math.round(9999F))
                            }', '${model.wage}', '${model.sex}', '${model.bornDate}', '${
                                formatter.toCepFormat(
                                    model.address
                                )
                            }', '${model.idDept}');"
                        )
                        query2.execute()
                        rows++
                        println("Query ok! Rows affected on table Employee: ${rows}")
                    } catch (e: SQLException) {
                        println("Failed to create create ${model.javaClass} data due to ${e.message}. Rows affected: ${rows}")
                        e.printStackTrace()
                    }
                }


                //Project
                is Project -> {
                    try {
                        val query1: PreparedStatement =
                            db.prepareStatement("SELECT * FROM Project WHERE STR_name = '${model.name}';")
                        val res: ResultSet = query1.executeQuery()
                        res.next()

                        if (res.row == 1) {
                            throw SQLException("Every Project name is unique and can't be repeated.")
                        }

                        val query2: PreparedStatement =
                            db.prepareStatement("INSERT INTO Project (STR_NAME, I_num_dept) VALUES ('${model.name}', '${model.dept}');")
                        query2.execute()
                        rows++
                        println("Query ok! Rows affected on table Project: ${rows}")
                    } catch (e: SQLException) {
                        println("Failed to create create ${model.javaClass} data due to ${e.message}. Rows affected: ${rows}")
                        e.printStackTrace()

                    }
                }


                //Contract
                is ProjectEmployeeContract -> {
                    try {
                        val query1: PreparedStatement =
                            db.prepareStatement("SELECT * FROM Project WHERE STR_name = '${model.project.name}';")
                        val projectRes: ResultSet = query1.executeQuery()

                        val query2: PreparedStatement =
                            db.prepareStatement("SELECT * FROM Employee WHERE STR_name='${model.employee.name}';")
                        val employeeRes: ResultSet = query2.executeQuery()

                        projectRes.next()
                        employeeRes.next()

                        if (projectRes.row == 1 && employeeRes.row == 1) {
                            val query3: PreparedStatement =
                                db.prepareStatement("INSERT INTO Employee_Project (STR_project, STR_employee, STR_description) VALUES ('${model.project.name}', '${model.employee.name}', '${model.description}');")
                            query3.execute()
                            rows++
                            println("Query OK! Number of affected rows: ${rows}")
                        }
                    } catch (e: SQLException) {
                        println("Failed to create ${model.javaClass} data due to: ${e.message}")
                        e.printStackTrace()
                    }
                }

                is Overseer -> {
                    try {
                        val query1: PreparedStatement =
                            db.prepareStatement("SELECT * FROM Employee WHERE STR_name = '${model.empName}';")
                        val employeeRes: ResultSet = query1.executeQuery()
                        employeeRes.next()

                        if (employeeRes.row == 1) {
                            val query2: PreparedStatement =
                                db.prepareStatement("INSERT INTO Overseer (STR_EMP_name, F_wage) VALUES ('${model.empName}', '${model.wage}');")
                            query2.execute()
                            rows++
                            println("Query OK! Number of affected rows: ${rows}")
                        }
                    } catch (e: SQLException) {
                        println("Failed to create ${model.javaClass} data due to: ${e.message}")
                        e.printStackTrace()
                    }
                }
            }
        }
        job.join()

        return if (job.isCompleted) {
            rows
        } else {
            throw RuntimeException("Process finished before IO thread routine.")
        }
    }

    suspend inline fun <reified T : Models> get(varchar: String): Models? {
        val db: Connection = DataBaseConnection.CONNECTION
        var model: Models? = null
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                when (T::class) {
                    Dept::class -> {
                        val query: PreparedStatement =
                            db.prepareStatement("SELECT * FROM Dept WHERE STR_name = '${varchar}';")
                        val res: ResultSet = query.executeQuery()
                        res.next()

                        if (res.row == 1) {
                            model = Dept(
                                id = res.getInt("I_num_dept"),
                                name = res.getString("STR_name"),
                                description = res.getString("STR_description")
                            )
                        }
                        println("Query ok! Object returned: ${model}")
                    }

                    Address::class -> {
                        val formatter: PostalCodeFormatter = PostalCodeFormatter()

                        if (formatter.isValid(varchar)) {
                            val query: PreparedStatement = db.prepareStatement(
                                "SELECT * FROM Address WHERE I_cep = '${
                                    formatter.toCepFormat(varchar)
                                }';"
                            )
                            val res: ResultSet = query.executeQuery()
                            res.next()

                            if (res.row == 1) {
                                model = Address(
                                    cep = res.getString("I_cep"),
                                    road = res.getString("STR_road"),
                                    district = res.getString("STR_district"),
                                    city = res.getString("STR_city")
                                )
                            }
                            println("Query ok! Object returned: ${model}")
                        } else {
                            throw IllegalArgumentException("Invalid CEP format.")
                        }
                    }

                    Employee::class -> {
                        val formatter: PostalCodeFormatter = PostalCodeFormatter()
                        val query: PreparedStatement =
                            db.prepareStatement("SELECT * FROM Employee WHERE STR_name = '${varchar}';")
                        val res: ResultSet = query.executeQuery()
                        res.next()

                        if (res.row == 1) {

                            model = Employee(
                                id = res.getInt("I_ID"),
                                name = res.getString("STR_name"),
                                sex = res.getString("STR_sex"),
                                wage = res.getDouble("F_wage"),
                                bornDate = res.getDate("dt_born_date")?.toString(),
                                timeWorked = res.getTimestamp("time_worked_journey")?.toString(),
                                address = res.getString("I_ADDRESS_cep"),
                                idDept = res.getInt("I_DEPT_num")
                            )
                            println("Query ok! Object returned: ${model}")
                        } else {
                            throw SQLException("Register doesn't exit!")
                        }
                    }

                    Project::class -> {
                        val query: PreparedStatement = db.prepareStatement("SELECT * FROM Project WHERE STR_name = '${varchar}';")
                        val res: ResultSet = query.executeQuery()
                        res.next()

                        if (res.row == 1) {
                            model = Project(
                                res.getString("STR_name"),
                                res.getInt("I_num_dept")
                            )
                            println("Query ok! Object returned: ${model}")
                        } else {
                            throw SQLException("Register doesn't exist")
                        }
                    }

                    Overseer::class -> {
                        val query: PreparedStatement =
                            db.prepareStatement("SELECT * FROM Overseer WHERE STR_EMP_name='${varchar}';")
                        val res: ResultSet = query.executeQuery()
                        res.next()

                        if (res.row == 1) {
                            model = Overseer(
                                res.getInt("I_id"),
                                res.getString("STR_EMP_name"),
                                res.getDouble("F_wage"),
                                res.getTimestamp("time_worked_journey")?.toString()
                            )
                        } else {
                            throw IllegalArgumentException("Register doesn't exist.")
                        }
                        println("Query OK! Object returned: ${model}")

                    }

                    ProjectEmployeeContract::class -> {
                        lateinit var project: Project
                        lateinit var employee: Employee

                        val query: PreparedStatement =
                            db.prepareStatement("SELECT * FROM Employee_Project WHERE STR_project = '${varchar}';")
                        val res: ResultSet = query.executeQuery()
                        res.next()

                        if (res.row == 1) {
                            val queryForProject: PreparedStatement = db.prepareStatement(
                                "SELECT * FROM Project WHERE STR_name = '${
                                    res.getString("STR_project")
                                }';"
                            )
                            val projectRes: ResultSet = queryForProject.executeQuery()
                            projectRes.next()

                            if (projectRes.row == 1) {
                                project = Project(
                                    projectRes.getString("STR_name"),
                                    projectRes.getInt("I_num_dept"),
                                )
                            } else {
                                throw IllegalArgumentException("This project doesn't exist.")
                            }

                            val queryForEmployee: PreparedStatement = db.prepareStatement(
                                "SELECT * FROM Employee WHERE STR_name = '${
                                    res.getString("STR_employee")
                                }';"
                            )
                            val employeeRes: ResultSet = queryForEmployee.executeQuery()
                            employeeRes.next()

                            if (employeeRes.row == 1) {
                                employee = Employee(
                                    id = employeeRes.getInt("I_ID"),
                                    name = employeeRes.getString("STR_name"),
                                    sex = employeeRes.getString("STR_sex"),
                                    wage = employeeRes.getDouble("F_wage"),
                                    bornDate = employeeRes.getDate("dt_born_date").toString() ?: "1970-01-01",
                                    timeWorked = employeeRes.getTimestamp("time_worked_journey").toString() ?: "1970-01-01 00:00:00",
                                    address = employeeRes.getString("I_ADDRESS_cep"),
                                    idDept = employeeRes.getInt("I_DEPT_num")
                                )
                            } else {
                                throw IllegalArgumentException("This employee doesn't exist.")
                            }
                            model = ProjectEmployeeContract(id = res.getInt("id_contract"), project = project, employee = employee, description = res.getString("STR_description"))
                        } else {
                            throw IllegalArgumentException("Register doesn't exist.")
                        }
                    }
                }
            } catch (e: SQLException) {
                println("Failed to get ${T::class} data due to: ${e.message}")
                e.printStackTrace()
            }
        }

        job.join()

        return if (job.isCompleted) {
            model
        } else {
            throw RuntimeException("Process finished before IO thread routine.")
        }
    }

    suspend inline fun <reified T : Models> getAll(): ArrayList<Models> {
        val db: Connection = DataBaseConnection.CONNECTION
        var rows: Int = 0
        val list: ArrayList<Models> = arrayListOf()
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                when (T::class) {
                    Dept::class -> {
                        val query: PreparedStatement = db.prepareStatement("SELECT * FROM Dept;")
                        val res: ResultSet = query.executeQuery()

                        while (res.next()) {
                            val dept: Dept = Dept(
                                id = res.getInt("I_num_dept"),
                                name = res.getString("STR_name"),
                                description = res.getString("STR_description")
                            )
                            list.add(dept)
                            rows++
                            }
                    }

                    Address::class -> {
                        val query: PreparedStatement = db.prepareStatement("SELECT * FROM Address;")
                        val res: ResultSet = query.executeQuery()

                        while (res.next()) {
                            val address: Address = Address(
                                cep = res.getString("I_cep"),
                                road = res.getString("STR_road"),
                                district = res.getString("STR_district"),
                                city = res.getString("STR_city")
                            )
                            list.add(address)
                            rows++
                        }
                    }

                    Employee::class -> {
                        val query: PreparedStatement = db.prepareStatement("SELECT * FROM Employee;")
                        val res: ResultSet = query.executeQuery()

                        while (res.next()) {
                            val emp: Employee = Employee(
                                id = res.getInt("I_ID"),
                                name = res.getString("STR_name"),
                                wage = res.getDouble("F_wage"),
                                sex = res.getString("STR_sex"),
                                bornDate = res.getDate("dt_born_date")?.toString() ?: "1970-01-01",
                                address = res.getString("I_ADDRESS_cep"),
                                idDept = res.getInt("I_DEPT_num"),
                                timeWorked = res.getTimestamp("time_worked_journey")?.toString() ?: "1970-01-01 00:00:00"
                            )
                            list.add(emp)
                            rows++
                        }
                    }

                    Project::class -> {
                        val query: PreparedStatement = db.prepareStatement("SELECT * FROM Project;")
                        val res: ResultSet = query.executeQuery()

                        while(res.next()) {
                            val project: Project = Project(
                                name = res.getString("STR_name"),
                                dept = res.getInt("I_num_dept")
                            )

                            list.add(project)
                            rows++
                        }
                    }

                    ProjectEmployeeContract::class -> {
                        val query: PreparedStatement = db.prepareStatement("SELECT * FROM Employee_Project;")
                        val res: ResultSet = query.executeQuery()

                            while(res.next()){
                                val project: Project = get<Project>(res.getString("STR_project"))!! as Project
                                val emp: Employee = get<Employee>(res.getString("STR_employee"))!! as Employee

                                val contract: ProjectEmployeeContract = ProjectEmployeeContract(
                                    id = res.getInt("id_contract"),
                                    project = project,
                                    employee = emp
                                )
                                list.add(contract)
                                rows++
                            }
                    }

                    Overseer::class -> {
                        val query: PreparedStatement = db.prepareStatement("SELECT * FROM Overseer;")
                        val res: ResultSet = query.executeQuery()

                        while (res.next()) {
                            val emp: Employee = get<Employee>(res.getString("STR_EMP_name"))!! as Employee
                            val overseer: Overseer = Overseer(
                                id = res.getInt("I_id"),
                                empName = emp.name,
                                wage = res.getDouble("F_wage"),
                                timeWorked = res.getTimestamp("time_worked_journey")?.toString() ?: "1970-01-01"
                            )
                            list.add(overseer)
                            rows++
                        }
                    }
                }
                println("Query OK! Number of retrieved rows: ${rows}")
            } catch (e: SQLException) {
                println("Failed to retrieve list of data due to: ${e.message}")
                e.printStackTrace()
            }
        }

        job.join()
        return if (job.isCompleted) {
            list
        } else {
            throw RuntimeException("Process finished before IO thread routine.")
        }
    }

    /*
        // DEPT
    //TODO("Delete this")
        suspend fun createDept(dept: Dept): Int {
            var rows: Int = 0
            val job: Job = CoroutineScope(Dispatchers.IO).launch {

                getAllDept().forEach {
                    if (it.name == dept.name) {
                        throw SQLException("Every Department name has an unique name. Can't create another department with that name.")
                    }
                }

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
            var data: Dept? = null
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
                    if (oldData != null) {
                        if (oldData.description != data.description) {
                            val query: PreparedStatement =
                                db.prepareStatement("UPDATE Dept SET STR_description='${data.description}' WHERE STR_name='${oldData.name}';")
                            query.execute()
                            rows++
                        }
                        if (oldData.name != data.name) {
                            val query: PreparedStatement =
                                db.prepareStatement("UPDATE Dept SET STR_name='${data.name}' WHERE STR_name='${oldData.name}';")
                            query.execute()
                            rows++
                        }
                        println("Query Ok! Number of Affected rows: ${rows}")
                    } else {
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
    //TODO("Delete this")
        suspend fun createAddress(address: Address): Int {
            var rows: Int = 0
            if (formatter.isValid(address.cep)) {

                val job: Job = CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val query: PreparedStatement =
                            db.prepareStatement(
                                "INSERT INTO Address (I_cep, STR_road, STR_district, STR_city) VALUES ('${
                                    formatter.toCepFormat(
                                        address.cep
                                    )
                                }','${address.road}','${address.district}','${address.city}');"
                            )
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
                        if (data != null && data.cep == address.cep) {

                            if (data.road != newData.road) {
                                val query: PreparedStatement =
                                    db.prepareStatement("UPDATE Address SET STR_road='${newData.road}' WHERE I_cep='${data.cep}';")
                                query.execute()
                            }

                            if (data.city != newData.city) {
                                val query: PreparedStatement =
                                    db.prepareStatement("UPDATE Address SET STR_city='${newData.city}' WHERE I_cep='${data.cep}';")
                                query.execute()
                            }

                            if (data.district != newData.district) {
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

                    } catch (e: IllegalArgumentException) {
                        println("Failed to update data due to: ${e.message}. Rows affected: ${rows}")
                        e.printStackTrace()
                    }
                } else {
                    throw IllegalArgumentException("${address} doesn't exist.")
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
            if (!formatter.isValid(cep)) {
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

        suspend fun deleteAddress(address: Address): Int {
            var rows: Int = 0
            val job: Job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val data: Address =
                        getAddress(address.cep) ?: throw NullPointerException("There is no such data.")
                    val query: PreparedStatement =
                        db.prepareStatement("DELETE FROM Address WHERE I_cep='${address.cep}';")
                    query.execute()
                    rows++
                    println("Query Ok! Number of affected rows: ${rows}.")
                } catch (e: SQLException) {
                    println("Failed to delete data due to: ${e.message}. Rows affected: ${rows}")
                    e.printStackTrace()
                }
            }

            job.join()
            return if (job.isCompleted) {
                rows
            } else {
                rows
            }
        }

        // EMPLOYEE
    //TODO("Delete this")
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

        suspend fun updateEmployee(employee: Employee, newData: Employee): Int {
            var rows: Int = 0
            val job: Job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val data: Employee? = getEmployee(employee.name)

                    if (data != null && data.name == newData.name) {

                        if (data.wage != newData.wage) {
                            val query: PreparedStatement =
                                db.prepareStatement("UPDATE Employee SET F_wage='${newData.wage}' WHERE STR_name='${data.name}';")
                            query.execute()
                        }

                        if (data.timeWorked != newData.timeWorked) {
                            val query: PreparedStatement =
                                db.prepareStatement("UPDATE Employee SET time_worked_journey='${newData.timeWorked}' WHERE STR_name='${data.name}';")
                            query.execute()
                        }
                        rows++
                        println("Query OK! Number of affected rows: ${rows}")
                    } else {
                        throw IllegalArgumentException("${employee} doesn't exist.")
                    }
                } catch (e: SQLException) {
                    println("Failed to update data due to: ${e.message}. Rows affected: ${rows}")
                    e.printStackTrace()
                }
            }
            job.join()
            return if (job.isCompleted) {
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
                            res.getTimestamp("time_worked_journey")?.toString()
                                ?: "0000-00-00 00:00:00",
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
                            val contract = ProjectEmployeeContract(
                                res1.getInt("id_contract"),
                                getProject(res1.getString("STR_project"))!!,
                                getEmployee(employee.name)!!
                            )
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
                            val overseer = Overseer(
                                res2.getInt("I_id"),
                                res2.getString("STR_EMP_name"),
                                res2.getDouble("F_wage"),
                                res2.getTimestamp("time_worked_journey")?.toString()
                                    ?: "0000-00-00 00:00:00"
                            )
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
    //TODO("Delete this")
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

        suspend fun updateProject(project: Project, newData: Project): Int {
            var rows: Int = 0
            val job: Job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val data: Project? = getProject(project.name)
                    if (data != null) {
                        if (data.name != newData.name) {
                            val query: PreparedStatement =
                                db.prepareStatement("UPDATE Project SET STR_name='${newData.name}' WHERE STR_name='${data.name}';")
                            query.execute()
                        }

                        if (data.dept != newData.dept) {
                            val query: PreparedStatement =
                                db.prepareStatement("UPDATE Project SET I_num_dept='${newData.dept}' WHERE STR_name='${data.name}';")
                            query.execute()
                        }

                        rows++
                        println("Query OK! Number of affected rows: ${rows}")
                    } else {
                        throw IllegalArgumentException("${project} doesn't exist.")
                    }

                } catch (e: SQLException) {
                    println("Failed to update data due to: ${e.message}. Rows affected: ${rows}")
                    e.printStackTrace()
                }
            }
            job.join()
            return if (job.isCompleted) {
                rows
            } else {
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

                        val subQuery2: PreparedStatement? = try {
                            db.prepareStatement(
                                "SELECT * FROM Employee_Project WHERE STR_project='${
                                    res1.getString(
                                        getProject(project.name)?.name
                                    )
                                }';"
                            )
                        } catch (e: SQLException) {
                            println("Data not found: ${e.message}")
                            null
                        }

                        val res2: ResultSet? = subQuery2?.executeQuery()
                        res2?.next()

                        if (res2?.row == 0 || res2 == null) {
                            println("There is no data bonded to this ${project.name}")
                            0
                        } else {
                            val contract: ProjectEmployeeContract = ProjectEmployeeContract(
                                res2.getInt("id_contract"),
                                getProject(res2.getString(project.name))!!,
                                getEmployee(res2.getString("STR_employee"))!!
                            )
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
    //TODO("Delete this")
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

        suspend fun updateProjectContract(
            contract: ProjectEmployeeContract,
            newData: ProjectEmployeeContract
        ): Int {
            var rows: Int = 0
            val job: Job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val data: ProjectEmployeeContract? = getProjectContract(contract.project.name)
                    if (data != null && data.description != newData.description) {
                        val query: PreparedStatement =
                            db.prepareStatement("UPDATE Employee_Project SET STR_description='${newData.description}' WHERE STR_project='${data.project.name}';")
                        query.execute()
                    }
                    rows++
                    println("Query OK! Number of affected rows: ${rows}")
                } catch (e: SQLException) {
                    println("Failed to update data due to: ${e.message}")
                    e.printStackTrace()
                }
            }

            job.join()
            return if (job.isCompleted) {
                rows
            } else {
                rows
            }
        }

        suspend fun getProjectContract(project: String): ProjectEmployeeContract? {
            var _contract: ProjectEmployeeContract? = null
            val job: Job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val query: PreparedStatement =
                        db.prepareStatement("SELECT * FROM Employee_Project WHERE STR_project ='${project}';")
                    val res: ResultSet = query.executeQuery()
                    res.next()

                    if (res.row == 1) {
                        val data: ProjectEmployeeContract = ProjectEmployeeContract(
                            res.getInt("id_contract"),
                            getProject(res.getString("STR_project"))!!,
                            getEmployee(res.getString("STR_employee"))!!,
                            res.getString("STR_description")
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
    //TODO("Delete this")
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

        suspend fun updateOverseer(overseer: Overseer, data: Overseer): Int {
            var rows: Int = 0
            val job: Job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val oldData: Overseer? = getOverseer(overseer.empName)
                    if (oldData != null && oldData.empName == data.empName) {
                        if (oldData.wage != data.wage) {
                            val query: PreparedStatement =
                                db.prepareStatement("UPDATE Overseer SET F_wage='${data.wage}' WHERE F_wage='${oldData.wage}';")
                            query.execute()
                        }

                        if (oldData.timeWorked != data.timeWorked) {
                            val query: PreparedStatement =
                                db.prepareStatement("UPDATE Overseer SET time_worked_journey='${data.timeWorked}' WHERE time_worked_journey='${oldData.timeWorked}'")
                            query.execute()
                        }
                        rows++
                        println("Query Ok! Number of affected rows: ${rows}")
                    } else {
                        throw SQLException("Data doesn't match. ${overseer.empName} and ${data.empName} is not the same.")
                    }
                } catch (e: SQLException) {
                    println("Failed to update data due to: ${e.message}.")
                    e.printStackTrace()
                }
            }

            job.join()

            return if (job.isCompleted) {
                rows
            } else {
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
        }*/
}