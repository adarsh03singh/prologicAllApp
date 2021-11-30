
package com.csestateconnect.www.csconnect.models.search;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status implements Serializable
{

    @SerializedName("doc_count_error_upper_bound")
    @Expose
    private Long docCountErrorUpperBound;
    @SerializedName("sum_other_doc_count")
    @Expose
    private Long sumOtherDocCount;
    @SerializedName("buckets")
    @Expose
    private List<Bucket_____> buckets = null;
    private final static long serialVersionUID = 5176190343200581901L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Status() {
    }

    /**
     * 
     * @param sumOtherDocCount
     * @param buckets
     * @param docCountErrorUpperBound
     */
    public Status(Long docCountErrorUpperBound, Long sumOtherDocCount, List<Bucket_____> buckets) {
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

    public List<Bucket_____> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Bucket_____> buckets) {
        this.buckets = buckets;
    }

}
