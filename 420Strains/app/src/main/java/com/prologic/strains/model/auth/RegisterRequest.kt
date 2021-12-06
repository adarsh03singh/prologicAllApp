package  com.prologic.strains.model.auth



class RegisterRequest(
    val username: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val password: String,
    val shipping: Shipping,
    val billing: Billing
)




