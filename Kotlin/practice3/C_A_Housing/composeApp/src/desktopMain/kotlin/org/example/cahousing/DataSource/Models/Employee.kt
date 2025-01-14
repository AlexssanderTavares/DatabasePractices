package org.example.cahousing.DataSource.Models

data class Employee(
    var id: Int? = null,
    val name: String,
    val sex: String,
    var wage: Double = 0.0,
    var bornDate: String? = "0000-00-00",
    var timeWorked: String? = "0000-00-00 00:00:00",
    var address: String,
    var idDept: Int = 0
) : Models