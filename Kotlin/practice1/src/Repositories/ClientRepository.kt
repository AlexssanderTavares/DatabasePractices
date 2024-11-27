package Repositories

import DataSources.DataSource
import Models.Client
import java.sql.Connection
import java.sql.SQLException

class ClientRepository  {

    private var db: Connection = DataSource.CONNECTION // Singleton

    fun insert(client: Client): Int {
        var rowAffected: Int = 0
        val query = db.prepareStatement(
            "INSERT INTO Cliente (cl_name, cl_cpf, cl_born_date, cl_tipo) values ('${client.name}', '${client.cpf}', '${client.bornDate}', ${client.type});")
        try{
            query.execute()
            rowAffected += 1
            println("Query OK, rows affected: $rowAffected")
            return rowAffected
        }catch (e: SQLException){
            e.printStackTrace()
            println(e.message)
            return rowAffected
        }
        return rowAffected
    }

    fun update(): Int {
        TODO("Not yet implemented")
    }

    fun delete(): Int {
        TODO("Not yet implemented")
    }

    fun select() {
        TODO("Not yet implemented")
    }

    fun selectAll() {
        TODO("Not yet implemented")
    }

    /*fun get() : Client {

    }

    fun getAll() : ArrayList<Client>{

    }*/
}