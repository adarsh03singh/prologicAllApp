package com.prologic.strains.search_dialog

import android.app.Activity
import android.app.Dialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.view.Window

import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.prologic.strains.R

import com.prologic.strains.databinding.SearchAdapterBinding
import com.prologic.strains.utils.OnSearchDialogListener
import com.prologic.strains.utils.TAG
import com.prologic.strains.utils.loadImage
import java.util.*
import kotlin.collections.ArrayList


class SearchDialog(var searchItemArray: ArrayList<SearchItem>, val activity: Activity) {
    val searchAdapter = SearchAdapter(searchItemArray)
    var searchDialogListener: OnSearchDialogListener? = null
    lateinit var dialog: Dialog
    lateinit var search: EditText

    init {
        initialize()
        //update(searchItemArray)
    }

    fun update(searchItemArray: ArrayList<SearchItem>) {
        this.searchItemArray = searchItemArray
    }

    fun setOnItemClickListItem(searchDialogListener: OnSearchDialogListener) {
        this.searchDialogListener = searchDialogListener
    }


    fun setHint(title: String) {
        search.setHint(title)
    }

    fun initialize() {
        dialog = Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(R.color.trans)
        dialog.setContentView(R.layout.search_dialog)
        dialog.setCancelable(false)
        val layout = dialog.findViewById<LinearLayout>(R.id.layout)
        val back = dialog.findViewById<ImageView>(R.id.back)
        search = dialog.findViewById(R.id.search)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = searchAdapter
        recyclerView.setHasFixedSize(true)
        back.setOnClickListener {
            dialog.dismiss()
        }
        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchAdapter.updateAdapter(search.text.toString())
            }
        })
        dialog.show()
    }


    inner class SearchAdapter(var searchItemArrayTemp: ArrayList<SearchItem>) :
        RecyclerView.Adapter<SearchAdapter.SearchHolder>() {

        fun updateAdapter(value: String) {

            if (value.isEmpty()) {
                searchItemArrayTemp = searchItemArray
            } else {
                val turkishLocale = Locale.forLanguageTag("us")
                searchItemArrayTemp = ArrayList<SearchItem>()
                for (item in searchItemArray) {
                    if (item.title.uppercase(turkishLocale)
                            .contains(value.uppercase(turkishLocale))
                    ) {
                        searchItemArrayTemp.add(item)
                    }
                }
            }
            Log.d(TAG, "SIZE " + searchItemArray.size + "  " + searchItemArrayTemp.size)
            notifyDataSetChanged()
        }

        override fun getItemCount() = searchItemArrayTemp.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
            val binding = SearchAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return SearchHolder(binding)
        }

        override fun onBindViewHolder(holder: SearchHolder, position: Int) {
            holder.bindHolder(position)
        }

        inner class SearchHolder(val binding: SearchAdapterBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bindHolder(position: Int) {
                val item = searchItemArrayTemp[position]
                if (item.url.isEmpty()) {
                    binding.image.setImageBitmap(null)
                    binding.image.visibility = View.GONE
                } else {
                    loadImage(binding.image, item.url)
                    binding.image.visibility = View.VISIBLE
                }
                if (item.title.isEmpty()) {
                    binding.title.text = ""
                    binding.title.visibility = View.GONE
                } else {
                    binding.title.text = item.title
                    binding.title.visibility = View.VISIBLE
                }
                if (item.text.isEmpty()) {
                    binding.text.text = ""
                    binding.text.visibility = View.GONE
                } else {
                    binding.text.text = item.text
                    binding.text.visibility = View.VISIBLE
                }
                binding.click.setOnClickListener {
                    dialog.dismiss()
                    searchDialogListener?.onClick(item)
                }
            }
        }


    }

}