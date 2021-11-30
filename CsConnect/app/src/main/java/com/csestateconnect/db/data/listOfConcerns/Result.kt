package com.csestateconnect.db.data.listOfConcerns

data class Result(
    val id: Int,
    val rm: Rm,
    val user: User,
    val closed_by: ClosedBy,
    val comments: List<Comments>?,
    val created_at: String,
    val updated_at: String,
    val heading: String,
    val description: String,
    val closed: Boolean,
    val closure_date: String
)