
package com.csestateconnect.www.csconnect.models.search;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterRooms implements Serializable
{

    @SerializedName("doc_count")
    @Expose
    private Long docCount;
    @SerializedName("rooms")
    @Expose
    private Rooms rooms;
    private final static long serialVersionUID = 2296180950147570365L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FilterRooms() {
    }

    /**
     * 
     * @param docCount
     * @param rooms
     */
    public FilterRooms(Long docCount, Rooms rooms) {
        super();
        this.docCount = docCount;
        this.rooms = rooms;
    }

    public Long getDocCount() {
        return docCount;
    }

    public void setDocCount(Long docCount) {
        this.docCount = docCount;
    }

    public Rooms getRooms() {
        return rooms;
    }

    public void setRooms(Rooms rooms) {
        this.rooms = rooms;
    }

}
