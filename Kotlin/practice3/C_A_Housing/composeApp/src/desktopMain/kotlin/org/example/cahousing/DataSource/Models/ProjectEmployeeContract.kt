package org.example.cahousing.DataSource.Models

data class ProjectEmployeeContract(
    val id: Int? = null,
    val project: Project,
    val employee: Employee,
    var description: String = ""
) : Models