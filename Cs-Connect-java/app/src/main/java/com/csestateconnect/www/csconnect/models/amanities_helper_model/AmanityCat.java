
package com.csestateconnect.www.csconnect.models.amanities_helper_model;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AmanityCat implements Serializable, Parcelable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("amanities")
    @Expose
    private List<Amanity> amanities = null;
    public final static Parcelable.Creator<AmanityCat> CREATOR = new Creator<AmanityCat>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AmanityCat createFromParcel(Parcel in) {
            return new AmanityCat(in);
        }

        public AmanityCat[] newArray(int size) {
            return (new AmanityCat[size]);
        }

    }
    ;
    private final static long serialVersionUID = -8556218741940185952L;

    protected AmanityCat(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.amanities, (com.csestateconnect.www.csconnect.models.amanities_helper_model.Amanity.class.getClassLoader()));
    }

    public AmanityCat() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AmanityCat withName(String name) {
        this.name = name;
        return this;
    }

    public List<Amanity> getAmanities() {
        return amanities;
    }

    public void setAmanities(List<Amanity> amanities) {
        this.amanities = amanities;
    }

    public AmanityCat withAmanities(List<Amanity> amanities) {
        this.amanities = amanities;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeList(amanities);
    }

    public int describeContents() {
        return  0;
    }

}
