package com.prologic.laughinggoat.model.blog

class BlogResult : ArrayList<BlogItem>()

data class BlogItem(
    val content: String,
    val id: String,
    val post_date: String,
    val post_author_name: String,
    val source_url: String,
    val title: String
)