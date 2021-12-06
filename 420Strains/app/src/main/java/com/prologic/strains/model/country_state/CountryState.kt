package com.blog.prologic.model.country_state

class CountryState {
    val countries = ArrayList<Country>()
}

data class Country(
    val alpha2Code: String,
    val alpha3Code: String,
    val country: String,
    val numberCode: String,
    val states: List<String>
)