package  com.prologic.laughinggoat.model.auth



data class Customer(
    val customer: UserResult
)

data class UserResult(
    val id: Int,
    val avatar_url: String,
    val username: String,
    val date_created: String,
    val date_created_gmt: String,
    val date_modified: String,
    val date_modified_gmt: String,
    val email: String,
    val first_name: String,
    val is_paying_customer: Boolean,
    val last_name: String,
    val role: String,
    val billing: Billing,
    val shipping: Shipping,
)

