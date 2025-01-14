package org.example.cahousing.DataSource.Models

data class ProjectEmployeeContract(
    val id: Int,
    val project: Project,
    val employee: Employee
) : Models