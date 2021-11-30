package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeadOfficeCity implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("number_code")
    @Expose
    private String numberCode;
    @SerializedName("icon_image")
    @Expose
    private String iconImage;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    public final static Parcelable.Creator<HeadOfficeCity> CREATOR = new Creator<HeadOfficeCity>() {


        @SuppressWarnings({
                "unchecked"
        })
        public HeadOfficeCity createFromParcel(Parcel in) {
            return new HeadOfficeCity(in);
        }

        public HeadOfficeCity[] newArray(int size) {
            return (new HeadOfficeCity[size]);
        }

    }
            ;
    private final static long serialVersionUID = -3753592231898015016L;

    protected HeadOfficeCity(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.numberCode = ((String) in.readValue((String.class.getClassLoader())));
        this.iconImage = ((String) in.readValue((String.class.getClassLoader())));
        this.latitude = ((String) in.readValue((String.class.getClassLoader())));
        this.longitude = ((String) in.readValue((String.class.getClassLoader())));
    }

    public HeadOfficeCity() {
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

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(numberCode);
        dest.writeValue(iconImage);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
    }

    public int describeContents() {
        return 0;
    }

}