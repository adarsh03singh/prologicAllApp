package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnitInsideAreaComponent implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("inside_area_category")
    @Expose
    private InsideAreaCategory insideAreaCategory;
    @SerializedName("length")
    @Expose
    private Float length;
    @SerializedName("length_view")
    @Expose
    private String lengthView;
    @SerializedName("width")
    @Expose
    private Float width;
    @SerializedName("width_view")
    @Expose
    private String widthView;
    @SerializedName("area")
    @Expose
    private Float area;
    @SerializedName("area_view")
    @Expose
    private String areaView;
    public final static Parcelable.Creator<UnitInsideAreaComponent> CREATOR = new Creator<UnitInsideAreaComponent>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UnitInsideAreaComponent createFromParcel(Parcel in) {
            return new UnitInsideAreaComponent(in);
        }

        public UnitInsideAreaComponent[] newArray(int size) {
            return (new UnitInsideAreaComponent[size]);
        }

    }
            ;
    private final static long serialVersionUID = -3537613789427200938L;

    protected UnitInsideAreaComponent(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.insideAreaCategory = ((InsideAreaCategory) in.readValue((InsideAreaCategory.class.getClassLoader())));
        this.length = ((Float) in.readValue((Float.class.getClassLoader())));
        this.lengthView = ((String) in.readValue((String.class.getClassLoader())));
        this.width = ((Float) in.readValue((Float.class.getClassLoader())));
        this.widthView = ((String) in.readValue((String.class.getClassLoader())));
        this.area = ((Float) in.readValue((Float.class.getClassLoader())));
        this.areaView = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UnitInsideAreaComponent() {
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

    public InsideAreaCategory getInsideAreaCategory() {
        return insideAreaCategory;
    }

    public void setInsideAreaCategory(InsideAreaCategory insideAreaCategory) {
        this.insideAreaCategory = insideAreaCategory;
    }

    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public String getLengthView() {
        return lengthView;
    }

    public void setLengthView(String lengthView) {
        this.lengthView = lengthView;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public String getWidthView() {
        return widthView;
    }

    public void setWidthView(String widthView) {
        this.widthView = widthView;
    }

    public Float getArea() {
        return area;
    }

    public void setArea(Float area) {
        this.area = area;
    }

    public String getAreaView() {
        return areaView;
    }

    public void setAreaView(String areaView) {
        this.areaView = areaView;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(insideAreaCategory);
        dest.writeValue(length);
        dest.writeValue(lengthView);
        dest.writeValue(width);
        dest.writeValue(widthView);
        dest.writeValue(area);
        dest.writeValue(areaView);
    }

    public int describeContents() {
        return 0;
    }

}