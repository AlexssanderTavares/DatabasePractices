package org.example.cahousing.DataSource.Data.Accessors

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.example.cahousing.DataBaseConnection
import org.example.cahousing.DataSource.Data.Accessors.DeptAccessor.Companion
import org.example.cahousing.DataSource.Models.Address
import org.example.cahousing.DataSource.Models.Employee
import org.example.cahousing.DataSource.Models.Overseer
import org.example.cahousing.DataSource.Models.ProjectEmployeeContract
import org.example.cahousing.DataSource.Utilities.Cep
import org.example.cahousing.DataSource.Utilities.Formatter
import org.example.cahousing.Factories.DataBaseAccessorFactory
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import kotlin.random.Random
import kotlin.random.nextInt

class EmployeeAccessor : EmployeeAccessorImp {

    companion object {
        private var db: Connection = DataBaseConnection.CONNECTION
        private val formatter: Formatter = Cep()
        private var projectAccessor: ProjectAccessorImp = ProjectAccessor()
        private var contractAccessor: ProjectEmployeeContractAccessorImp = ProjectEmployeeContractAccessor()
        private var overseerAccessor: OverseerAccessorImp = OverseerAccessor()
    }

    override suspend fun create(model: Employee): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement = db.prepareStatement(
                    "INSERT INTO Employee (STR_name, I_ID, F_wage, STR_sex, dt_born_date, I_ADDRESS_cep, I_DEPT_num) VALUES ('${model.name}', '${
                        Random.nextInt(Math.round(1111F)..Math.round(9999F))
                    }', '${model.wage}', '${model.sex}', '${model.bornDate}', '${
                        formatter.format(
                            model.address
                        )
                    }', '${model.idDept}');"
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

    override suspend fun update(model: Employee, data: Employee): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val emp: Employee? = get(model.name)

                if (emp != null && emp.name == data.name) {

                    if (emp.wage != data.wage) {
                        val query: PreparedStatement =
                            db.prepareStatement("UPDATE Employee SET F_wage='${data.wage}' WHERE STR_name='${data.name}';")
                        query.execute()
                    }

                    if (emp.timeWorked != data.timeWorked) {
                        val query: PreparedStatement =
                            db.prepareStatement("UPDATE Employee SET time_worked_journey='${data.timeWorked}' WHERE STR_name='${data.name}';")
                        query.execute()
                    }
                    rows++
                    println("Query OK! Number of affected rows: ${rows}")
                } else {
                    throw IllegalArgumentException("${model} doesn't exist.")
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

    override suspend fun get(varchar: String): Employee? {
        var employee: Employee? = null
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("SELECT * FROM Employee WHERE STR_name = '${varchar}';")
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
                            ?: "2000-01-01 00:00:00",
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

    override suspend fun getAll(): ArrayList<Employee> {
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
                        res.getDate("dt_born_date")?.toString() ?: "2000-01-01",
                        res.getTimestamp("time_worked_journey")?.toString() ?: "2000-01-01 00:00:00",
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

    override suspend fun delete(model: Employee): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("DELETE FROM Employee WHERE STR_name='${model.name}';")
                val task1: Deferred<Int> = async {
                    val subQuery1: PreparedStatement =
                        db.prepareStatement("SELECT * FROM Employee_Project WHERE STR_employee='${model.name}';")
                    val res1: ResultSet = subQuery1.executeQuery()
                    res1.next()
                    if (res1.row == 0) {
                        0
                    } else {
                        val contract = ProjectEmployeeContract(
                            id = res1.getInt("id_contract"),
                            project = projectAccessor.get(res1.getString("STR_project"))!!,
                            employee = get(res1.getString("STR_employee"))!!
                        )
                        contractAccessor.delete(contract)
                    }
                }
                val task2: Deferred<Int> = async {
                    val subQuery2: PreparedStatement =
                        db.prepareStatement("SELECT * FROM Overseer WHERE STR_EMP_name='${model.name}';")
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
                                ?: "2000-01-01 00:00:00"
                        )
                        overseerAccessor.delete(overseer)
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

    override fun turnTestOn(){
        db = DataBaseConnection.TEST_CONNECTION
    }

    override fun turnTestOff(){
        db = DataBaseConnection.CONNECTION
    }
}