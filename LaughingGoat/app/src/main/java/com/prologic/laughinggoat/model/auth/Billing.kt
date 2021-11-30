package  com.prologic.laughinggoat.model.auth

data class Billing(
    val address_1: String,
    val address_2: String,
    val city: String,
    val company: String,
    val country: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val postcode: String,
    val state: String
)