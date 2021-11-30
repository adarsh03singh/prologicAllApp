
package com.csestateconnect.www.csconnect.models.search;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterPrice implements Serializable
{

    @SerializedName("doc_count")
    @Expose
    private Long docCount;
    @SerializedName("selected")
    @Expose
    private Selected_ selected;
    @SerializedName("price")
    @Expose
    private Price price;
    private final static long serialVersionUID = 783615887325729903L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FilterPrice() {
    }

    /**
     * 
     * @param selected
     * @param price
     * @param docCount
     */
    public FilterPrice(Long docCount, Selected_ selected, Price price) {
        super();
        this.docCount = docCount;
        this.selected = selected;
        this.price = price;
    }

    public Long getDocCount() {
        return docCount;
    }

    public void setDocCount(Long docCount) {
        this.docCount = docCount;
    }

    public Selected_ getSelected() {
        return selected;
    }

    public void setSelected(Selected_ selected) {
        this.selected = selected;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

}
