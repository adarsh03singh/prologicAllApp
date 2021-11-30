
package com.csestateconnect.www.csconnect.models.search;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Locations implements Serializable
{

    @SerializedName("doc_count_error_upper_bound")
    @Expose
    private Long docCountErrorUpperBound;
    @SerializedName("sum_other_doc_count")
    @Expose
    private Long sumOtherDocCount;
    @SerializedName("buckets")
    @Expose
    private List<Bucket______> buckets = null;
    private final static long serialVersionUID = -5376307488043181406L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Locations() {
    }

    /**
     * 
     * @param sumOtherDocCount
     * @param buckets
     * @param docCountErrorUpperBound
     */
    public Locations(Long docCountErrorUpperBound, Long sumOtherDocCount, List<Bucket______> buckets) {
        super();
        this.docCountErrorUpperBound = docCountErrorUpperBound;
        this.sumOtherDocCount = sumOtherDocCount;
        this.buckets = buckets;
    }

    public Long getDocCountErrorUpperBound() {
        return docCountErrorUpperBound;
    }

    public void setDocCountErrorUpperBound(Long docCountErrorUpperBound) {
        this.docCountErrorUpperBound = docCountErrorUpperBound;
    }

    public Long getSumOtherDocCount() {
        return sumOtherDocCount;
    }

    public void setSumOtherDocCount(Long sumOtherDocCount) {
        this.sumOtherDocCount = sumOtherDocCount;
    }

    public List<Bucket______> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Bucket______> buckets) {
        this.buckets = buckets;
    }

}
