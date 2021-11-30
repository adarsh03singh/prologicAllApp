package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnitPlanComponentPrice implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("price_component_type")
    @Expose
    private PriceComponentType priceComponentType;
    @SerializedName("component_price")
    @Expose
    private Float componentPrice;
    @SerializedName("component_price_view")
    @Expose
    private String componentPriceView;
    public final static Parcelable.Creator<UnitPlanComponentPrice> CREATOR = new Creator<UnitPlanComponentPrice>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UnitPlanComponentPrice createFromParcel(Parcel in) {
            return new UnitPlanComponentPrice(in);
        }

        public UnitPlanComponentPrice[] newArray(int size) {
            return (new UnitPlanComponentPrice[size]);
        }

    }
            ;
    private final static long serialVersionUID = 5562980654735248272L;

    protected UnitPlanComponentPrice(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.priceComponentType = ((PriceComponentType) in.readValue((PriceComponentType.class.getClassLoader())));
        this.componentPrice = ((Float) in.readValue((Float.class.getClassLoader())));
        this.componentPriceView = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UnitPlanComponentPrice() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PriceComponentType getPriceComponentType() {
        return priceComponentType;
    }

    public void setPriceComponentType(PriceComponentType priceComponentType) {
        this.priceComponentType = priceComponentType;
    }

    public Float getComponentPrice() {
        return componentPrice;
    }

    public void setComponentPrice(Float componentPrice) {
        this.componentPrice = componentPrice;
    }

    public String getComponentPriceView() {
        return componentPriceView;
    }

    public void setComponentPriceView(String componentPriceView) {
        this.componentPriceView = componentPriceView;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(priceComponentType);
        dest.writeValue(componentPrice);
        dest.writeValue(componentPriceView);
    }

    public int describeContents() {
        return 0;
    }

}