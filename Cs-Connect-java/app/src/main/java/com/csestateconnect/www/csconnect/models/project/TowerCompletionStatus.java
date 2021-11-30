package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TowerCompletionStatus implements Serializable, Parcelable
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
    public final static Parcelable.Creator<TowerCompletionStatus> CREATOR = new Creator<TowerCompletionStatus>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TowerCompletionStatus createFromParcel(Parcel in) {
            return new TowerCompletionStatus(in);
        }

        public TowerCompletionStatus[] newArray(int size) {
            return (new TowerCompletionStatus[size]);
        }

    }
            ;
    private final static long serialVersionUID = -4126395053833986217L;

    protected TowerCompletionStatus(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.completionPercentage = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public TowerCompletionStatus() {
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