package com.prologic.strains.model

data class ErrorAlert(val action: Int, val message: String)
enum class ActionAlert {
    CLOSE, NO
}