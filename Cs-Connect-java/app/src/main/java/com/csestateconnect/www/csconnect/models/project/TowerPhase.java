package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TowerPhase implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("phase_images")
    @Expose
    private List<PhaseImage> phaseImages = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phase_completion_status")
    @Expose
    private PhaseCompletionStatus phaseCompletionStatus;
    @SerializedName("phase_launch_year")
    @Expose
    private Integer phaseLaunchYear;
    @SerializedName("phase_launch_month")
    @Expose
    private Integer phaseLaunchMonth;
    @SerializedName("possession_year")
    @Expose
    private Integer possessionYear;
    @SerializedName("possession_month")
    @Expose
    private Integer possessionMonth;
    @SerializedName("phase_rera_number")
    @Expose
    private String phaseReraNumber;
    @SerializedName("no_of_total_units")
    @Expose
    private Integer noOfTotalUnits;
    @SerializedName("no_of_booked_units")
    @Expose
    private Integer noOfBookedUnits;
    @SerializedName("percentage_of_booked_units")
    @Expose
    private Float percentageOfBookedUnits;
    @SerializedName("floors")
    @Expose
    private Integer floors;
    public final static Parcelable.Creator<TowerPhase> CREATOR = new Creator<TowerPhase>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TowerPhase createFromParcel(Parcel in) {
            return new TowerPhase(in);
        }

        public TowerPhase[] newArray(int size) {
            return (new TowerPhase[size]);
        }

    }
            ;
    private final static long serialVersionUID = 722290544274757503L;

    protected TowerPhase(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.phaseImages, (PhaseImage.class.getClassLoader()));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.phaseCompletionStatus = ((PhaseCompletionStatus) in.readValue((PhaseCompletionStatus.class.getClassLoader())));
        this.phaseLaunchYear = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.phaseLaunchMonth = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.possessionYear = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.possessionMonth = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.phaseReraNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.noOfTotalUnits = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.noOfBookedUnits = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.percentageOfBookedUnits = ((Float) in.readValue((Float.class.getClassLoader())));
        this.floors = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public TowerPhase() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<PhaseImage> getPhaseImages() {
        return phaseImages;
    }

    public void setPhaseImages(List<PhaseImage> phaseImages) {
        this.phaseImages = phaseImages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PhaseCompletionStatus getPhaseCompletionStatus() {
        return phaseCompletionStatus;
    }

    public void setPhaseCompletionStatus(PhaseCompletionStatus phaseCompletionStatus) {
        this.phaseCompletionStatus = phaseCompletionStatus;
    }

    public Integer getPhaseLaunchYear() {
        return phaseLaunchYear;
    }

    public void setPhaseLaunchYear(Integer phaseLaunchYear) {
        this.phaseLaunchYear = phaseLaunchYear;
    }

    public Integer getPhaseLaunchMonth() {
        return phaseLaunchMonth;
    }

    public void setPhaseLaunchMonth(Integer phaseLaunchMonth) {
        this.phaseLaunchMonth = phaseLaunchMonth;
    }

    public Integer getPossessionYear() {
        return possessionYear;
    }

    public void setPossessionYear(Integer possessionYear) {
        this.possessionYear = possessionYear;
    }

    public Integer getPossessionMonth() {
        return possessionMonth;
    }

    public void setPossessionMonth(Integer possessionMonth) {
        this.possessionMonth = possessionMonth;
    }

    public String getPhaseReraNumber() {
        return phaseReraNumber;
    }

    public void setPhaseReraNumber(String phaseReraNumber) {
        this.phaseReraNumber = phaseReraNumber;
    }

    public Integer getNoOfTotalUnits() {
        return noOfTotalUnits;
    }

    public void setNoOfTotalUnits(Integer noOfTotalUnits) {
        this.noOfTotalUnits = noOfTotalUnits;
    }

    public Integer getNoOfBookedUnits() {
        return noOfBookedUnits;
    }

    public void setNoOfBookedUnits(Integer noOfBookedUnits) {
        this.noOfBookedUnits = noOfBookedUnits;
    }

    public Float getPercentageOfBookedUnits() {
        return percentageOfBookedUnits;
    }

    public void setPercentageOfBookedUnits(Float percentageOfBookedUnits) {
        this.percentageOfBookedUnits = percentageOfBookedUnits;
    }

    public Integer getFloors() {
        return floors;
    }

    public void setFloors(Integer floors) {
        this.floors = floors;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(phaseImages);
        dest.writeValue(name);
        dest.writeValue(phaseCompletionStatus);
        dest.writeValue(phaseLaunchYear);
        dest.writeValue(phaseLaunchMonth);
        dest.writeValue(possessionYear);
        dest.writeValue(possessionMonth);
        dest.writeValue(phaseReraNumber);
        dest.writeValue(noOfTotalUnits);
        dest.writeValue(noOfBookedUnits);
        dest.writeValue(percentageOfBookedUnits);
        dest.writeValue(floors);
    }

    public int describeContents() {
        return 0;
    }

}