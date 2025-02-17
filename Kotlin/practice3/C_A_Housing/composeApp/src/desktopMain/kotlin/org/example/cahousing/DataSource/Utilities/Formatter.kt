package org.example.cahousing.DataSource.Utilities

interface Formatter {

    fun format(value: String) : String

    fun isValid(value: String) : Boolean
}