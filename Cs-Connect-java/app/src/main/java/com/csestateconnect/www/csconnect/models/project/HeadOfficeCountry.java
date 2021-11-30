package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeadOfficeCountry implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("three_digit_code")
    @Expose
    private String threeDigitCode;
    @SerializedName("two_digit_code")
    @Expose
    private String twoDigitCode;
    @SerializedName("number_code")
    @Expose
    private String numberCode;
    @SerializedName("flag_image")
    @Expose
    private String flagImage;
    @SerializedName("icon_image")
    @Expose
    private String iconImage;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    public final static Parcelable.Creator<HeadOfficeCountry> CREATOR = new Creator<HeadOfficeCountry>() {


        @SuppressWarnings({
                "unchecked"
        })
        public HeadOfficeCountry createFromParcel(Parcel in) {
            return new HeadOfficeCountry(in);
        }

        public HeadOfficeCountry[] newArray(int size) {
            return (new HeadOfficeCountry[size]);
        }

    }
            ;
    private final static long serialVersionUID = 7056843702571559810L;

    protected HeadOfficeCountry(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.threeDigitCode = ((String) in.readValue((String.class.getClassLoader())));
        this.twoDigitCode = ((String) in.readValue((String.class.getClassLoader())));
        this.numberCode = ((String) in.readValue((String.class.getClassLoader())));
        this.flagImage = ((String) in.readValue((String.class.getClassLoader())));
        this.iconImage = ((String) in.readValue((String.class.getClassLoader())));
        this.latitude = ((String) in.readValue((String.class.getClassLoader())));
        this.longitude = ((String) in.readValue((String.class.getClassLoader())));
    }

    public HeadOfficeCountry() {
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

    public String getThreeDigitCode() {
        return threeDigitCode;
    }

    public void setThreeDigitCode(String threeDigitCode) {
        this.threeDigitCode = threeDigitCode;
    }

    public String getTwoDigitCode() {
        return twoDigitCode;
    }

    public void setTwoDigitCode(String twoDigitCode) {
        this.twoDigitCode = twoDigitCode;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    public String getFlagImage() {
        return flagImage;
    }

    public void setFlagImage(String flagImage) {
        this.flagImage = flagImage;
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
        dest.writeValue(threeDigitCode);
        dest.writeValue(twoDigitCode);
        dest.writeValue(numberCode);
        dest.writeValue(flagImage);
        dest.writeValue(iconImage);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
    }

    public int describeContents() {
        return 0;
    }

}