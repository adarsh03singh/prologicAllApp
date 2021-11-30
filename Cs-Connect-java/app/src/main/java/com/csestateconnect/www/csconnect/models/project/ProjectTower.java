package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectTower implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("floors")
    @Expose
    private Integer floors;
    @SerializedName("total_no_of_units")
    @Expose
    private Integer totalNoOfUnits;
    @SerializedName("tower_completion_status")
    @Expose
    private TowerCompletionStatus towerCompletionStatus;
    @SerializedName("tower_images")
    @Expose
    private List<TowerImage> towerImages = null;
    @SerializedName("tower_phase")
    @Expose
    private TowerPhase towerPhase;
    @SerializedName("unit_plans")
    @Expose
    private List<UnitPlan> unitPlans = null;
    public final static Parcelable.Creator<ProjectTower> CREATOR = new Creator<ProjectTower>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ProjectTower createFromParcel(Parcel in) {
            return new ProjectTower(in);
        }

        public ProjectTower[] newArray(int size) {
            return (new ProjectTower[size]);
        }

    }
            ;
    private final static long serialVersionUID = 1445315560813542404L;

    protected ProjectTower(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.floors = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.totalNoOfUnits = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.towerCompletionStatus = ((TowerCompletionStatus) in.readValue((TowerCompletionStatus.class.getClassLoader())));
        in.readList(this.towerImages, (TowerImage.class.getClassLoader()));
        this.towerPhase = ((TowerPhase) in.readValue((TowerPhase.class.getClassLoader())));
        in.readList(this.unitPlans, (UnitPlan.class.getClassLoader()));
    }

    public ProjectTower() {
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

    public Integer getFloors() {
        return floors;
    }

    public void setFloors(Integer floors) {
        this.floors = floors;
    }

    public Integer getTotalNoOfUnits() {
        return totalNoOfUnits;
    }

    public void setTotalNoOfUnits(Integer totalNoOfUnits) {
        this.totalNoOfUnits = totalNoOfUnits;
    }

    public TowerCompletionStatus getTowerCompletionStatus() {
        return towerCompletionStatus;
    }

    public void setTowerCompletionStatus(TowerCompletionStatus towerCompletionStatus) {
        this.towerCompletionStatus = towerCompletionStatus;
    }

    public List<TowerImage> getTowerImages() {
        return towerImages;
    }

    public void setTowerImages(List<TowerImage> towerImages) {
        this.towerImages = towerImages;
    }

    public TowerPhase getTowerPhase() {
        return towerPhase;
    }

    public void setTowerPhase(TowerPhase towerPhase) {
        this.towerPhase = towerPhase;
    }

    public List<UnitPlan> getUnitPlans() {
        return unitPlans;
    }

    public void setUnitPlans(List<UnitPlan> unitPlans) {
        this.unitPlans = unitPlans;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(floors);
        dest.writeValue(totalNoOfUnits);
        dest.writeValue(towerCompletionStatus);
        dest.writeList(towerImages);
        dest.writeValue(towerPhase);
        dest.writeList(unitPlans);
    }

    public int describeContents() {
        return 0;
    }

}