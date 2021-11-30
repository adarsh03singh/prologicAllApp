package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectCompletionStatus implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("completion_percentage")
    @Expose
    private Integer completionPercentage;
    public final static Parcelable.Creator<ProjectCompletionStatus> CREATOR = new Creator<ProjectCompletionStatus>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ProjectCompletionStatus createFromParcel(Parcel in) {
            return new ProjectCompletionStatus(in);
        }

        public ProjectCompletionStatus[] newArray(int size) {
            return (new ProjectCompletionStatus[size]);
        }

    }
            ;
    private final static long serialVersionUID = 6561905689003807200L;

    protected ProjectCompletionStatus(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.completionPercentage = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public ProjectCompletionStatus() {
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

    public Integer getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(Integer completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(completionPercentage);
    }

    public int describeContents() {
        return 0;
    }

}