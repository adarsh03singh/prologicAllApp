package org.gstsuvidhakendra.mygsk.data;




public class GetGskAddressListdata{

    public int iD;
    public String billing_phone;
    public String billing_company;
    public String billing_address_1;
    public String billing_address_2;
    public String billing_state;
    public String billing_city;
    public String billing_postcode;

    public GetGskAddressListdata(int iD,String billing_phone,String billing_company,String billing_address_1, String billing_address_2, String billing_state,
                                 String billing_city, String billing_postcode) {

        this.iD = iD;
        this.billing_phone = billing_phone;
        this.billing_company = billing_company;
        this.billing_address_1 = billing_address_1;
        this.billing_address_2 = billing_address_2;
        this.billing_state = billing_state;
        this.billing_city = billing_city;
        this.billing_postcode = billing_postcode;
    }

    // creating getter and setter methods.

    public int getId() {
        return iD;
    }

    public void setiD(int iD) {
        this.iD = iD;
    }

    public String getBilling_phone() {
        return billing_phone;
    }

    public void setBilling_phone(String billing_phone) {
        this.billing_phone = billing_phone;
    }

    public String getBilling_company() {
        return billing_company;
    }

    public void setBilling_company(String billing_company) {
        this.billing_company = billing_company;
    }

    public String getBilling_address_1() {
        return billing_address_1;
    }

    public void setBilling_address_1(String billing_address_1) {
        this.billing_address_1 = billing_address_1;
    }

    public String getBilling_address_2() {
        return billing_address_2;
    }

    public void setBilling_address_2(String billing_address_2) {
        this.billing_address_2 = billing_address_2;
    }

    public String getBilling_state() {
        return billing_state;
    }

    public void setBilling_state(String billing_state) {
        this.billing_state = billing_state;
    }

    public String getBilling_city() {
        return billing_city;
    }

    public void setBilling_city(String billing_city) {
        this.billing_city = billing_city;
    }

    public String getBilling_postcode() {
        return billing_postcode;
    }

    public void setBilling_postcode(String billing_postcode) {
        this.billing_postcode = billing_postcode;
    }

}

