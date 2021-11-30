package com.csestateconnect.www.csconnect.models.lead;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DealId implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;

    public final static Creator<DealId> CREATOR = new Creator<DealId>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DealId createFromParcel(Parcel in) {
            return new DealId(in);
        }

        public DealId[] newArray(int size) {
            return (new DealId[size]);
        }

    }
            ;
    private final static long serialVersionUID = 7091535632588176579L;

    protected DealId(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public DealId() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
    }

    public int describeContents() {
        return 0;
    }

}