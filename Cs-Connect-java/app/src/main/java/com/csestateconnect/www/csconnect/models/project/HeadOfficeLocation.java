package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeadOfficeLocation implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pin_code")
    @Expose
    private String pinCode;
    @SerializedName("icon_image")
    @Expose
    private String iconImage;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    public final static Parcelable.Creator<HeadOfficeLocation> CREATOR = new Creator<HeadOfficeLocation>() {


        @SuppressWarnings({
                "unchecked"
        })
        public HeadOfficeLocation createFromParcel(Parcel in) {
            return new HeadOfficeLocation(in);
        }

        public HeadOfficeLocation[] newArray(int size) {
            return (new HeadOfficeLocation[size]);
        }

    }
            ;
    private final static long serialVersionUID = 7512514656638110112L;

    protected HeadOfficeLocation(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.pinCode = ((String) in.readValue((String.class.getClassLoader())));
        this.iconImage = ((String) in.readValue((String.class.getClassLoader())));
        this.latitude = ((String) in.readValue((String.class.getClassLoader())));
        this.longitude = ((String) in.readValue((String.class.getClassLoader())));
    }

    public HeadOfficeLocation() {
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

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
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
        dest.writeValue(pinCode);
        dest.writeValue(iconImage);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
    }

    public int describeContents() {
        return 0;
    }

}