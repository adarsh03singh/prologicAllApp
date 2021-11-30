
package com.csestateconnect.www.csconnect.models.search;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bucket__ implements Serializable
{

    @SerializedName("key")
    @Expose
    private Long key;
    @SerializedName("doc_count")
    @Expose
    private Long docCount;
    @SerializedName("checked")
    @Expose
    private Boolean checked;
    private final static long serialVersionUID = 6267237925245469267L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Bucket__() {
    }

    /**
     * 
     * @param docCount
     * @param checked
     * @param key
     */
    public Bucket__(Long key, Long docCount, Boolean checked) {
        super();
        this.key = key;
        this.docCount = docCount;
        this.checked = checked;
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

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

}
