package org.example.cahousing.DataSource.Utilities

class PostalCodeFormatter() {

    fun toCepFormat(cep: String): String {
        val builder: StringBuilder = StringBuilder()

        if (cep.length == 8) {
            builder.append(cep.get(0)).append(cep.get(1)).append(".").append(cep.get(2))
                .append(cep.get(3)).append(cep.get(4)).append("-").append(cep.get(5))
                .append(cep.get(6)).append(cep.get(7))
        } else {
            println("Argument length: ${cep.length}, argument size below the CEP pattern.")
            throw IllegalArgumentException("Argument must have length 9")
        }
        return builder.toString()
    }

    fun isValid(postalCode: String) : Boolean {
        return if(
            postalCode.length == 8 &&
            !postalCode.contains("abcdefghijklmnopqrstuvwxyz") &&
            !postalCode.contains("abcdefghijklmnopqrstuvwxyz".uppercase())) {
            true
        }else if(
            postalCode.length == 9 &&
            postalCode.contains(".-") &&
            !postalCode.contains("abcdefghijklmnopqrstuvwxyz") &&
            !postalCode.contains("abcdefghijklmnopqrstuvwxyz".uppercase())){
            true
        } else {
            //println("${postalCode} is a data in a invalid format, this data must only contains numbers and a size of 8 if the input hasn't any dots or hyphen, and size of 10 if the input has punctuation marks.")
            throw IllegalArgumentException("${postalCode} is a data in a invalid format, this data must only contains numbers and a size of 8 if the input hasn't any dots or hyphen, and size of 10 if the input has punctuation marks.")
        }
    }
}