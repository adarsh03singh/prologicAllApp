package com.csestateconnect.ui.navdrawer.reimbursements

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.csestateconnect.R
import com.csestateconnect.databinding.FragmentAddClientsBinding
import com.csestateconnect.databinding.FragmentCreateReimbursementBinding
import com.csestateconnect.db.data.events.GetEventCategoriesList
import com.csestateconnect.db.data.events.Result
import com.csestateconnect.db.data.reimbursements.GetReimbursementTypeEntity
import com.csestateconnect.viewmodel.ClientsViewModel
import com.csestateconnect.viewmodel.ReimbursementViewModel
import kotlinx.android.synthetic.main.fragment_create_reimbursement.*

class CreateReimbursementFragment : Fragment() {

    lateinit var viewModel: ReimbursementViewModel
    var reimburseTypeIndex: Int = 0
    lateinit var reimburseListData: List<com.csestateconnect.db.data.reimbursements.Result>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_reimbursement, container, false)

        val binding: FragmentCreateReimbursementBinding = FragmentCreateReimbursementBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(ReimbursementViewModel::class.java)
        binding.createReimburseViewmodel = viewModel
        binding.lifecycleOwner = this

        if (isNetworkConnected(context!!)) {
            try {
                viewModel.callReimburseTypesListAPIAndStore()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(context, "No internet found.", Toast.LENGTH_LONG).show()
        }

        viewModel.getReimburseTypesListFromDatabase().observe(this, androidx.lifecycle.Observer<GetReimbursementTypeEntity>
        { typeData ->
            try {
                reimburseListData = typeData.results!!
                val reimburseTypeId = reimburseListData.map { it.id.toString() }
                val reimburseTypeName = reimburseListData.map { it.name!! }
                setUpReimburseTypesDropDown(reimburseTypeId, reimburseTypeName)

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        })

     return  view
    }

    fun setUpReimburseTypesDropDown(numberId: List<String>, reimburseNameList: List<String>) {

        // Country Spinner
        if (remburseType_dropDown != null) {
            val reimburseAdapter = ArrayAdapter(
                context!!, R.layout.autocomplete_textview_dropdown_items,
                R.id.autoTextView_item_id,
                reimburseNameList
            )
            remburseType_dropDown!!.setAdapter(reimburseAdapter)
        }

        remburseType_dropDown!!.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val reimburseNamePosition = parent!!.getItemAtPosition(position).toString()
                try {

                    val reimbursementTypeId = numberId.get(position)
                    reimburseTypeIndex = reimburseNameList.indexOf(reimburseNamePosition)

                    val sharedPreference =
                        context!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                    val editor = sharedPreference.edit()
                    editor.putInt("countryIndex", reimburseTypeIndex)
                    editor.apply()
                    viewModel.reimbursementTypeId.value = reimbursementTypeId

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }


}