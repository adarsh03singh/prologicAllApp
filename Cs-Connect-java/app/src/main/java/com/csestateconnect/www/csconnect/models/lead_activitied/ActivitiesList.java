
package com.csestateconnect.www.csconnect.models.lead_activitied;

public class ActivitiesList {
    private String activities;
    private String date;

    public ActivitiesList() {
    }

    public ActivitiesList(String date, String activities) {
        this.date = date;
        this.activities = activities;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

}
