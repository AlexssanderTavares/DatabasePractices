package org.example.cahousing.DataSource.Models

import java.sql.Timestamp
import java.time.LocalDateTime

data class Overseer(
    val id: Int,
    val empName: String,
    var wage: Double,
    var timeWorked: String? = ""
) : Models