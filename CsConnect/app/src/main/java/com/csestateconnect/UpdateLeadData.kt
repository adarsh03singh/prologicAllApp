package com.csestateconnect

class UpdateLeadData(name: String, status: String, budget: String, location: String,
                     preferredPropertyType: String, preferredStatus: String, address: String, email: String, mobile: String,
                     createdDate: String, modifiedDate: String){

    val leads_name: String = name
    val leads_status = status
    val leads_budget = budget
//    val leads_id = id
    val leads_location = location
    val preferred_property_type = preferredPropertyType
    val preferred_status = preferredStatus
    val leads_address = address
    val leads_email = email
    val leads_mobile = mobile
    val leads_created_date = createdDate
    val leads_modified_date = modifiedDate
}
