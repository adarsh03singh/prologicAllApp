package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TowerImage implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("record_type")
    @Expose
    private String recordType;
    @SerializedName("image_type")
    @Expose
    private ImageType imageType;
    public final static Parcelable.Creator<TowerImage> CREATOR = new Creator<TowerImage>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TowerImage createFromParcel(Parcel in) {
            return new TowerImage(in);
        }

        public TowerImage[] newArray(int size) {
            return (new TowerImage[size]);
        }

    }
            ;
    private final static long serialVersionUID = 8724807877305230188L;

    protected TowerImage(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.recordType = ((String) in.readValue((String.class.getClassLoader())));
        this.imageType = ((ImageType) in.readValue((ImageType.class.getClassLoader())));
    }

    public TowerImage() {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(imageUrl);
        dest.writeValue(recordType);
        dest.writeValue(imageType);
    }

    public int describeContents() {
        return 0;
    }

}