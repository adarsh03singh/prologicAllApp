
package com.csestateconnect.www.csconnect.models.deal;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealStatus implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("color")
    @Expose
    private String color;
    public final static Creator<DealStatus> CREATOR = new Creator<DealStatus>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DealStatus createFromParcel(Parcel in) {
            return new DealStatus(in);
        }

        public DealStatus[] newArray(int size) {
            return (new DealStatus[size]);
        }

    }
    ;
    private final static long serialVersionUID = -5953838410601045584L;

    protected DealStatus(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.color = ((String) in.readValue((String.class.getClassLoader())));
    }

    public DealStatus() {
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(color);
    }

    public int describeContents() {
        return  0;
    }

}
