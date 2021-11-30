
package com.csestateconnect.www.csconnect.models.home;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brokers implements Serializable, Parcelable
{

    @SerializedName("cities")
    @Expose
    private List<String> cities = null;
    @SerializedName("brokerDetails")
    @Expose
    private List<BrokerDetail> brokerDetails = null;
    public final static Creator<Brokers> CREATOR = new Creator<Brokers>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Brokers createFromParcel(Parcel in) {
            return new Brokers(in);
        }

        public Brokers[] newArray(int size) {
            return (new Brokers[size]);
        }

    }
    ;
    private final static long serialVersionUID = 2239279645511406967L;

    protected Brokers(Parcel in) {
        in.readList(this.cities, (String.class.getClassLoader()));
        in.readList(this.brokerDetails, (BrokerDetail.class.getClassLoader()));
    }

    public Brokers() {
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public Brokers withCities(List<String> cities) {
        this.cities = cities;
        return this;
    }

    public List<BrokerDetail> getBrokerDetails() {
        return brokerDetails;
    }

    public void setBrokerDetails(List<BrokerDetail> brokerDetails) {
        this.brokerDetails = brokerDetails;
    }

    public Brokers withBrokerDetails(List<BrokerDetail> brokerDetails) {
        this.brokerDetails = brokerDetails;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(cities);
        dest.writeList(brokerDetails);
    }

    public int describeContents() {
        return  0;
    }

}
