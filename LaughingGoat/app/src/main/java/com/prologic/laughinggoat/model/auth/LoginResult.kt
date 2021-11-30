package  com.prologic.laughinggoat.model.auth

data class LoginResult(
    val token: String,
    val user_display_name: String,
    val user_email: String,
    val user_nicename: String
)