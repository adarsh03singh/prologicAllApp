package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Project implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("country")
    @Expose
    private Country country;
    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("project_completion_status")
    @Expose
    private ProjectCompletionStatus projectCompletionStatus;
    @SerializedName("project_developer")
    @Expose
    private ProjectDeveloper projectDeveloper;
    @SerializedName("project_specifications_and_others")
    @Expose
    private List<ProjectSpecificationsAndOther> projectSpecificationsAndOthers = null;
    @SerializedName("project_amenities")
    @Expose
    private List<ProjectAmenity> projectAmenities = null;
    @SerializedName("project_wow_factors")
    @Expose
    private List<ProjectWowFactor> projectWowFactors = null;
    @SerializedName("project_roads")
    @Expose
    private List<ProjectRoad> projectRoads = null;
    @SerializedName("project_landmarks")
    @Expose
    private List<ProjectLandmark> projectLandmarks = null;
    @SerializedName("minimum_size")
    @Expose
    private Integer minimumSize;
    @SerializedName("minimum_size_view")
    @Expose
    private String minimumSizeView;
    @SerializedName("maximum_size")
    @Expose
    private Integer maximumSize;
    @SerializedName("maximum_size_view")
    @Expose
    private String maximumSizeView;
    @SerializedName("project_rera_number")
    @Expose
    private String projectReraNumber;
    @SerializedName("project_info")
    @Expose
    private String projectInfo;
    @SerializedName("icon_image")
    @Expose
    private String iconImage;
    @SerializedName("project_launch_year")
    @Expose
    private Integer projectLaunchYear;
    @SerializedName("project_launch_month")
    @Expose
    private Integer projectLaunchMonth;
    @SerializedName("project_class_name")
    @Expose
    private String projectClassName;
    @SerializedName("project_bsp_cost")
    @Expose
    private Integer projectBspCost;
    @SerializedName("project_bsp_cost_view")
    @Expose
    private String projectBspCostView;
    @SerializedName("low_cost")
    @Expose
    private Integer lowCost;
    @SerializedName("low_cost_view")
    @Expose
    private String lowCostView;
    @SerializedName("high_cost")
    @Expose
    private Integer highCost;
    @SerializedName("high_cost_view")
    @Expose
    private String highCostView;
    @SerializedName("tower_count")
    @Expose
    private Integer towerCount;
    @SerializedName("quarter")
    @Expose
    private Integer quarter;
    @SerializedName("possession_year")
    @Expose
    private Integer possessionYear;
    @SerializedName("possession_month")
    @Expose
    private Integer possessionMonth;
    @SerializedName("project_total_area")
    @Expose
    private Float projectTotalArea;
    @SerializedName("project_total_area_view")
    @Expose
    private String projectTotalAreaView;
    @SerializedName("project_open_area")
    @Expose
    private Integer projectOpenArea;
    @SerializedName("project_open_area_percentage")
    @Expose
    private Integer projectOpenAreaPercentage;
    @SerializedName("project_open_area_view")
    @Expose
    private String projectOpenAreaView;
    @SerializedName("project_display_priority")
    @Expose
    private Integer projectDisplayPriority;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("project_towers")
    @Expose
    private List<ProjectTower> projectTowers = null;
    @SerializedName("project_images")
    @Expose
    private List<ProjectImage> projectImages = null;
    @SerializedName("project_brochure")
    @Expose
    private String projectBrochure;
    @SerializedName("documents")
    @Expose
    private List<Document> documents = null;
    public final static Parcelable.Creator<Project> CREATOR = new Creator<Project>() {


//        @SuppressWarnings({
//                "unchecked"
//        })
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        public Project[] newArray(int size) {
            return (new Project[size]);
        }

    }
            ;
    private final static long serialVersionUID = 5320031028575553301L;

    protected Project(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.country = ((Country) in.readValue((Country.class.getClassLoader())));
        this.city = ((City) in.readValue((City.class.getClassLoader())));
        this.location = ((Location) in.readValue((Location.class.getClassLoader())));
        this.projectCompletionStatus = ((ProjectCompletionStatus) in.readValue((ProjectCompletionStatus.class.getClassLoader())));
        this.projectDeveloper = ((ProjectDeveloper) in.readValue((ProjectDeveloper.class.getClassLoader())));
        in.readList(this.projectSpecificationsAndOthers, (ProjectSpecificationsAndOther.class.getClassLoader()));
        in.readList(this.projectAmenities, (ProjectAmenity.class.getClassLoader()));
        in.readList(this.projectWowFactors, (ProjectWowFactor.class.getClassLoader()));
        in.readList(this.projectRoads, (ProjectRoad.class.getClassLoader()));
        in.readList(this.projectLandmarks, (ProjectLandmark.class.getClassLoader()));
        this.minimumSize = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.minimumSizeView = ((String) in.readValue((String.class.getClassLoader())));
        this.maximumSize = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.maximumSizeView = ((String) in.readValue((String.class.getClassLoader())));
        this.projectReraNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.projectInfo = ((String) in.readValue((String.class.getClassLoader())));
        this.iconImage = ((String) in.readValue((String.class.getClassLoader())));
        this.projectLaunchYear = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.projectLaunchMonth = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.projectClassName = ((String) in.readValue((String.class.getClassLoader())));
        this.projectBspCost = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.projectBspCostView = ((String) in.readValue((String.class.getClassLoader())));
        this.lowCost = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.lowCostView = ((String) in.readValue((String.class.getClassLoader())));
        this.highCost = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.highCostView = ((String) in.readValue((String.class.getClassLoader())));
        this.towerCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.quarter = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.possessionYear = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.possessionMonth = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.projectTotalArea = ((Float) in.readValue((Integer.class.getClassLoader())));
        this.projectTotalAreaView = ((String) in.readValue((String.class.getClassLoader())));
        this.projectOpenArea = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.projectOpenAreaPercentage = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.projectOpenAreaView = ((String) in.readValue((String.class.getClassLoader())));
        this.projectDisplayPriority = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.projectTowers, (ProjectTower.class.getClassLoader()));
        in.readList(this.projectImages, (ProjectImage.class.getClassLoader()));
        this.projectBrochure = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.documents, (Document.class.getClassLoader()));
    }

    public Project() {
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ProjectCompletionStatus getProjectCompletionStatus() {
        return projectCompletionStatus;
    }

    public void setProjectCompletionStatus(ProjectCompletionStatus projectCompletionStatus) {
        this.projectCompletionStatus = projectCompletionStatus;
    }

    public ProjectDeveloper getProjectDeveloper() {
        return projectDeveloper;
    }

    public void setProjectDeveloper(ProjectDeveloper projectDeveloper) {
        this.projectDeveloper = projectDeveloper;
    }

    public List<ProjectSpecificationsAndOther> getProjectSpecificationsAndOthers() {
        return projectSpecificationsAndOthers;
    }

    public void setProjectSpecificationsAndOthers(List<ProjectSpecificationsAndOther> projectSpecificationsAndOthers) {
        this.projectSpecificationsAndOthers = projectSpecificationsAndOthers;
    }

    public List<ProjectAmenity> getProjectAmenities() {
        return projectAmenities;
    }

    public void setProjectAmenities(List<ProjectAmenity> projectAmenities) {
        this.projectAmenities = projectAmenities;
    }

    public List<ProjectWowFactor> getProjectWowFactors() {
        return projectWowFactors;
    }

    public void setProjectWowFactors(List<ProjectWowFactor> projectWowFactors) {
        this.projectWowFactors = projectWowFactors;
    }

    public List<ProjectRoad> getProjectRoads() {
        return projectRoads;
    }

    public void setProjectRoads(List<ProjectRoad> projectRoads) {
        this.projectRoads = projectRoads;
    }

    public List<ProjectLandmark> getProjectLandmarks() {
        return projectLandmarks;
    }

    public void setProjectLandmarks(List<ProjectLandmark> projectLandmarks) {
        this.projectLandmarks = projectLandmarks;
    }

    public Integer getMinimumSize() {
        return minimumSize;
    }

    public void setMinimumSize(Integer minimumSize) {
        this.minimumSize = minimumSize;
    }

    public String getMinimumSizeView() {
        return minimumSizeView;
    }

    public void setMinimumSizeView(String minimumSizeView) {
        this.minimumSizeView = minimumSizeView;
    }

    public Integer getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(Integer maximumSize) {
        this.maximumSize = maximumSize;
    }

    public String getMaximumSizeView() {
        return maximumSizeView;
    }

    public void setMaximumSizeView(String maximumSizeView) {
        this.maximumSizeView = maximumSizeView;
    }

    public String getProjectReraNumber() {
        return projectReraNumber;
    }

    public void setProjectReraNumber(String projectReraNumber) {
        this.projectReraNumber = projectReraNumber;
    }

    public String getProjectInfo() {
        return projectInfo;
    }

    public void setProjectInfo(String projectInfo) {
        this.projectInfo = projectInfo;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public Integer getProjectLaunchYear() {
        return projectLaunchYear;
    }

    public void setProjectLaunchYear(Integer projectLaunchYear) {
        this.projectLaunchYear = projectLaunchYear;
    }

    public Integer getProjectLaunchMonth() {
        return projectLaunchMonth;
    }

    public void setProjectLaunchMonth(Integer projectLaunchMonth) {
        this.projectLaunchMonth = projectLaunchMonth;
    }

    public String getProjectClassName() {
        return projectClassName;
    }

    public void setProjectClassName(String projectClassName) {
        this.projectClassName = projectClassName;
    }

    public Integer getProjectBspCost() {
        return projectBspCost;
    }

    public void setProjectBspCost(Integer projectBspCost) {
        this.projectBspCost = projectBspCost;
    }

    public String getProjectBspCostView() {
        return projectBspCostView;
    }

    public void setProjectBspCostView(String projectBspCostView) {
        this.projectBspCostView = projectBspCostView;
    }

    public Integer getLowCost() {
        return lowCost;
    }

    public void setLowCost(Integer lowCost) {
        this.lowCost = lowCost;
    }

    public String getLowCostView() {
        return lowCostView;
    }

    public void setLowCostView(String lowCostView) {
        this.lowCostView = lowCostView;
    }

    public Integer getHighCost() {
        return highCost;
    }

    public void setHighCost(Integer highCost) {
        this.highCost = highCost;
    }

    public String getHighCostView() {
        return highCostView;
    }

    public void setHighCostView(String highCostView) {
        this.highCostView = highCostView;
    }

    public Integer getTowerCount() {
        return towerCount;
    }

    public void setTowerCount(Integer towerCount) {
        this.towerCount = towerCount;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
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

    public Float getProjectTotalArea() {
        return projectTotalArea;
    }

    public void setProjectTotalArea(Float projectTotalArea) {
        this.projectTotalArea = projectTotalArea;
    }

    public String getProjectTotalAreaView() {
        return projectTotalAreaView;
    }

    public void setProjectTotalAreaView(String projectTotalAreaView) {
        this.projectTotalAreaView = projectTotalAreaView;
    }

    public Integer getProjectOpenArea() {
        return projectOpenArea;
    }

    public void setProjectOpenArea(Integer projectOpenArea) {
        this.projectOpenArea = projectOpenArea;
    }

    public Integer getProjectOpenAreaPercentage() {
        return projectOpenAreaPercentage;
    }

    public void setProjectOpenAreaPercentage(Integer projectOpenAreaPercentage) {
        this.projectOpenAreaPercentage = projectOpenAreaPercentage;
    }

    public String getProjectOpenAreaView() {
        return projectOpenAreaView;
    }

    public void setProjectOpenAreaView(String projectOpenAreaView) {
        this.projectOpenAreaView = projectOpenAreaView;
    }

    public Integer getProjectDisplayPriority() {
        return projectDisplayPriority;
    }

    public void setProjectDisplayPriority(Integer projectDisplayPriority) {
        this.projectDisplayPriority = projectDisplayPriority;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ProjectTower> getProjectTowers() {
        return projectTowers;
    }

    public void setProjectTowers(List<ProjectTower> projectTowers) {
        this.projectTowers = projectTowers;
    }

    public List<ProjectImage> getProjectImages() {
        return projectImages;
    }

    public void setProjectImages(List<ProjectImage> projectImages) {
        this.projectImages = projectImages;
    }

    public String getProjectBrochure() {
        return projectBrochure;
    }

    public void setProjectBrochure(String projectBrochure) {
        this.projectBrochure = projectBrochure;
    }

    public String getBhks(){
        Set<String> bhkSet = new HashSet<String>();
        for (ProjectTower tower : this.projectTowers) {
            for (UnitPlan unitPlan : tower.getUnitPlans()) {
                bhkSet.add(unitPlan.getBhkType().getRooms().toString());
            }
        }

        StringBuilder builder = new StringBuilder();
        for (String i : bhkSet) {
            builder.append(i);
            builder.append(" ");
        }

        return builder.toString();
    }
    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(country);
        dest.writeValue(city);
        dest.writeValue(location);
        dest.writeValue(projectCompletionStatus);
        dest.writeValue(projectDeveloper);
        dest.writeList(projectSpecificationsAndOthers);
        dest.writeList(projectAmenities);
        dest.writeList(projectWowFactors);
        dest.writeList(projectRoads);
        dest.writeList(projectLandmarks);
        dest.writeValue(minimumSize);
        dest.writeValue(minimumSizeView);
        dest.writeValue(maximumSize);
        dest.writeValue(maximumSizeView);
        dest.writeValue(projectReraNumber);
        dest.writeValue(projectInfo);
        dest.writeValue(iconImage);
        dest.writeValue(projectLaunchYear);
        dest.writeValue(projectLaunchMonth);
        dest.writeValue(projectClassName);
        dest.writeValue(projectBspCost);
        dest.writeValue(projectBspCostView);
        dest.writeValue(lowCost);
        dest.writeValue(lowCostView);
        dest.writeValue(highCost);
        dest.writeValue(highCostView);
        dest.writeValue(towerCount);
        dest.writeValue(quarter);
        dest.writeValue(possessionYear);
        dest.writeValue(possessionMonth);
        dest.writeValue(projectTotalArea);
        dest.writeValue(projectTotalAreaView);
        dest.writeValue(projectOpenArea);
        dest.writeValue(projectOpenAreaPercentage);
        dest.writeValue(projectOpenAreaView);
        dest.writeValue(projectDisplayPriority);
        dest.writeValue(address);
        dest.writeList(projectTowers);
        dest.writeList(projectImages);
        dest.writeValue(projectBrochure);
        dest.writeList(documents);
    }

    public int describeContents() {
        return 0;
    }

}