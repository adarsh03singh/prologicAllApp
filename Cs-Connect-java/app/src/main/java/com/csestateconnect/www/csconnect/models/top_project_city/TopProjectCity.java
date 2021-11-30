package com.csestateconnect.www.csconnect.models.top_project_city;

import android.os.Parcel;
import android.os.Parcelable;

import com.csestateconnect.www.csconnect.models.home.ProjectDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TopProjectCity implements Serializable, Parcelable
{
    @SerializedName("cities")
    @Expose
    private String cities;
    @SerializedName("projectDetails")
    @Expose
    private ProjectDetail projectsDetails;
    public final static Creator<TopProjectCity> CREATOR = new Creator<TopProjectCity>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TopProjectCity createFromParcel(Parcel in) {
            return new TopProjectCity(in);
        }

        public TopProjectCity[] newArray(int size) {
            return (new TopProjectCity[size]);
        }

    }
            ;
    private final static long serialVersionUID = 5669871426257961858L;

    protected TopProjectCity(Parcel in) {
        this.cities = ((String) in.readValue((String.class.getClassLoader())));
        this.projectsDetails = ((ProjectDetail) in.readValue((ProjectDetail.class.getClassLoader())));
    }

    public TopProjectCity() {
    }

    public String getCities() {
        return cities;
    }

    public void setCities(String cities) {
        this.cities = cities;
    }

    public ProjectDetail getProjectsDetails() {
        return projectsDetails;
    }

    public void setProjectsDetails(ProjectDetail projectsDetails) {
        this.projectsDetails = projectsDetails;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(cities);
        dest.writeValue(projectsDetails);
    }

    public int describeContents() {
        return 0;
    }

}