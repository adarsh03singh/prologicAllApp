
package com.csestateconnect.www.csconnect.models.search;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Selected_ implements Serializable
{

    @SerializedName("from")
    @Expose
    private Long from;
    @SerializedName("to")
    @Expose
    private Long to;
    private final static long serialVersionUID = 2865585433562225452L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Selected_() {
    }

    /**
     * 
     * @param to
     * @param from
     */
    public Selected_(Long from, Long to) {
        super();
        this.from = from;
        this.to = to;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

}
