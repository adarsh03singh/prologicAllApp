package com.csestateconnect.www.csconnect.models.lead;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeadStatus implements Serializable, Parcelable
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
    public final static Parcelable.Creator<LeadStatus> CREATOR = new Creator<LeadStatus>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LeadStatus createFromParcel(Parcel in) {
            return new LeadStatus(in);
        }

        public LeadStatus[] newArray(int size) {
            return (new LeadStatus[size]);
        }

    }
            ;
    private final static long serialVersionUID = -3784294269930560209L;

    protected LeadStatus(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.color = ((String) in.readValue((String.class.getClassLoader())));
    }

    public LeadStatus() {
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
        return 0;
    }

}