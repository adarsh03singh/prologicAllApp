package com.prologic.laughinggoat.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.prologic.laughinggoat.model.squareup.AmountMoney
import com.prologic.laughinggoat.model.squareup.SquareRequest
import com.prologic.laughinggoat.network.Repository
import kotlinx.coroutines.*
import sqip.CardDetails
import sqip.CardEntryActivityCommand
import sqip.CardNonceBackgroundHandler
import java.util.*

class CardEntryBackgroundHandler(val payment_amount: Int) : CardNonceBackgroundHandler {
    val repository = Repository()

    override fun handleEnteredCardInBackground(cardDetails: CardDetails): CardEntryActivityCommand {
        val uuid = UUID.randomUUID().toString()
        val amountMoney = AmountMoney(
            amount = payment_amount,
            currency = "USD"
        )
        val squareRequest = SquareRequest(
            idempotency_key = uuid,
            source_id = cardDetails.nonce,
            amount_money = amountMoney
        )
        val status = getApiStatus(squareRequest)
        if (status)
            return CardEntryActivityCommand.Finish()
        else
            return CardEntryActivityCommand.ShowError("There was an error processing the payment, please check your network connection.")

    }


    fun getApiStatus(squareRequest: SquareRequest): Boolean {
        var status: Boolean? = null
        runBlocking {
            val job = async { repository.updateSquarePayment(squareRequest) }
            status = job.await().isSuccessful
        }
        return status!!
    }

}