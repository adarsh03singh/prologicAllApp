package com.csestateconnect.db.data

class SearchResult {

    private var id: Int? = null
    private var name: String? = null
    private var type: String? = null
    private var location: String? = null

    constructor(mName: String, mType: String) {
        this.name = mName
        this.type = mType
    }

    constructor(mId: Int?, mName: String, mType: String, mLocation: String) {
        this.id = mId
        this.name = mName
        this.type = mType
        this.location = mLocation
    }

    fun getId(): Int? {
        return id
    }

    fun getName(): String? {
        return name
    }

    fun getType(): String? {
        return type
    }

    fun getLocation(): String {
        return location!!
    }
}