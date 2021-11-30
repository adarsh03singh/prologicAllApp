
package com.csestateconnect.www.csconnect.models.home;

import android.widget.ImageView;

public class HomeProjectImageSliderList
{
        private String sliderIconImage;
    private String projectName;
    private String ProjectLocation;
    private Integer ProjectId;

        public HomeProjectImageSliderList(String sliderIconImage, String projectName, String ProjectLocation, Integer ProjectId) {
            this.sliderIconImage = sliderIconImage;
            this.projectName = projectName;
            this.ProjectLocation = ProjectLocation;
            this.ProjectId = ProjectId;

        }

        public String getSliderIconImage() {
            return sliderIconImage;
        }

        public void setSliderIconImage(String sliderIconImage) {
            this.sliderIconImage = sliderIconImage;
        }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectLocation() {
        return ProjectLocation;
    }

    public void setProjectLocation(String ProjectLocation) {
        this.ProjectLocation = ProjectLocation;
    }

    public Integer getProjectId() {
        return ProjectId;
    }

    public void setProjectId(Integer ProjectId) {
        this.ProjectId = ProjectId;
    }

}
