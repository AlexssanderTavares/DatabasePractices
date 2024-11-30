package DataSources.Repositories

import DataSources.Data.DatabaseConnection
import Models.Product
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.SQLTimeoutException

class ProductRepository(private val testEnvironment: Boolean = false) {

    private var db: Connection

    init {
        if (testEnvironment) {
            db = DatabaseConnection.connect("Practice2Test")
        } else {
            db = DatabaseConnection.CONNECTION
        }
    }


    fun insert(productName: String, quantity: Int = 0, category: Int): Int {
        var rowsAffected: Int = 0
        try {
            if (productName.length < 120 && category <= 3 || category > 0) {
                val query: PreparedStatement =
                    this.db.prepareStatement("INSERT INTO Product (str_name, i_quantity, i_category) VALUES ('${productName}', '${quantity}', '${category}');")

                query.execute()
                rowsAffected += 1
                println("Insertion Success! Query ok: ${rowsAffected} row affected")
                db.close()
                return rowsAffected
            } else {
                throw IllegalArgumentException("Product Name must have length below 120 chars and above 0, category must be above 0 and below 3.")
            }
        } catch (e: SQLException) {
            e.message
            e.printStackTrace()
            println("Insertion failed or aborted! ${rowsAffected} rows affected")
            return rowsAffected
        } catch (e: IllegalArgumentException) {
            e.message
            e.printStackTrace()
            println("You must fulfill the right data requirements in order to register some data.")
            println("Product Name Size: ${productName.length} | Category: ${category}")
        }
        return rowsAffected
    }

    fun get(name: String): Product? {
        var product: Product? = null
        try {
            val query: PreparedStatement = this.db.prepareStatement("SELECT * FROM Product;")

            try {
                val res: ResultSet = query.executeQuery()
                res.next() // moves cursor from row 0 to 1
                try {
                    var res2: String = res.getString("str_name")
                    while (res2 != name) {
                        res.next()
                        res2 = res.getString("str_name")
                    }

                    if (res2 == name) {
                        product = Product(res.getString("str_name"), res.getInt("i_quantity"), res.getInt("i_category"))
                        return product
                    }
                } catch (e: SQLException) {
                    println(e.message)
                    e.printStackTrace()
                    println("Invalid column OR closed ResultSet")
                }
            } catch (e: SQLException) {
                println(e.message)
                e.printStackTrace()
                println("The statement doesn't returned anything from DB, this could happen due to connection error or SQL statement error.")
            } catch (e: SQLTimeoutException) {
                println(e.message)
                e.printStackTrace()
                println("Connection timeout.")
            }
        } catch (e: SQLException) {
            println(e.message)
            e.printStackTrace()
            println("Closed connection.")
        }
        return product
    }

    /*fun delete(productName: String) : Int {
        val product: Product? = this.get(productName)
        if(product != null){
            val query: PreparedStatement = this.db.prepareStatement("DELETE ")
        }

    }*/

}