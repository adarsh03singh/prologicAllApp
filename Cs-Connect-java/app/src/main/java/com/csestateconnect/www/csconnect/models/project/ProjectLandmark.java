package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectLandmark implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("icon_image")
    @Expose
    private String iconImage;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("landmark_category")
    @Expose
    private LandmarkCategory landmarkCategory;
    public final static Parcelable.Creator<ProjectLandmark> CREATOR = new Creator<ProjectLandmark>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ProjectLandmark createFromParcel(Parcel in) {
            return new ProjectLandmark(in);
        }

        public ProjectLandmark[] newArray(int size) {
            return (new ProjectLandmark[size]);
        }

    }
            ;
    private final static long serialVersionUID = 1149745506221113712L;

    protected ProjectLandmark(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));
        this.iconImage = ((String) in.readValue((String.class.getClassLoader())));
        this.latitude = ((String) in.readValue((String.class.getClassLoader())));
        this.longitude = ((String) in.readValue((String.class.getClassLoader())));
        this.landmarkCategory = ((LandmarkCategory) in.readValue((LandmarkCategory.class.getClassLoader())));
    }

    public ProjectLandmark() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public LandmarkCategory getLandmarkCategory() {
        return landmarkCategory;
    }

    public void setLandmarkCategory(LandmarkCategory landmarkCategory) {
        this.landmarkCategory = landmarkCategory;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(address);
        dest.writeValue(iconImage);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
        dest.writeValue(landmarkCategory);
    }

    public int describeContents() {
        return 0;
    }

}