package org.example.cahousing.DataSource.Data.Accessors

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.example.cahousing.DataBaseConnection
import org.example.cahousing.DataSource.Models.Address
import org.example.cahousing.DataSource.Models.Project
import org.example.cahousing.DataSource.Models.ProjectEmployeeContract
import org.example.cahousing.Factories.DataBaseAccessorFactory
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class ProjectAccessor : ProjectAccessorImp {

    companion object {
        private var db: Connection = DataBaseConnection.CONNECTION
        private lateinit var employeeAccessor: EmployeeAccessorImp
        private lateinit var contractAccessor: ProjectEmployeeContractAccessorImp
    }

    override suspend fun create(model: Project): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("INSERT INTO Project (STR_NAME, I_num_dept) VALUES ('${model.name}', '${model.dept}');")
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

    override suspend fun update(model: Project, data: Project): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val project: Project? = get(model.name)
                if (project != null) {
                    if (project.name != data.name) {
                        val query: PreparedStatement =
                            db.prepareStatement("UPDATE Project SET STR_name='${data.name}' WHERE STR_name='${project.name}';")
                        query.execute()
                    }

                    if (project.dept != data.dept) {
                        val query: PreparedStatement =
                            db.prepareStatement("UPDATE Project SET I_num_dept='${data.dept}' WHERE STR_name='${project.name}';")
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

    override suspend fun get(varchar: String): Project? {
        lateinit var project: Project
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("SELECT * FROM Project WHERE STR_NAME = '${varchar}';")
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

    override suspend fun getAll(): ArrayList<Project> {
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

    override suspend fun delete(model: Project): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("DELETE FROM Project WHERE STR_name='${model.name}';")

                val task: Deferred<Int> = async {
                    val subQuery1: PreparedStatement =
                        db.prepareStatement("SELECT * FROM Project WHERE STR_name='${model.name}';")
                    val res1: ResultSet = subQuery1.executeQuery()
                    res1.next()

                    val subQuery2: PreparedStatement? = try {
                        db.prepareStatement(
                            "SELECT * FROM Employee_Project WHERE STR_project='${
                                res1.getString(
                                    get(model.name)?.name
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
                        println("There is no data bonded to this ${model.name}")
                        0
                    } else {
                        val contract: ProjectEmployeeContract = ProjectEmployeeContract(
                            res2.getInt("id_contract"),
                            get(res2.getString(model.name))!!,
                            employeeAccessor.get(res2.getString("STR_employee"))!!
                        )
                        contractAccessor.delete(contract)
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

    override fun turnTestOn() {
        db = DataBaseConnection.TEST_CONNECTION
    }

    override fun turnTestOff() {
        db = DataBaseConnection.CONNECTION
    }

}