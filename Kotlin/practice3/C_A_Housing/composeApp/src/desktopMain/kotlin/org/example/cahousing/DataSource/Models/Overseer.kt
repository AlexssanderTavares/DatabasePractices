package org.example.cahousing.DataSource.Models

import java.sql.Timestamp
import java.time.LocalDateTime

data class Overseer(
    val id: Int? = null,
    val empName: String,
    var wage: Double,
    var timeWorked: String? = "0000-00-00 00:00:00"
) : Models