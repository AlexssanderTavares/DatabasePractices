package org.example.cahousing.DataSource.Models

data class Employee(
    var id: Int? = null,
    val name: String,
    val sex: String,
    var wage: Double = 0.0,
    var bornDate: String? = "2000/01/01",
    var timeWorked: String?,
    var address: String,
    var idDept: Int = 0
)