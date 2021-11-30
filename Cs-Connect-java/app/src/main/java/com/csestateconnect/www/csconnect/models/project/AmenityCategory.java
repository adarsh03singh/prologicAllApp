package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AmenityCategory implements Serializable, Parcelable
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
    public final static Parcelable.Creator<AmenityCategory> CREATOR = new Creator<AmenityCategory>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AmenityCategory createFromParcel(Parcel in) {
            return new AmenityCategory(in);
        }

        public AmenityCategory[] newArray(int size) {
            return (new AmenityCategory[size]);
        }

    }
            ;
    private final static long serialVersionUID = -8473429828668552714L;

    protected AmenityCategory(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.iconImage = ((String) in.readValue((String.class.getClassLoader())));
    }

    public AmenityCategory() {
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