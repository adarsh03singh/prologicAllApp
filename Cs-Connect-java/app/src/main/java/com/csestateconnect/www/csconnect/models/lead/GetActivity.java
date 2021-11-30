package com.csestateconnect.www.csconnect.models.lead;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetActivity implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("lead")
    @Expose
    private Integer lead;
    @SerializedName("short_description")
    @Expose
    private String shortDescription;
    @SerializedName("date")
    @Expose
    private String date;
    public final static Parcelable.Creator<GetActivity> CREATOR = new Creator<GetActivity>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GetActivity createFromParcel(Parcel in) {
            return new GetActivity(in);
        }

        public GetActivity[] newArray(int size) {
            return (new GetActivity[size]);
        }

    }
            ;
    private final static long serialVersionUID = 2353130193598855379L;

    protected GetActivity(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.lead = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.shortDescription = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
    }

    public GetActivity() {
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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
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
        dest.writeValue(shortDescription);
        dest.writeValue(date);
    }

    public int describeContents() {
        return 0;
    }

}