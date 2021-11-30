package com.csestateconnect.www.csconnect.models.search;


public class SearchResult {

    private Integer id;
    private String name;
    private String type;
    private String location;

    public SearchResult(String mName, String mType){
        this.name = mName;
        this.type = mType;
    }

    public SearchResult(Integer mId, String mName, String mType, String mLocation){
        this.id = mId;
        this.name = mName;
        this.type = mType;
        this.location = mLocation;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }
}
