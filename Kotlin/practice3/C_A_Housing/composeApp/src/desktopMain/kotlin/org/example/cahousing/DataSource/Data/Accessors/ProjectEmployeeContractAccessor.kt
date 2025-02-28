package org.example.cahousing.DataSource.Data.Accessors

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.example.cahousing.DataBaseConnection
import org.example.cahousing.DataSource.Models.Employee
import org.example.cahousing.DataSource.Models.Project
import org.example.cahousing.DataSource.Models.ProjectEmployeeContract
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class ProjectEmployeeContractAccessor: ProjectEmployeeContractAccessorImp {

    companion object {
        private var db: Connection = DataBaseConnection.CONNECTION
        private val employeeAccessor: EmployeeAccessorImp = EmployeeAccessor()
        private val projectAccessor: ProjectAccessorImp = ProjectAccessor()
    }

    override suspend fun create(model: ProjectEmployeeContract): Int {
            var rows: Int = 0
            val job: Job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val projectData: Project? = projectAccessor.get(model.project.name)
                    val employeeData: Employee? = employeeAccessor.get(model.employee.name)

                    if (projectData != null && employeeData != null) {
                        val query: PreparedStatement =
                            db.prepareStatement("INSERT INTO Employee_Project (STR_project, STR_employee, STR_description) VALUES ('${projectData.name}', '${employeeData.name}', '${model.description}');")
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

    override suspend fun update(model: ProjectEmployeeContract, data: ProjectEmployeeContract) : Int {
            var rows: Int = 0
            val job: Job = CoroutineScope(Dispatchers.IO).launch {
                try{
                    val contract: ProjectEmployeeContract? = get(model.project.name)
                    if(contract != null && contract.description != data.description) {
                        val query: PreparedStatement = db.prepareStatement("UPDATE Employee_Project SET STR_description='${data.description}' WHERE STR_project='${contract.project.name}';")
                        query.execute()
                    }
                    rows++
                    println("Query OK! Number of affected rows: ${rows}")
                } catch (e: SQLException){
                    println("Failed to update data due to: ${e.message}")
                    e.printStackTrace()
                }
            }

            job.join()
            return if(job.isCompleted){
                rows
            }else{
                rows
            }
        }

    override suspend fun get(varchar: String): ProjectEmployeeContract? {
            var _contract: ProjectEmployeeContract? = null
            val job: Job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val query: PreparedStatement = db.prepareStatement("SELECT * FROM Employee_Project WHERE STR_project ='${varchar}';")
                    val res: ResultSet = query.executeQuery()
                    res.next()

                    if (res.row == 1) {
                        val data: ProjectEmployeeContract = ProjectEmployeeContract(
                            res.getInt("id_contract"),
                            projectAccessor.get(res.getString("STR_project"))!!,
                            employeeAccessor.get(res.getString("STR_employee"))!!,
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

    override suspend fun getAll(): ArrayList<ProjectEmployeeContract> {
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
                            projectAccessor.get(res.getString("STR_project"))!!,
                            employeeAccessor.get(res.getString("STR_employee"))!!
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

    override suspend fun delete(model: ProjectEmployeeContract): Int {
            var rows: Int = 0
            val job: Job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val data: ProjectEmployeeContract? = get(model.project.name)
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

    override fun turnTestOn() {
        db = DataBaseConnection.TEST_CONNECTION
    }

    override fun turnTestOff() {
        db = DataBaseConnection.CONNECTION
    }
    
}