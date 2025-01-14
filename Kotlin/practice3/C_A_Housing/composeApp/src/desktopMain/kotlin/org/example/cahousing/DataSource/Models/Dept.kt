package org.example.cahousing.DataSource.Models

data class Dept(
    val id: Int,
    val name: String,
    var description: String = ""
) : Models