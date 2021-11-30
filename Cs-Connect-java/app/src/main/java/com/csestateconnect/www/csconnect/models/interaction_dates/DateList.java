
package com.csestateconnect.www.csconnect.models.interaction_dates;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DateList
{
        private String date;

        public DateList() {
        }

        public DateList(String date) {
            this.date = date;

        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

}
