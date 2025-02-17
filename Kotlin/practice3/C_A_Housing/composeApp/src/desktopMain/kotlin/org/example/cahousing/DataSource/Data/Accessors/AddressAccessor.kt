package org.example.cahousing.DataSource.Data.Accessors

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.example.cahousing.DataBaseConnection
import org.example.cahousing.DataSource.Models.Address
import org.example.cahousing.DataSource.Utilities.Cep
import org.example.cahousing.DataSource.Utilities.Formatter
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class AddressAccessor : DataBaseAccessor<Address>{

    private val db: Connection = DataBaseConnection.CONNECTION
    private val formatter: Formatter = Cep()

    override suspend fun create(model: Address): Int {
        var rows: Int = 0
        if (formatter.isValid(model.cep)) {

            val job: Job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val query: PreparedStatement =
                        db.prepareStatement(
                            "INSERT INTO Address (I_cep, STR_road, STR_district, STR_city) VALUES ('${formatter.format(model.cep)}','${model.road}','${model.district}','${model.city}');")
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

    override suspend fun update(model: Address, data: Address): Int {
        var rows: Int = 0
        model.cep = formatter.format(model.cep)
        data.cep = formatter.format(data.cep)

        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            if (formatter.isValid(model.cep)) {
                try {

                    val address: Address? = get(model.cep)
                    if(address != null && address.cep == model.cep){

                        if(address.road != data.road) {
                            val query: PreparedStatement =
                                db.prepareStatement("UPDATE Address SET STR_road='${data.road}' WHERE I_cep='${address.cep}';")
                            query.execute()
                        }

                        if(address.city != data.city){
                            val query: PreparedStatement =
                                db.prepareStatement("UPDATE Address SET STR_city='${data.city}' WHERE I_cep='${address.cep}';")
                            query.execute()
                        }

                        if(address.district != data.district){
                            val query: PreparedStatement =
                                db.prepareStatement("UPDATE Address SET STR_district='${data.district}' WHERE I_cep='${address.district}';")
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
            }else {
                throw IllegalArgumentException("${model} doesn't exist.")
            }
        }
        job.join()
        return if (job.isCompleted) {
            rows
        } else {
            rows
        }
    }

    override suspend fun get(varchar: String): Address? {
        if(!formatter.isValid(varchar)) {
            throw IllegalArgumentException("${varchar} is not a valid postal code.")
        }
        lateinit var data: Address

        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val query: PreparedStatement =
                    db.prepareStatement("SELECT * FROM Address WHERE I_cep='${varchar}';")
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

    override suspend fun getAll(): ArrayList<Address> {
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

    override suspend fun delete(model: Address) : Int {
        var rows: Int = 0
        val job: Job = CoroutineScope(Dispatchers.IO).launch {
            try{
                val data: Address = get(model.cep) ?: throw SQLException("There is no such data.")
                val query: PreparedStatement = db.prepareStatement("DELETE FROM Address WHERE I_cep='${model.cep}';")
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
}