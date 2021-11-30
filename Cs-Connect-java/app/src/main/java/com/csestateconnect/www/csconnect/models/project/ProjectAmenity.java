package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectAmenity implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("icon_image")
    @Expose
    private String iconImage;
    @SerializedName("amenity_category")
    @Expose
    private AmenityCategory amenityCategory;
    public final static Parcelable.Creator<ProjectAmenity> CREATOR = new Creator<ProjectAmenity>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ProjectAmenity createFromParcel(Parcel in) {
            return new ProjectAmenity(in);
        }

        public ProjectAmenity[] newArray(int size) {
            return (new ProjectAmenity[size]);
        }

    }
            ;
    private final static long serialVersionUID = 5669871426257961858L;

    protected ProjectAmenity(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.iconImage = ((String) in.readValue((String.class.getClassLoader())));
        this.amenityCategory = ((AmenityCategory) in.readValue((AmenityCategory.class.getClassLoader())));
    }

    public ProjectAmenity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public AmenityCategory getAmenityCategory() {
        return amenityCategory;
    }

    public void setAmenityCategory(AmenityCategory amenityCategory) {
        this.amenityCategory = amenityCategory;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(iconImage);
        dest.writeValue(amenityCategory);
    }

    public int describeContents() {
        return 0;
    }

}