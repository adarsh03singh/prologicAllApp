package com.prologic.laughinggoat.view.activity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.prologic.laughinggoat.R

import com.prologic.laughinggoat.databinding.SquareActivityBinding

import com.prologic.laughinggoat.utils.*

import com.prologic.laughinggoat.viewmodel.SquareViewModel

import sqip.Callback
import sqip.CardEntry
import sqip.CardEntryActivityResult


class SquareActivity : FragmentActivity() {
    lateinit var binding: SquareActivityBinding
    lateinit var viewModel: SquareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.square_activity)
        viewModel = ViewModelProvider(this).get(SquareViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        intent.extras
        val payment_amount =    intent.extras!!.getInt("payment_amount", 0)
        if (payment_amount == 0) {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        } else {
            val cardHandler = CardEntryBackgroundHandler(payment_amount)
            CardEntry.setCardNonceBackgroundHandler(cardHandler)
            CardEntry.startCardEntryActivity(this)
        }


    }

    override fun onBackPressed() {
        val intent = Intent()
        setResult(RESULT_CANCELED, intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        CardEntry.handleActivityResult(data, object : Callback<CardEntryActivityResult> {
            override fun onResult(result: CardEntryActivityResult) {
                val intent = Intent()
                if (result.isSuccess()) {
                    setResult(RESULT_OK, intent)
                } else {
                    setResult(RESULT_CANCELED, intent)
                }
                finish()
            }
        })
    }

}

