package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsideAreaCategory implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    public final static Parcelable.Creator<InsideAreaCategory> CREATOR = new Creator<InsideAreaCategory>() {


        @SuppressWarnings({
                "unchecked"
        })
        public InsideAreaCategory createFromParcel(Parcel in) {
            return new InsideAreaCategory(in);
        }

        public InsideAreaCategory[] newArray(int size) {
            return (new InsideAreaCategory[size]);
        }

    }
            ;
    private final static long serialVersionUID = 654194284257577148L;

    protected InsideAreaCategory(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public InsideAreaCategory() {
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
    }

    public int describeContents() {
        return 0;
    }

}