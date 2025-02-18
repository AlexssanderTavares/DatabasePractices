package org.example.cahousing.DataSource.Models

import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

data class Employee(
    var id: Int? = null,
    val name: String,
    val sex: String,
    var wage: Double = 0.0,
    var bornDate: String?,
    var timeWorked: String?,
    var address: String,
    var idDept: Int = 0
) : Models