
package com.csestateconnect.www.csconnect.models.home;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Commission implements Serializable, Parcelable
{

    @SerializedName("commission_amount_total__sum")
    @Expose
    private Integer commissionAmountTotalSum;
    @SerializedName("commission_amount_paid__sum")
    @Expose
    private Integer commissionAmountPaidSum;
    @SerializedName("commission_amount_unpaid__sum")
    @Expose
    private Integer commissionAmountUnpaidSum;
    public final static Creator<Commission> CREATOR = new Creator<Commission>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Commission createFromParcel(Parcel in) {
            return new Commission(in);
        }

        public Commission[] newArray(int size) {
            return (new Commission[size]);
        }

    }
    ;
    private final static long serialVersionUID = -319505158337449507L;

    protected Commission(Parcel in) {
        this.commissionAmountTotalSum = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.commissionAmountPaidSum = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.commissionAmountUnpaidSum = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public Commission() {
    }

    public Integer getCommissionAmountTotalSum() {
        return commissionAmountTotalSum;
    }

    public void setCommissionAmountTotalSum(Integer commissionAmountTotalSum) {
        this.commissionAmountTotalSum = commissionAmountTotalSum;
    }

    public Commission withCommissionAmountTotalSum(Integer commissionAmountTotalSum) {
        this.commissionAmountTotalSum = commissionAmountTotalSum;
        return this;
    }

    public Integer getCommissionAmountPaidSum() {
        return commissionAmountPaidSum;
    }

    public void setCommissionAmountPaidSum(Integer commissionAmountPaidSum) {
        this.commissionAmountPaidSum = commissionAmountPaidSum;
    }

    public Commission withCommissionAmountPaidSum(Integer commissionAmountPaidSum) {
        this.commissionAmountPaidSum = commissionAmountPaidSum;
        return this;
    }

    public Integer getCommissionAmountUnpaidSum() {
        return commissionAmountUnpaidSum;
    }

    public void setCommissionAmountUnpaidSum(Integer commissionAmountUnpaidSum) {
        this.commissionAmountUnpaidSum = commissionAmountUnpaidSum;
    }

    public Commission withCommissionAmountUnpaidSum(Integer commissionAmountUnpaidSum) {
        this.commissionAmountUnpaidSum = commissionAmountUnpaidSum;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(commissionAmountTotalSum);
        dest.writeValue(commissionAmountPaidSum);
        dest.writeValue(commissionAmountUnpaidSum);
    }

    public int describeContents() {
        return  0;
    }

}
