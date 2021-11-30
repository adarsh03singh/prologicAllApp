
package com.csestateconnect.www.csconnect.models.project_detail;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AmenityCategory implements Serializable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("amenities")
    @Expose
    private List<Amenity> amenities = null;
    private final static long serialVersionUID = 7905507076152230838L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public AmenityCategory() {
    }

    /**
     * 
     * @param name
     * @param amenities
     */
    public AmenityCategory(String name, List<Amenity> amenities) {
        super();
        this.name = name;
        this.amenities = amenities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AmenityCategory withName(String name) {
        this.name = name;
        return this;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void addAmenity(Amenity amenity){
        this.amenities.add(amenity);
    }

    public void setAmenities(List<Amenity> amanities) {
        this.amenities = amenities;
    }

    public AmenityCategory withAmanities(List<Amenity> amenities) {
        this.amenities = amenities;
        return this;
    }

}
