package org.example.cahousing.DataSource.Data.Accessors

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.example.cahousing.DataBaseConnection
import org.example.cahousing.DataSource.Models.Address
import org.example.cahousing.DataSource.Models.Employee
import org.example.cahousing.DataSource.Models.Overseer
import org.example.cahousing.Factories.DataBaseAccessorFactory
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class OverseerAccessor: OverseerAccessorImp {

    companion object {
        private var db: Connection = DataBaseConnection.CONNECTION
        private var employeeAccessor: EmployeeAccessorImp = EmployeeAccessor()
    }

    override suspend fun create(model: Overseer): Int {
            var rows: Int = 0
            val job: Job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val employee: Employee? = employeeAccessor.get(model.empName)

                    val query: PreparedStatement =
                        db.prepareStatement("INSERT INTO Overseer (STR_EMP_name, F_wage, time_worked_journey) VALUES ('${employee?.name}', '${model.wage}', '${model.timeWorked}');")
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

    override suspend fun update(model: Overseer, data: Overseer) : Int{
            var rows: Int = 0
            val job: Job = CoroutineScope(Dispatchers.IO).launch {
                try{
                    val oldData: Overseer? = get(model.empName)
                    if(oldData != null && oldData.empName == data.empName){
                        if(oldData.wage != data.wage) {
                            val query: PreparedStatement =
                                db.prepareStatement("UPDATE Overseer SET F_wage='${data.wage}' WHERE F_wage='${oldData.wage}';")
                            query.execute()
                        }

                        if(oldData.timeWorked != data.timeWorked || oldData.timeWorked == null){
                            val query: PreparedStatement =
                                db.prepareStatement("UPDATE Overseer SET time_worked_journey='${data.timeWorked}' WHERE time_worked_journey='${oldData.timeWorked}';")
                            query.execute()
                        }
                        rows++
                        println("Query Ok! Number of affected rows: ${rows}")
                    }else{
                        throw SQLException("Data doesn't match. ${model.empName} and ${data.empName} is not the same.")
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

    override suspend fun get(varchar: String): Overseer? {
            lateinit var _overseer: Overseer
            val job: Job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val query: PreparedStatement =
                        db.prepareStatement("SELECT * FROM Overseer WHERE STR_EMP_name='${varchar}';")
                    val res: ResultSet = query.executeQuery()
                    res.next()

                    _overseer = Overseer(
                        res.getInt("I_id"),
                        res.getString("STR_EMP_name"),
                        res.getDouble("F_wage"),
                        res.getTimestamp("time_worked_journey")?.toString() ?: "2000-01-01 00:00:00"
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

    override suspend fun getAll(): ArrayList<Overseer> {
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

    override suspend fun delete(model: Overseer): Int {
            var rows: Int = 0
            val job: Job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val query: PreparedStatement =
                        db.prepareStatement("DELETE FROM Overseer WHERE STR_EMP_name='${model.empName}';")
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

    override fun turnTestOn() {
        db = DataBaseConnection.TEST_CONNECTION
    }

    override fun turnTestOff() {
        db = DataBaseConnection.CONNECTION
    }
}