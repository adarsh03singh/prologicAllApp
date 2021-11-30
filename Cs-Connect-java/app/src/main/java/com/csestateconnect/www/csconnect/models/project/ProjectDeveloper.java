package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectDeveloper implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("head_office_location")
    @Expose
    private HeadOfficeLocation headOfficeLocation;
    @SerializedName("head_office_city")
    @Expose
    private HeadOfficeCity headOfficeCity;
    @SerializedName("head_office_country")
    @Expose
    private HeadOfficeCountry headOfficeCountry;
    @SerializedName("rera_no")
    @Expose
    private String reraNo;
    @SerializedName("icon_image")
    @Expose
    private String iconImage;
    @SerializedName("developer_info")
    @Expose
    private String developerInfo;
    @SerializedName("head_office_address")
    @Expose
    private String headOfficeAddress;
    @SerializedName("contact_no")
    @Expose
    private String contactNo;
    @SerializedName("management")
    @Expose
    private String management;
    @SerializedName("no_of_projects")
    @Expose
    private String noOfProjects;
    public final static Parcelable.Creator<ProjectDeveloper> CREATOR = new Creator<ProjectDeveloper>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ProjectDeveloper createFromParcel(Parcel in) {
            return new ProjectDeveloper(in);
        }

        public ProjectDeveloper[] newArray(int size) {
            return (new ProjectDeveloper[size]);
        }

    }
            ;
    private final static long serialVersionUID = 1772453615719405668L;

    protected ProjectDeveloper(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.headOfficeLocation = ((HeadOfficeLocation) in.readValue((HeadOfficeLocation.class.getClassLoader())));
        this.headOfficeCity = ((HeadOfficeCity) in.readValue((HeadOfficeCity.class.getClassLoader())));
        this.headOfficeCountry = ((HeadOfficeCountry) in.readValue((HeadOfficeCountry.class.getClassLoader())));
        this.reraNo = ((String) in.readValue((String.class.getClassLoader())));
        this.iconImage = ((String) in.readValue((String.class.getClassLoader())));
        this.developerInfo = ((String) in.readValue((String.class.getClassLoader())));
        this.headOfficeAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.contactNo = ((String) in.readValue((String.class.getClassLoader())));
        this.management = ((String) in.readValue((String.class.getClassLoader())));
        this.noOfProjects = ((String) in.readValue((Integer.class.getClassLoader())));
    }

    public ProjectDeveloper() {
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

    public HeadOfficeLocation getHeadOfficeLocation() {
        return headOfficeLocation;
    }

    public void setHeadOfficeLocation(HeadOfficeLocation headOfficeLocation) {
        this.headOfficeLocation = headOfficeLocation;
    }

    public HeadOfficeCity getHeadOfficeCity() {
        return headOfficeCity;
    }

    public void setHeadOfficeCity(HeadOfficeCity headOfficeCity) {
        this.headOfficeCity = headOfficeCity;
    }

    public HeadOfficeCountry getHeadOfficeCountry() {
        return headOfficeCountry;
    }

    public void setHeadOfficeCountry(HeadOfficeCountry headOfficeCountry) {
        this.headOfficeCountry = headOfficeCountry;
    }

    public String getReraNo() {
        return reraNo;
    }

    public void setReraNo(String reraNo) {
        this.reraNo = reraNo;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public String getDeveloperInfo() {
        return developerInfo;
    }

    public void setDeveloperInfo(String developerInfo) {
        this.developerInfo = developerInfo;
    }

    public String getHeadOfficeAddress() {
        return headOfficeAddress;
    }

    public void setHeadOfficeAddress(String headOfficeAddress) {
        this.headOfficeAddress = headOfficeAddress;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getManagement() {
        return management;
    }

    public void setManagement(String management) {
        this.management = management;
    }

    public String getNoOfProjects() {
        return noOfProjects;
    }

    public void setNoOfProjects(String noOfProjects) {
        this.noOfProjects = noOfProjects;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(headOfficeLocation);
        dest.writeValue(headOfficeCity);
        dest.writeValue(headOfficeCountry);
        dest.writeValue(reraNo);
        dest.writeValue(iconImage);
        dest.writeValue(developerInfo);
        dest.writeValue(headOfficeAddress);
        dest.writeValue(contactNo);
        dest.writeValue(management);
        dest.writeValue(noOfProjects);
    }

    public int describeContents() {
        return 0;
    }

}