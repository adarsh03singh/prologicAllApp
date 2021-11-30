
package com.csestateconnect.www.csconnect.models.search;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterAmenities implements Serializable
{

    @SerializedName("doc_count")
    @Expose
    private Long docCount;
    @SerializedName("amenities")
    @Expose
    private Amenities amenities;
    private final static long serialVersionUID = -2059407294969538347L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FilterAmenities() {
    }

    /**
     * 
     * @param amenities
     * @param docCount
     */
    public FilterAmenities(Long docCount, Amenities amenities) {
        super();
        this.docCount = docCount;
        this.amenities = amenities;
    }

    public Long getDocCount() {
        return docCount;
    }

    public void setDocCount(Long docCount) {
        this.docCount = docCount;
    }

    public Amenities getAmenities() {
        return amenities;
    }

    public void setAmenities(Amenities amenities) {
        this.amenities = amenities;
    }

}
