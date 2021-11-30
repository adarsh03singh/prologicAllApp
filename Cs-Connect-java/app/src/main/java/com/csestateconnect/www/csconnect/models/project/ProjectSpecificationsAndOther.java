package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectSpecificationsAndOther implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("area_name")
    @Expose
    private String areaName;
    @SerializedName("icon_image")
    @Expose
    private String iconImage;
    public final static Parcelable.Creator<ProjectSpecificationsAndOther> CREATOR = new Creator<ProjectSpecificationsAndOther>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ProjectSpecificationsAndOther createFromParcel(Parcel in) {
            return new ProjectSpecificationsAndOther(in);
        }

        public ProjectSpecificationsAndOther[] newArray(int size) {
            return (new ProjectSpecificationsAndOther[size]);
        }

    }
            ;
    private final static long serialVersionUID = 2937156524426910185L;

    protected ProjectSpecificationsAndOther(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.areaName = ((String) in.readValue((String.class.getClassLoader())));
        this.iconImage = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ProjectSpecificationsAndOther() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(type);
        dest.writeValue(areaName);
        dest.writeValue(iconImage);
    }

    public int describeContents() {
        return 0;
    }

}