package org.example.cahousing.DataSource.Utilities

class Cep() : Formatter {

    override fun format(value: String): String {
        val builder: StringBuilder = StringBuilder()

        if (this.isValid(value)) {
            if(value.contains(".") && value.contains("-")){
                return value
            } else {
                builder.append(value.get(0)).append(value.get(1)).append(".").append(value.get(2))
                    .append(value.get(3)).append(value.get(4)).append("-").append(value.get(5))
                    .append(value.get(6)).append(value.get(7))
            }
        } else {
            println("Argument length: ${value.length}, argument size below the CEP pattern.")
            throw IllegalArgumentException("Argument must have length 9")
        }
        return builder.toString()
    }

    override fun isValid(value: String) : Boolean {
        return if(
            value.length == 8 &&
            !value.contains("abcdefghijklmnopqrstuvwxyz") &&
            !value.contains("abcdefghijklmnopqrstuvwxyz".uppercase())) {
            true
        }else if(
            value.length == 10 &&
            value.contains(".") &&
            value.contains("-") &&
            !value.contains("abcdefghijklmnopqrstuvwxyz") &&
            !value.contains("abcdefghijklmnopqrstuvwxyz".uppercase())){
            true
        } else {
            throw IllegalArgumentException("${value} is a data in a invalid format, this data must only contains numbers and a size of 8 if the input hasn't any dots or hyphen, and size of 10 if the input has punctuation marks.")
        }
    }
}