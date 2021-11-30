
package com.csestateconnect.www.csconnect.models.home;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Projects implements Serializable, Parcelable
{

    @SerializedName("cities")
    @Expose
    private List<String> cities = null;
    @SerializedName("projectDetails")
    @Expose
    private List<ProjectDetail> projectDetails = null;
    public final static Creator<Projects> CREATOR = new Creator<Projects>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Projects createFromParcel(Parcel in) {
            return new Projects(in);
        }

        public Projects[] newArray(int size) {
            return (new Projects[size]);
        }

    }
    ;
    private final static long serialVersionUID = 3272173513525645741L;

    protected Projects(Parcel in) {
        in.readList(this.cities, (String.class.getClassLoader()));
        in.readList(this.projectDetails, (ProjectDetail.class.getClassLoader()));
    }

    public Projects() {
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public Projects withCities(List<String> cities) {
        this.cities = cities;
        return this;
    }

    public List<ProjectDetail> getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(List<ProjectDetail> projectDetails) {
        this.projectDetails = projectDetails;
    }

    public Projects withProjectDetails(List<ProjectDetail> projectDetails) {
        this.projectDetails = projectDetails;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(cities);
        dest.writeList(projectDetails);
    }

    public int describeContents() {
        return  0;
    }

}
