package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnitPlan implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("bhk_type")
    @Expose
    private BhkType bhkType;
    @SerializedName("property_type")
    @Expose
    private PropertyType propertyType;
    @SerializedName("unit_plan_images")
    @Expose
    private List<UnitPlanImage> unitPlanImages = null;
    @SerializedName("unit_inside_area_components")
    @Expose
    private List<UnitInsideAreaComponent> unitInsideAreaComponents = null;
    @SerializedName("unit_plan_component_prices")
    @Expose
    private List<UnitPlanComponentPrice> unitPlanComponentPrices = null;
    @SerializedName("icon_image")
    @Expose
    private String iconImage;
    @SerializedName("carpet_area")
    @Expose
    private Float carpetArea;
    @SerializedName("carpet_area_percentage")
    @Expose
    private Float carpetAreaPercentage;
    @SerializedName("carpet_area_view")
    @Expose
    private String carpetAreaView;
    @SerializedName("built_up_area")
    @Expose
    private Float builtUpArea;
    @SerializedName("built_up_area_percentage")
    @Expose
    private Float builtUpAreaPercentage;
    @SerializedName("built_up_area_view")
    @Expose
    private String builtUpAreaView;
    @SerializedName("super_area")
    @Expose
    private Float superArea;
    @SerializedName("super_area_percentage")
    @Expose
    private Float superAreaPercentage;
    @SerializedName("super_area_view")
    @Expose
    private String superAreaView;
    @SerializedName("low_cost")
    @Expose
    private Float lowCost;
    @SerializedName("low_cost_view")
    @Expose
    private String lowCostView;
    @SerializedName("high_cost")
    @Expose
    private Float highCost;
    @SerializedName("high_cost_view")
    @Expose
    private String highCostView;
    @SerializedName("avg_cost")
    @Expose
    private Float avgCost;
    @SerializedName("avg_cost_view")
    @Expose
    private String avgCostView;
    @SerializedName("total_low_cost")
    @Expose
    private Float totalLowCost;
    @SerializedName("total_low_cost_view")
    @Expose
    private String totalLowCostView;
    @SerializedName("total_high_cost")
    @Expose
    private Float totalHighCost;
    @SerializedName("total_high_cost_view")
    @Expose
    private String totalHighCostView;
    @SerializedName("hybrid_high_cost")
    @Expose
    private Float hybridHighCost;
    @SerializedName("hybrid_high_cost_view")
    @Expose
    private String hybridHighCostView;
    @SerializedName("unit_plan_description")
    @Expose
    private String unitPlanDescription;
    @SerializedName("floor_type")
    @Expose
    private String floorType;
    public final static Parcelable.Creator<UnitPlan> CREATOR = new Creator<UnitPlan>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UnitPlan createFromParcel(Parcel in) {
            return new UnitPlan(in);
        }

        public UnitPlan[] newArray(int size) {
            return (new UnitPlan[size]);
        }

    }
            ;
    private final static long serialVersionUID = -3464839765771038410L;

    protected UnitPlan(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.bhkType = ((BhkType) in.readValue((BhkType.class.getClassLoader())));
        this.propertyType = ((PropertyType) in.readValue((PropertyType.class.getClassLoader())));
        in.readList(this.unitPlanImages, (UnitPlanImage.class.getClassLoader()));
        in.readList(this.unitInsideAreaComponents, (UnitInsideAreaComponent.class.getClassLoader()));
        in.readList(this.unitPlanComponentPrices, (UnitPlanComponentPrice.class.getClassLoader()));
        this.iconImage = ((String) in.readValue((String.class.getClassLoader())));
        this.carpetArea = ((Float) in.readValue((Float.class.getClassLoader())));
        this.carpetAreaPercentage = ((Float) in.readValue((Float.class.getClassLoader())));
        this.carpetAreaView = ((String) in.readValue((String.class.getClassLoader())));
        this.builtUpArea = ((Float) in.readValue((Float.class.getClassLoader())));
        this.builtUpAreaPercentage = ((Float) in.readValue((Float.class.getClassLoader())));
        this.builtUpAreaView = ((String) in.readValue((String.class.getClassLoader())));
        this.superArea = ((Float) in.readValue((Float.class.getClassLoader())));
        this.superAreaPercentage = ((Float) in.readValue((Float.class.getClassLoader())));
        this.superAreaView = ((String) in.readValue((String.class.getClassLoader())));
        this.lowCost = ((Float) in.readValue((Float.class.getClassLoader())));
        this.lowCostView = ((String) in.readValue((String.class.getClassLoader())));
        this.highCost = ((Float) in.readValue((Float.class.getClassLoader())));
        this.highCostView = ((String) in.readValue((String.class.getClassLoader())));
        this.avgCost = ((Float) in.readValue((Float.class.getClassLoader())));
        this.avgCostView = ((String) in.readValue((String.class.getClassLoader())));
        this.totalLowCost = ((Float) in.readValue((Float.class.getClassLoader())));
        this.totalLowCostView = ((String) in.readValue((String.class.getClassLoader())));
        this.totalHighCost = ((Float) in.readValue((Float.class.getClassLoader())));
        this.totalHighCostView = ((String) in.readValue((String.class.getClassLoader())));
        this.hybridHighCost = ((Float) in.readValue((Float.class.getClassLoader())));
        this.hybridHighCostView = ((String) in.readValue((String.class.getClassLoader())));
        this.unitPlanDescription = ((String) in.readValue((String.class.getClassLoader())));
        this.floorType = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UnitPlan() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BhkType getBhkType() {
        return bhkType;
    }

    public void setBhkType(BhkType bhkType) {
        this.bhkType = bhkType;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public List<UnitPlanImage> getUnitPlanImages() {
        return unitPlanImages;
    }

    public void setUnitPlanImages(List<UnitPlanImage> unitPlanImages) {
        this.unitPlanImages = unitPlanImages;
    }

    public List<UnitInsideAreaComponent> getUnitInsideAreaComponents() {
        return unitInsideAreaComponents;
    }

    public void setUnitInsideAreaComponents(List<UnitInsideAreaComponent> unitInsideAreaComponents) {
        this.unitInsideAreaComponents = unitInsideAreaComponents;
    }

    public List<UnitPlanComponentPrice> getUnitPlanComponentPrices() {
        return unitPlanComponentPrices;
    }

    public void setUnitPlanComponentPrices(List<UnitPlanComponentPrice> unitPlanComponentPrices) {
        this.unitPlanComponentPrices = unitPlanComponentPrices;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public Float getCarpetArea() {
        return carpetArea;
    }

    public void setCarpetArea(Float carpetArea) {
        this.carpetArea = carpetArea;
    }

    public Float getCarpetAreaPercentage() {
        return carpetAreaPercentage;
    }

    public void setCarpetAreaPercentage(Float carpetAreaPercentage) {
        this.carpetAreaPercentage = carpetAreaPercentage;
    }

    public String getCarpetAreaView() {
        return carpetAreaView;
    }

    public void setCarpetAreaView(String carpetAreaView) {
        this.carpetAreaView = carpetAreaView;
    }

    public Float getBuiltUpArea() {
        return builtUpArea;
    }

    public void setBuiltUpArea(Float builtUpArea) {
        this.builtUpArea = builtUpArea;
    }

    public Float getBuiltUpAreaPercentage() {
        return builtUpAreaPercentage;
    }

    public void setBuiltUpAreaPercentage(Float builtUpAreaPercentage) {
        this.builtUpAreaPercentage = builtUpAreaPercentage;
    }

    public String getBuiltUpAreaView() {
        return builtUpAreaView;
    }

    public void setBuiltUpAreaView(String builtUpAreaView) {
        this.builtUpAreaView = builtUpAreaView;
    }

    public Float getSuperArea() {
        return superArea;
    }

    public void setSuperArea(Float superArea) {
        this.superArea = superArea;
    }

    public Float getSuperAreaPercentage() {
        return superAreaPercentage;
    }

    public void setSuperAreaPercentage(Float superAreaPercentage) {
        this.superAreaPercentage = superAreaPercentage;
    }

    public String getSuperAreaView() {
        return superAreaView;
    }

    public void setSuperAreaView(String superAreaView) {
        this.superAreaView = superAreaView;
    }

    public Float getLowCost() {
        return lowCost;
    }

    public void setLowCost(Float lowCost) {
        this.lowCost = lowCost;
    }

    public String getLowCostView() {
        return lowCostView;
    }

    public void setLowCostView(String lowCostView) {
        this.lowCostView = lowCostView;
    }

    public Float getHighCost() {
        return highCost;
    }

    public void setHighCost(Float highCost) {
        this.highCost = highCost;
    }

    public String getHighCostView() {
        return highCostView;
    }

    public void setHighCostView(String highCostView) {
        this.highCostView = highCostView;
    }

    public Float getAvgCost() {
        return avgCost;
    }

    public void setAvgCost(Float avgCost) {
        this.avgCost = avgCost;
    }

    public String getAvgCostView() {
        return avgCostView;
    }

    public void setAvgCostView(String avgCostView) {
        this.avgCostView = avgCostView;
    }

    public Float getTotalLowCost() {
        return totalLowCost;
    }

    public void setTotalLowCost(Float totalLowCost) {
        this.totalLowCost = totalLowCost;
    }

    public String getTotalLowCostView() {
        return totalLowCostView;
    }

    public void setTotalLowCostView(String totalLowCostView) {
        this.totalLowCostView = totalLowCostView;
    }

    public Float getTotalHighCost() {
        return totalHighCost;
    }

    public void setTotalHighCost(Float totalHighCost) {
        this.totalHighCost = totalHighCost;
    }

    public String getTotalHighCostView() {
        return totalHighCostView;
    }

    public void setTotalHighCostView(String totalHighCostView) {
        this.totalHighCostView = totalHighCostView;
    }

    public Float getHybridHighCost() {
        return hybridHighCost;
    }

    public void setHybridHighCost(Float hybridHighCost) {
        this.hybridHighCost = hybridHighCost;
    }

    public String getHybridHighCostView() {
        return hybridHighCostView;
    }

    public void setHybridHighCostView(String hybridHighCostView) {
        this.hybridHighCostView = hybridHighCostView;
    }

    public String getUnitPlanDescription() {
        return unitPlanDescription;
    }

    public void setUnitPlanDescription(String unitPlanDescription) {
        this.unitPlanDescription = unitPlanDescription;
    }

    public String getFloorType() {
        return floorType;
    }

    public void setFloorType(String floorType) {
        this.floorType = floorType;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(bhkType);
        dest.writeValue(propertyType);
        dest.writeList(unitPlanImages);
        dest.writeList(unitInsideAreaComponents);
        dest.writeList(unitPlanComponentPrices);
        dest.writeValue(iconImage);
        dest.writeValue(carpetArea);
        dest.writeValue(carpetAreaPercentage);
        dest.writeValue(carpetAreaView);
        dest.writeValue(builtUpArea);
        dest.writeValue(builtUpAreaPercentage);
        dest.writeValue(builtUpAreaView);
        dest.writeValue(superArea);
        dest.writeValue(superAreaPercentage);
        dest.writeValue(superAreaView);
        dest.writeValue(lowCost);
        dest.writeValue(lowCostView);
        dest.writeValue(highCost);
        dest.writeValue(highCostView);
        dest.writeValue(avgCost);
        dest.writeValue(avgCostView);
        dest.writeValue(totalLowCost);
        dest.writeValue(totalLowCostView);
        dest.writeValue(totalHighCost);
        dest.writeValue(totalHighCostView);
        dest.writeValue(hybridHighCost);
        dest.writeValue(hybridHighCostView);
        dest.writeValue(unitPlanDescription);
        dest.writeValue(floorType);
    }

    public int describeContents() {
        return 0;
    }

}