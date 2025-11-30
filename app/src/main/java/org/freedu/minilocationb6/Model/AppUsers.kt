package org.freedu.minilocationb6.Model

data class AppUsers(
    val userId: String = "",
    val email: String = "",
    val username: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
)