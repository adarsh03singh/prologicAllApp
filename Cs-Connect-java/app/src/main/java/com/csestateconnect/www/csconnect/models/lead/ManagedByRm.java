package com.csestateconnect.www.csconnect.models.lead;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ManagedByRm implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    public final static Parcelable.Creator<ManagedByRm> CREATOR = new Creator<ManagedByRm>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ManagedByRm createFromParcel(Parcel in) {
            return new ManagedByRm(in);
        }

        public ManagedByRm[] newArray(int size) {
            return (new ManagedByRm[size]);
        }

    }
            ;
    private final static long serialVersionUID = 7213152575879230774L;

    protected ManagedByRm(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.phoneNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ManagedByRm() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(phoneNumber);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(email);
    }

    public int describeContents() {
        return 0;
    }

}