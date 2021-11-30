package com.csestateconnect.www.csconnect.models.user;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Serializable, Parcelable
{

    @SerializedName("country")
    @Expose
    private Country country;
    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("rm")
    @Expose
    private Rm rm;
    @SerializedName("email_verified")
    @Expose
    private Boolean emailVerified;
    public final static Parcelable.Creator<User> CREATOR = new Creator<User>() {


        @SuppressWarnings({
                "unchecked"
        })
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return (new User[size]);
        }

    }
            ;
    private final static long serialVersionUID = 3719560270742627175L;

    protected User(Parcel in) {
        this.country = ((Country) in.readValue((Country.class.getClassLoader())));
        this.city = ((City) in.readValue((City.class.getClassLoader())));
        this.state = ((String) in.readValue((String.class.getClassLoader())));
        this.profileImage = ((String) in.readValue((String.class.getClassLoader())));
        this.latitude = ((String) in.readValue((String.class.getClassLoader())));
        this.longitude = ((String) in.readValue((String.class.getClassLoader())));
        this.username = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.phoneNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.rm = ((Rm) in.readValue((Rm.class.getClassLoader())));
        this.emailVerified = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public User() {
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Rm getRm() {
        return rm;
    }

    public void setRm(Rm rm) {
        this.rm = rm;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(country);
        dest.writeValue(city);
        dest.writeValue(state);
        dest.writeValue(profileImage);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
        dest.writeValue(username);
        dest.writeValue(email);
        dest.writeValue(phoneNumber);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(rm);
        dest.writeValue(emailVerified);
    }

    public int describeContents() {
        return 0;
    }

}