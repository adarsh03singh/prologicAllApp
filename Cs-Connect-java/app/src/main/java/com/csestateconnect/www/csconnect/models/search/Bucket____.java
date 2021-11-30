
package com.csestateconnect.www.csconnect.models.search;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bucket____ implements Serializable
{

    @SerializedName("key")
    @Expose
    private Long key;
    @SerializedName("doc_count")
    @Expose
    private Long docCount;
    private final static long serialVersionUID = -6127357658121159547L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Bucket____() {
    }

    /**
     * 
     * @param docCount
     * @param key
     */
    public Bucket____(Long key, Long docCount) {
        super();
        this.key = key;
        this.docCount = docCount;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Long getDocCount() {
        return docCount;
    }

    public void setDocCount(Long docCount) {
        this.docCount = docCount;
    }

}
