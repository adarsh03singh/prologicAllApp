
package com.csestateconnect.www.csconnect.models.deal;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.csestateconnect.www.csconnect.models.lead.Lead;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Deal implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lead")
    @Expose
    private Lead lead;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("deal_status")
    @Expose
    private DealStatus dealStatus;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("project")
    @Expose
    private Project project;
    @SerializedName("commission_amount_total")
    @Expose
    private Integer commissionAmountTotal;
    @SerializedName("commission_amount_total_view")
    @Expose
    private String commissionAmountTotalView;
    @SerializedName("commission_amount_paid")
    @Expose
    private Integer commissionAmountPaid;
    @SerializedName("commission_amount_paid_view")
    @Expose
    private String commissionAmountPaidView;
    @SerializedName("commission_amount_unpaid")
    @Expose
    private Integer commissionAmountUnpaid;
    @SerializedName("commission_amount_unpaid_view")
    @Expose
    private String commissionAmountUnpaidView;
    @SerializedName("sold_area_sq_ft")
    @Expose
    private Integer soldAreaSqFt;
    @SerializedName("commission_rate_sq_ft")
    @Expose
    private Integer commissionRateSqFt;
    @SerializedName("commission_percentage")
    @Expose
    private Integer commissionPercentage;
    @SerializedName("cheque_image")
    @Expose
    private String chequeImage;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    public final static Creator<Deal> CREATOR = new Creator<Deal>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Deal createFromParcel(Parcel in) {
            return new Deal(in);
        }

        public Deal[] newArray(int size) {
            return (new Deal[size]);
        }

    }
    ;
    private final static long serialVersionUID = 5284145251118252134L;

    protected Deal(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.lead = ((Lead) in.readValue((Lead.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.phoneNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.dealStatus = ((DealStatus) in.readValue((DealStatus.class.getClassLoader())));
        this.project = ((Project) in.readValue((Project.class.getClassLoader())));
        this.commissionAmountTotal = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.commissionAmountTotalView = ((String) in.readValue((String.class.getClassLoader())));
        this.commissionAmountPaid = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.commissionAmountPaidView = ((String) in.readValue((String.class.getClassLoader())));
        this.commissionAmountUnpaid = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.commissionAmountUnpaidView = ((String) in.readValue((String.class.getClassLoader())));
        this.soldAreaSqFt = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.commissionRateSqFt = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.commissionPercentage = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.chequeImage = ((String) in.readValue((String.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Deal() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public DealStatus getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(DealStatus dealStatus) {
        this.dealStatus = dealStatus;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Integer getCommissionAmountTotal() {
        return commissionAmountTotal;
    }

    public void setCommissionAmountTotal(Integer commissionAmountTotal) {
        this.commissionAmountTotal = commissionAmountTotal;
    }

    public String getCommissionAmountTotalView() {
        return commissionAmountTotalView;
    }

    public void setCommissionAmountTotalView(String commissionAmountTotalView) {
        this.commissionAmountTotalView = commissionAmountTotalView;
    }

    public Integer getCommissionAmountPaid() {
        return commissionAmountPaid;
    }

    public void setCommissionAmountPaid(Integer commissionAmountPaid) {
        this.commissionAmountPaid = commissionAmountPaid;
    }

    public String getCommissionAmountPaidView() {
        return commissionAmountPaidView;
    }

    public void setCommissionAmountPaidView(String commissionAmountPaidView) {
        this.commissionAmountPaidView = commissionAmountPaidView;
    }

    public Integer getCommissionAmountUnpaid() {
        return commissionAmountUnpaid;
    }

    public void setCommissionAmountUnpaid(Integer commissionAmountUnpaid) {
        this.commissionAmountUnpaid = commissionAmountUnpaid;
    }

    public String getCommissionAmountUnpaidView() {
        return commissionAmountUnpaidView;
    }

    public void setCommissionAmountUnpaidView(String commissionAmountUnpaidView) {
        this.commissionAmountUnpaidView = commissionAmountUnpaidView;
    }

    public Integer getSoldAreaSqFt() {
        return soldAreaSqFt;
    }

    public void setSoldAreaSqFt(Integer soldAreaSqFt) {
        this.soldAreaSqFt = soldAreaSqFt;
    }

    public Integer getCommissionRateSqFt() {
        return commissionRateSqFt;
    }

    public void setCommissionRateSqFt(Integer commissionRateSqFt) {
        this.commissionRateSqFt = commissionRateSqFt;
    }

    public Integer getCommissionPercentage() {
        return commissionPercentage;
    }

    public void setCommissionPercentage(Integer commissionPercentage) {
        this.commissionPercentage = commissionPercentage;
    }

    public String getChequeImage() {
        return chequeImage;
    }

    public void setChequeImage(String chequeImage) {
        this.chequeImage = chequeImage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(lead);
        dest.writeValue(phoneNumber);
        dest.writeValue(email);
        dest.writeValue(dealStatus);
        dest.writeValue(project);
        dest.writeValue(commissionAmountTotal);
        dest.writeValue(commissionAmountTotalView);
        dest.writeValue(commissionAmountPaid);
        dest.writeValue(commissionAmountPaidView);
        dest.writeValue(commissionAmountUnpaid);
        dest.writeValue(commissionAmountUnpaidView);
        dest.writeValue(soldAreaSqFt);
        dest.writeValue(commissionRateSqFt);
        dest.writeValue(commissionPercentage);
        dest.writeValue(chequeImage);
        dest.writeValue(createdAt);
        dest.writeValue(updatedAt);
    }

    public int describeContents() {
        return  0;
    }

}
