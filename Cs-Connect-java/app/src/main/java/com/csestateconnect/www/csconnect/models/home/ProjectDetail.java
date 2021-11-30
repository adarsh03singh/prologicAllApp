
package com.csestateconnect.www.csconnect.models.home;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectDetail implements Serializable, Parcelable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("deals")
    @Expose
    private Integer deals;
    public final static Creator<ProjectDetail> CREATOR = new Creator<ProjectDetail>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ProjectDetail createFromParcel(Parcel in) {
            return new ProjectDetail(in);
        }

        public ProjectDetail[] newArray(int size) {
            return (new ProjectDetail[size]);
        }

    }
    ;
    private final static long serialVersionUID = 8658229323943095855L;

    protected ProjectDetail(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.city = ((String) in.readValue((String.class.getClassLoader())));
        this.deals = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public ProjectDetail() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectDetail withName(String name) {
        this.name = name;
        return this;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ProjectDetail withCity(String city) {
        this.city = city;
        return this;
    }

    public Integer getDeals() {
        return deals;
    }

    public void setDeals(Integer deals) {
        this.deals = deals;
    }

    public ProjectDetail withDeals(Integer deals) {
        this.deals = deals;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(city);
        dest.writeValue(deals);
    }

    public int describeContents() {
        return  0;
    }

}
