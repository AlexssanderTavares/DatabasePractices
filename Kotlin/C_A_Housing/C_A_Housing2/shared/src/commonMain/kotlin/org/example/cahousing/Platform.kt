package org.example.cahousing

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform