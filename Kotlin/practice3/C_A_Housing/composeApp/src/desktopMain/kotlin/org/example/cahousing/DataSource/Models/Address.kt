package org.example.cahousing.DataSource.Models

data class Address(
    var cep: String,
    val road: String,
    val district: String,
    val city: String
) : Models