
package com.csestateconnect.www.csconnect.models.search;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bucket implements Serializable
{

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("doc_count")
    @Expose
    private Long docCount;
    @SerializedName("checked")
    @Expose
    private Boolean checked;
    private final static long serialVersionUID = 809497527189476470L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Bucket() {
    }

    /**
     * 
     * @param docCount
     * @param checked
     * @param key
     */
    public Bucket(String key, Long docCount, Boolean checked) {
        super();
        this.key = key;
        this.docCount = docCount;
        this.checked = checked;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getDocCount() {
        return docCount;
    }

    public void setDocCount(Long docCount) {
        this.docCount = docCount;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

}
