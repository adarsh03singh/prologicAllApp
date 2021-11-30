package com.csestateconnect.www.csconnect.models.user;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rm implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    public final static Parcelable.Creator<Rm> CREATOR = new Creator<Rm>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Rm createFromParcel(Parcel in) {
            return new Rm(in);
        }

        public Rm[] newArray(int size) {
            return (new Rm[size]);
        }

    }
            ;
    private final static long serialVersionUID = -6640193109141675657L;

    protected Rm(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.phoneNumber = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Rm() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(email);
        dest.writeValue(phoneNumber);
    }

    public int describeContents() {
        return 0;
    }

}