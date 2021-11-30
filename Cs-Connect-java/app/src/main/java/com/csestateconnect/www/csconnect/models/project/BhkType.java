package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BhkType implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("rooms")
    @Expose
    private Integer rooms;
    @SerializedName("icon_image")
    @Expose
    private String iconImage;
    public final static Parcelable.Creator<BhkType> CREATOR = new Creator<BhkType>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BhkType createFromParcel(Parcel in) {
            return new BhkType(in);
        }

        public BhkType[] newArray(int size) {
            return (new BhkType[size]);
        }

    }
            ;
    private final static long serialVersionUID = -3247821801740775889L;

    protected BhkType(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.rooms = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.iconImage = ((String) in.readValue((String.class.getClassLoader())));
    }

    public BhkType() {
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

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(rooms);
        dest.writeValue(iconImage);
    }

    public int describeContents() {
        return 0;
    }

}
