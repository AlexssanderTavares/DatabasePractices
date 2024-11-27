//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import DataSources.DataSource
import Models.Client
import Repositories.ClientRepository
import java.sql.*
import java.util.*

fun main() {

    val client = ClientRepository()

    val newClient: Client = Client("Lindiane", "22233344411", "2000-05-30", 2)

    client.insert(newClient)




}