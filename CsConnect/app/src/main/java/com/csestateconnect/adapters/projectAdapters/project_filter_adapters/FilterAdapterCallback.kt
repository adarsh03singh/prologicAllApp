package com.csestateconnect.adapters.projectAdapters.project_filter_adapters

interface FilterAdapterCallback {

    fun onUnitCheckedCallback(position: Int?, checkedValue: Boolean?)
    fun onStatusCheckedCallback(position: Int?, checkedValue: Boolean?)
    fun onAmenityCheckedCallback(position: Int?, checkedValue: Boolean?)
    fun onLocationCheckedCallback(position: Int, checkedValue: Boolean)
}
