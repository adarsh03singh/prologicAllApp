
package com.csestateconnect.www.csconnect.models.search;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterArea implements Serializable
{

    @SerializedName("doc_count")
    @Expose
    private Long docCount;
    @SerializedName("selected")
    @Expose
    private Selected__ selected;
    @SerializedName("area")
    @Expose
    private Area area;
    private final static long serialVersionUID = 4110905213415646850L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FilterArea() {
    }

    /**
     * 
     * @param selected
     * @param area
     * @param docCount
     */
    public FilterArea(Long docCount, Selected__ selected, Area area) {
        super();
        this.docCount = docCount;
        this.selected = selected;
        this.area = area;
    }

    public Long getDocCount() {
        return docCount;
    }

    public void setDocCount(Long docCount) {
        this.docCount = docCount;
    }

    public Selected__ getSelected() {
        return selected;
    }

    public void setSelected(Selected__ selected) {
        this.selected = selected;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

}
