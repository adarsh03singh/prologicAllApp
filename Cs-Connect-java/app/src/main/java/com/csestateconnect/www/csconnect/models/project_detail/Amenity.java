
package com.csestateconnect.www.csconnect.models.project_detail;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Amenity implements Serializable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    private final static long serialVersionUID = -3473834787336510378L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Amenity() {
    }

    /**
     * 
     * @param name
     * @param image
     */
    public Amenity(String name, String image) {
        super();
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Amenity withName(String name) {
        this.name = name;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Amenity withImage(String image) {
        this.image = image;
        return this;
    }

}
