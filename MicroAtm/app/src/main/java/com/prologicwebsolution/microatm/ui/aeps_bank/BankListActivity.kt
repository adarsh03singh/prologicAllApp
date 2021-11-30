package com.prologicwebsolution.microatm.ui.aeps_bank

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView


import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.data.aepsBankList.BankCodeValue
import com.prologicwebsolution.microatm.databinding.BankListActivityBinding
import com.prologicwebsolution.microatm.util.Constants
import com.prologicwebsolution.microatm.util.MyProgressDialog
import kotlinx.android.synthetic.main.bank_list_activity.*
import java.util.*
import kotlin.collections.ArrayList


class BankListActivity : AppCompatActivity() {
    var thisActivity: Activity = this
    var recyclerViewAdapter: MyRecyclerViewAdapter = MyRecyclerViewAdapter()
    lateinit var viewModel: BankListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: BankListActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.bank_list_activity)
        viewModel = ViewModelProvider(this).get(BankListViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        recyclerView.adapter = recyclerViewAdapter

        viewModel.codeValueList.observe(this, Observer {
            recyclerViewAdapter.updateAdapter(it)
        })
        viewModel.onBackPressedCall.observe(this, Observer {
            onBackPressed()
        })
        viewModel.errorMessage.observe(this, Observer {
            showToast(it)
        })
        viewModel.searchValue.observe(this, Observer {
            recyclerViewAdapter.filter(it)
        })
        viewModel.errorMessage.observe(this, Observer {
            if (!it.isEmpty()) {
                showToast(it)
            }
        })
        viewModel.progress_bar.observe(this, Observer {
            if (it) {
                MyProgressDialog.show(thisActivity)
            } else {
                MyProgressDialog.dismiss()
            }
        })
        viewModel.getApi()

    }


    private fun showToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
    }


    inner class MyRecyclerViewAdapter : RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {
        var arrayListTemp: ArrayList<BankCodeValue> = ArrayList()
        var arrayListLive: ArrayList<BankCodeValue> = ArrayList()

        fun updateAdapter(arrayListLive: ArrayList<BankCodeValue>) {
            this.arrayListLive = arrayListLive
            filter("")
        }

        @SuppressLint("NotifyDataSetChanged")
        fun filter(value: String) {
            if (value.isEmpty()) {
                arrayListTemp = arrayListLive
            } else {
                arrayListTemp = ArrayList()
                for (i in 0 until arrayListLive.size) {
                    val codeValue = arrayListLive.get(i)
                    if (codeValue.value.toLowerCase(Locale.getDefault())
                            .contains(value.toLowerCase(Locale.getDefault()))
                    ) {
                        arrayListTemp.add(codeValue)
                    }
                }
            }
            Log.d(Constants.TAG, "LIST SIZE " + arrayListTemp.size)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(thisActivity).inflate(R.layout.bank_adp, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var codeValue = arrayListTemp.get(position)
            holder.name.text = codeValue.value
            holder.name.setOnClickListener { v: View? ->
                val intent = Intent()
                intent.putExtra("code", codeValue.code)
                intent.putExtra("value", codeValue.value)
                setResult(RESULT_OK, intent)
                finish()
            }

        }

        override fun getItemCount(): Int {
            return arrayListTemp.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var name: TextView = itemView.findViewById(R.id.name)
        }

    }
}