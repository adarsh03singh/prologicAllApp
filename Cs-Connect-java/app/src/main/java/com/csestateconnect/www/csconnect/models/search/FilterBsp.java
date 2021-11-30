
package com.csestateconnect.www.csconnect.models.search;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterBsp implements Serializable
{

    @SerializedName("doc_count")
    @Expose
    private Long docCount;
    @SerializedName("selected")
    @Expose
    private Selected selected;
    @SerializedName("bsp")
    @Expose
    private Bsp bsp;
    private final static long serialVersionUID = 893824863808245821L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FilterBsp() {
    }

    /**
     * 
     * @param selected
     * @param docCount
     * @param bsp
     */
    public FilterBsp(Long docCount, Selected selected, Bsp bsp) {
        super();
        this.docCount = docCount;
        this.selected = selected;
        this.bsp = bsp;
    }

    public Long getDocCount() {
        return docCount;
    }

    public void setDocCount(Long docCount) {
        this.docCount = docCount;
    }

    public Selected getSelected() {
        return selected;
    }

    public void setSelected(Selected selected) {
        this.selected = selected;
    }

    public Bsp getBsp() {
        return bsp;
    }

    public void setBsp(Bsp bsp) {
        this.bsp = bsp;
    }

}
