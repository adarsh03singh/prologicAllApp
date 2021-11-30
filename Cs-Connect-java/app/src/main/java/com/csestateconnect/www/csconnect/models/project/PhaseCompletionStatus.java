package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhaseCompletionStatus implements Serializable, Parcelable
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
    public final static Parcelable.Creator<PhaseCompletionStatus> CREATOR = new Creator<PhaseCompletionStatus>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PhaseCompletionStatus createFromParcel(Parcel in) {
            return new PhaseCompletionStatus(in);
        }

        public PhaseCompletionStatus[] newArray(int size) {
            return (new PhaseCompletionStatus[size]);
        }

    }
            ;
    private final static long serialVersionUID = -2993092485666169265L;

    protected PhaseCompletionStatus(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.completionPercentage = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public PhaseCompletionStatus() {
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