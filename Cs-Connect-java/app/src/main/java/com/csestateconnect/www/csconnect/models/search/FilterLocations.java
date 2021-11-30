
package com.csestateconnect.www.csconnect.models.search;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterLocations implements Serializable
{

    @SerializedName("doc_count")
    @Expose
    private Long docCount;
    @SerializedName("locations")
    @Expose
    private Locations locations;
    private final static long serialVersionUID = 2866690017081041888L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FilterLocations() {
    }

    /**
     * 
     * @param locations
     * @param docCount
     */
    public FilterLocations(Long docCount, Locations locations) {
        super();
        this.docCount = docCount;
        this.locations = locations;
    }

    public Long getDocCount() {
        return docCount;
    }

    public void setDocCount(Long docCount) {
        this.docCount = docCount;
    }

    public Locations getLocations() {
        return locations;
    }

    public void setLocations(Locations locations) {
        this.locations = locations;
    }

}
