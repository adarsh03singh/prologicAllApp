
package com.csestateconnect.www.csconnect.models.search;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchFilter implements Serializable
{

    @SerializedName("_filter_amenities")
    @Expose
    private FilterAmenities filterAmenities;
    @SerializedName("_filter_bsp")
    @Expose
    private FilterBsp filterBsp;
    @SerializedName("_filter_rooms")
    @Expose
    private FilterRooms filterRooms;
    @SerializedName("_filter_price")
    @Expose
    private FilterPrice filterPrice;
    @SerializedName("_filter_area")
    @Expose
    private FilterArea filterArea;
    @SerializedName("_filter_status")
    @Expose
    private FilterStatus filterStatus;
    @SerializedName("_filter_locations")
    @Expose
    private FilterLocations filterLocations;
    private final static long serialVersionUID = 6635484528107246163L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public SearchFilter() {
    }

    /**
     * 
     * @param filterBsp
     * @param filterPrice
     * @param filterAmenities
     * @param filterLocations
     * @param filterRooms
     * @param filterStatus
     * @param filterArea
     */
    public SearchFilter(FilterAmenities filterAmenities, FilterBsp filterBsp, FilterRooms filterRooms, FilterPrice filterPrice, FilterArea filterArea, FilterStatus filterStatus, FilterLocations filterLocations) {
        super();
        this.filterAmenities = filterAmenities;
        this.filterBsp = filterBsp;
        this.filterRooms = filterRooms;
        this.filterPrice = filterPrice;
        this.filterArea = filterArea;
        this.filterStatus = filterStatus;
        this.filterLocations = filterLocations;
    }

    public FilterAmenities getFilterAmenities() {
        return filterAmenities;
    }

    public void setFilterAmenities(FilterAmenities filterAmenities) {
        this.filterAmenities = filterAmenities;
    }

    public FilterBsp getFilterBsp() {
        return filterBsp;
    }

    public void setFilterBsp(FilterBsp filterBsp) {
        this.filterBsp = filterBsp;
    }

    public FilterRooms getFilterRooms() {
        return filterRooms;
    }

    public void setFilterRooms(FilterRooms filterRooms) {
        this.filterRooms = filterRooms;
    }

    public FilterPrice getFilterPrice() {
        return filterPrice;
    }

    public void setFilterPrice(FilterPrice filterPrice) {
        this.filterPrice = filterPrice;
    }

    public FilterArea getFilterArea() {
        return filterArea;
    }

    public void setFilterArea(FilterArea filterArea) {
        this.filterArea = filterArea;
    }

    public FilterStatus getFilterStatus() {
        return filterStatus;
    }

    public void setFilterStatus(FilterStatus filterStatus) {
        this.filterStatus = filterStatus;
    }

    public FilterLocations getFilterLocations() {
        return filterLocations;
    }

    public void setFilterLocations(FilterLocations filterLocations) {
        this.filterLocations = filterLocations;
    }

}
