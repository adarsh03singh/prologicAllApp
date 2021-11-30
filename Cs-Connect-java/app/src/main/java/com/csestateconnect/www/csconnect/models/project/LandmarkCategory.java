package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LandmarkCategory implements Serializable, Parcelable
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
    public final static Parcelable.Creator<LandmarkCategory> CREATOR = new Creator<LandmarkCategory>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LandmarkCategory createFromParcel(Parcel in) {
            return new LandmarkCategory(in);
        }

        public LandmarkCategory[] newArray(int size) {
            return (new LandmarkCategory[size]);
        }

    }
            ;
    private final static long serialVersionUID = -8108363292501789989L;

    protected LandmarkCategory(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.iconImage = ((String) in.readValue((String.class.getClassLoader())));
    }

    public LandmarkCategory() {
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(iconImage);
    }

    public int describeContents() {
        return 0;
    }

}