
package com.csestateconnect.www.csconnect.models.search;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterStatus implements Serializable
{

    @SerializedName("doc_count")
    @Expose
    private Long docCount;
    @SerializedName("status")
    @Expose
    private Status status;
    private final static long serialVersionUID = -2086742352977818052L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FilterStatus() {
    }

    /**
     * 
     * @param status
     * @param docCount
     */
    public FilterStatus(Long docCount, Status status) {
        super();
        this.docCount = docCount;
        this.status = status;
    }

    public Long getDocCount() {
        return docCount;
    }

    public void setDocCount(Long docCount) {
        this.docCount = docCount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
