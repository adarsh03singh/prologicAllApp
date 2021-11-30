package com.csestateconnect.www.csconnect.models.lead;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetInteractionDate implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("lead")
    @Expose
    private Integer lead;
    @SerializedName("date")
    @Expose
    private String date;
    public final static Parcelable.Creator<GetInteractionDate> CREATOR = new Creator<GetInteractionDate>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GetInteractionDate createFromParcel(Parcel in) {
            return new GetInteractionDate(in);
        }

        public GetInteractionDate[] newArray(int size) {
            return (new GetInteractionDate[size]);
        }

    }
            ;
    private final static long serialVersionUID = 4839515957407623523L;

    protected GetInteractionDate(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.lead = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
    }

    public GetInteractionDate() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLead() {
        return lead;
    }

    public void setLead(Integer lead) {
        this.lead = lead;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(lead);
        dest.writeValue(date);
    }

    public int describeContents() {
        return 0;
    }

}