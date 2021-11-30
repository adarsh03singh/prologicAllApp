package com.csestateconnect

class CreateLeadData(name: String, number: String, emailid: String?, country: Int?, city: Int?,
                     minimum: String?, maximum: String?, currency: String?,
                     locationStatus: List<Int>?, propertyType: List<Int>?, preferredStatus: List<Int>?, leadStatus: Int) {
    val name = name
    val phone_number = number
    val email = emailid
    val country = country
    val city = city
    val min_budget_view = minimum
    val max_budget_view = maximum
    val budget_currency = currency
    val preferred_location = locationStatus
    val preferred_property_type  = propertyType
    val preferred_status = preferredStatus
    val lead_status = leadStatus

}
