
package com.csestateconnect.www.csconnect.models.home;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrokerDetail implements Serializable, Parcelable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("commission")
    @Expose
    private Integer commission;
    @SerializedName("id")
    @Expose
    private Integer id;
    public final static Creator<BrokerDetail> CREATOR = new Creator<BrokerDetail>() {


        @SuppressWarnings({
            "unchecked"
        })
        public BrokerDetail createFromParcel(Parcel in) {
            return new BrokerDetail(in);
        }

        public BrokerDetail[] newArray(int size) {
            return (new BrokerDetail[size]);
        }

    }
    ;
    private final static long serialVersionUID = 6726375501269668337L;

    protected BrokerDetail(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.city = ((String) in.readValue((String.class.getClassLoader())));
        this.commission = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public BrokerDetail() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BrokerDetail withName(String name) {
        this.name = name;
        return this;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public BrokerDetail withCity(String city) {
        this.city = city;
        return this;
    }

    public Integer getCommission() {
        return commission;
    }

    public void setCommission(Integer commission) {
        this.commission = commission;
    }

    public BrokerDetail withCommission(Integer commission) {
        this.commission = commission;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BrokerDetail withId(Integer id) {
        this.id = id;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(city);
        dest.writeValue(commission);
        dest.writeValue(id);
    }

    public int describeContents() {
        return  0;
    }

}
