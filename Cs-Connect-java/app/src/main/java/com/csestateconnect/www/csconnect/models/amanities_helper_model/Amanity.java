
package com.csestateconnect.www.csconnect.models.amanities_helper_model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Amanity implements Serializable, Parcelable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    public final static Parcelable.Creator<Amanity> CREATOR = new Creator<Amanity>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Amanity createFromParcel(Parcel in) {
            return new Amanity(in);
        }

        public Amanity[] newArray(int size) {
            return (new Amanity[size]);
        }

    }
    ;
    private final static long serialVersionUID = -15644306334653338L;

    protected Amanity(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Amanity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Amanity withName(String name) {
        this.name = name;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Amanity withImage(String image) {
        this.image = image;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(image);
    }

    public int describeContents() {
        return  0;
    }

}
