package com.csestateconnect.db.data.deals

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.leads.Result
import com.csestateconnect.db.data.typeconverter.DealsResultTypeConverters

@TypeConverters(DealsResultTypeConverters::class)
@Entity(tableName = "deal_result")
data class Result(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val lead: Result,
    val deal_status: DealStatus,
    val project: Project,
    val commission_amount_total: String,
    val commission_amount_total_view: String,
    val commission_amount_payable: String,
    val commission_amount_paid: String,
    val commission_amount_paid_view: String,
    val commission_amount_unpaid: String,
    val commission_amount_unpaid_view: String,
    val sold_area_sq_ft: String,
    val commission_rate_sq_ft: String,
    val commission_percentage: String,
    val cheque_image: String?,
    val created_at: String,
    val updated_at: String,
    val payment_approved: Boolean,
    val paid: Boolean,
    val days_left_for_payment: Int,
    val commission_amount_payable_view: String?,
    val payment_approval_date: String?
)