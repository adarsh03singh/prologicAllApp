package com.csestateconnect.www.csconnect.models.lead;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lead implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("budget")
    @Expose
    public String budget;
    @SerializedName("phone_number")
    @Expose
    public String phoneNumber;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("country")
    @Expose
    public Country country;
    @SerializedName("city")
    @Expose
    public City city;
    @SerializedName("location")
    @Expose
    public Location location;
    @SerializedName("quality")
    @Expose
    public Integer quality;
    @SerializedName("lead_remarks")
    @Expose
    public String leadRemarks;
    @SerializedName("lead_status")
    @Expose
    public LeadStatus leadStatus;
    @SerializedName("feedback_comment")
    @Expose
    public String feedbackComment;
    @SerializedName("used")
    @Expose
    public Boolean used;
    @SerializedName("feedback_submitted")
    @Expose
    public Boolean feedbackSubmitted;
    @SerializedName("assigned")
    @Expose
    public Boolean assigned;
    @SerializedName("self_generated")
    @Expose
    public Boolean selfGenerated;
    @SerializedName("submitted")
    @Expose
    public Boolean submitted;
    @SerializedName("managed_by_rm")
    @Expose
    public ManagedByRm managedByRm;
    @SerializedName("get_activities")
    @Expose
    public List<GetActivity> getActivities = null;
    @SerializedName("get_interaction_dates")
    @Expose
    public List<GetInteractionDate> getInteractionDates = null;
    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("get_deal")
    @Expose
    public DealId dealId;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    public final static Parcelable.Creator<Lead> CREATOR = new Creator<Lead>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Lead createFromParcel(Parcel in) {
            return new Lead(in);
        }

        public Lead[] newArray(int size) {
            return (new Lead[size]);
        }

    }
            ;
    private final static long serialVersionUID = -6334438436749817400L;

    public Lead(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.budget = ((String) in.readValue((String.class.getClassLoader())));
        this.phoneNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.country = ((Country) in.readValue((Country.class.getClassLoader())));
        this.city = ((City) in.readValue((City.class.getClassLoader())));
        this.location = ((Location) in.readValue((Location.class.getClassLoader())));
        this.quality = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.leadRemarks = ((String) in.readValue((String.class.getClassLoader())));
        this.leadStatus = ((LeadStatus) in.readValue((LeadStatus.class.getClassLoader())));
        this.feedbackComment = ((String) in.readValue((String.class.getClassLoader())));
        this.used = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.feedbackSubmitted = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.assigned = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.selfGenerated = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.submitted = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.managedByRm = ((ManagedByRm) in.readValue((ManagedByRm.class.getClassLoader())));
        in.readList(this.getActivities, (com.csestateconnect.www.csconnect.models.lead.GetActivity.class.getClassLoader()));
        in.readList(this.getInteractionDates, (com.csestateconnect.www.csconnect.models.lead.GetInteractionDate.class.getClassLoader()));
        this.user = ((User) in.readValue((User.class.getClassLoader())));
        this.dealId = ((DealId) in.readValue((DealId.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Lead() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public String getLeadRemarks() {
        return leadRemarks;
    }

    public void setLeadRemarks(String leadRemarks) {
        this.leadRemarks = leadRemarks;
    }

    public LeadStatus getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(LeadStatus leadStatus) {
        this.leadStatus = leadStatus;
    }

    public String getFeedbackComment() {
        return feedbackComment;
    }

    public void setFeedbackComment(String feedbackComment) {
        this.feedbackComment = feedbackComment;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public Boolean getFeedbackSubmitted() {
        return feedbackSubmitted;
    }

    public void setFeedbackSubmitted(Boolean feedbackSubmitted) {
        this.feedbackSubmitted = feedbackSubmitted;
    }

    public Boolean getAssigned() {
        return assigned;
    }

    public void setAssigned(Boolean assigned) {
        this.assigned = assigned;
    }

    public Boolean getSelfGenerated() {
        return selfGenerated;
    }

    public void setSelfGenerated(Boolean selfGenerated) {
        this.selfGenerated = selfGenerated;
    }

    public Boolean getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Boolean submitted) {
        this.submitted = submitted;
    }

    public ManagedByRm getManagedByRm() {
        return managedByRm;
    }

    public void setManagedByRm(ManagedByRm managedByRm) {
        this.managedByRm = managedByRm;
    }

    public List<GetActivity> getGetActivities() {
        return getActivities;
    }

    public void setGetActivities(List<GetActivity> getActivities) {
        this.getActivities = getActivities;
    }

    public List<GetInteractionDate> getGetInteractionDates() {
        return getInteractionDates;
    }

    public void setGetInteractionDates(List<GetInteractionDate> getInteractionDates) {
        this.getInteractionDates = getInteractionDates;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DealId getDealId() {
        return dealId;
    }

    public void setUser(DealId dealId) {
        this.dealId = dealId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(budget);
        dest.writeValue(phoneNumber);
        dest.writeValue(email);
        dest.writeValue(country);
        dest.writeValue(city);
        dest.writeValue(location);
        dest.writeValue(quality);
        dest.writeValue(leadRemarks);
        dest.writeValue(leadStatus);
        dest.writeValue(feedbackComment);
        dest.writeValue(used);
        dest.writeValue(feedbackSubmitted);
        dest.writeValue(assigned);
        dest.writeValue(selfGenerated);
        dest.writeValue(submitted);
        dest.writeValue(managedByRm);
        dest.writeList(getActivities);
        dest.writeList(getInteractionDates);
        dest.writeValue(user);
        dest.writeValue(dealId);
        dest.writeValue(createdAt);
        dest.writeValue(updatedAt);
    }
    public int describeContents() {
        return 0;
    }

}