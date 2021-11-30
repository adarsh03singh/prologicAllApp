
package com.csestateconnect.www.csconnect.models.home;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeAllData implements Serializable, Parcelable
{

    @SerializedName("projects")
    @Expose
    private Projects projects;
    @SerializedName("brokers")
    @Expose
    private Brokers brokers;
    @SerializedName("commission")
    @Expose
    private Commission commission;
    public final static Creator<HomeAllData> CREATOR = new Creator<HomeAllData>() {


        @SuppressWarnings({
            "unchecked"
        })
        public HomeAllData createFromParcel(Parcel in) {
            return new HomeAllData(in);
        }

        public HomeAllData[] newArray(int size) {
            return (new HomeAllData[size]);
        }

    }
    ;
    private final static long serialVersionUID = -1616746649739028377L;

    protected HomeAllData(Parcel in) {
        this.projects = ((Projects) in.readValue((Projects.class.getClassLoader())));
        this.brokers = ((Brokers) in.readValue((Brokers.class.getClassLoader())));
        this.commission = ((Commission) in.readValue((Commission.class.getClassLoader())));
    }

    public HomeAllData() {
    }

    public Projects getProjects() {
        return projects;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }

    public HomeAllData withProjects(Projects projects) {
        this.projects = projects;
        return this;
    }

    public Brokers getBrokers() {
        return brokers;
    }

    public void setBrokers(Brokers brokers) {
        this.brokers = brokers;
    }

    public HomeAllData withBrokers(Brokers brokers) {
        this.brokers = brokers;
        return this;
    }

    public Commission getCommission() {
        return commission;
    }

    public void setCommission(Commission commission) {
        this.commission = commission;
    }

    public HomeAllData withCommission(Commission commission) {
        this.commission = commission;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(projects);
        dest.writeValue(brokers);
        dest.writeValue(commission);
    }

    public int describeContents() {
        return  0;
    }

}
