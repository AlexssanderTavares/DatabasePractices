package org.example.cahousing.DataSource.Data.Accessors

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.example.cahousing.DataBaseConnection
import org.example.cahousing.DataSource.Models.Dept
import org.example.cahousing.DataSource.Models.Models
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


class DeptAccessor : DataBaseAccessor<Dept> {

    companion object {
        private val db = DataBaseConnection.CONNECTION
    }
    override suspend fun create(model: Dept): Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {

            getAll().forEach {
                if (it.name == model.name) {
                    throw SQLException("Every Department name has an unique name. Can't create another department with that name.")
                }
            }

            try {
                val query: PreparedStatement =
                    db.prepareStatement("INSERT INTO Dept (STR_name, STR_description) VALUES ('${model.name}','${model.description}');")
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

    override suspend fun get(varchar: String): Dept? {
        var data: Dept? = null
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("SELECT * FROM Dept WHERE STR_name = '${varchar}';")
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

    override suspend fun update(model: Dept, data: Dept): Int {

        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val oldData: Dept? = get(model.name)
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
                    throw SQLException("Data doesn't match. ${model} and ${data} is not the same.")
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

    override suspend fun delete(model: Dept): Int {

        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("DELETE FROM Dept WHERE STR_name='${model.name}';")
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

    override suspend fun getAll(): ArrayList<Dept> {
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
}