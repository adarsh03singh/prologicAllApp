package com.csestateconnect.ui.navdrawer.calenderEvents

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.csestateconnect.R
import com.csestateconnect.databinding.FragmentEditEventBinding
import com.csestateconnect.db.data.events.GetEventCategoriesList
import com.csestateconnect.db.data.events.Result
import com.csestateconnect.viewmodel.EventsViewModel
import kotlinx.android.synthetic.main.fragment_add_events.*
import java.util.*


class EditEventFragment : Fragment(), View.OnClickListener {

    var autorememberDropDown: AutoCompleteTextView? = null
    var activeStatusDropDown: AutoCompleteTextView? = null
    var PriorityDropDown: AutoCompleteTextView? = null
    lateinit var categoriesListData: List<Result>
    var categoryIndex: Int = 0
    var eventCategoryDropDown: AutoCompleteTextView? = null
    var isSelectedText: Boolean = false
    var timeTonotify: String? = null
    var eventid:Int? = null

    var status_radioGroup: RadioGroup? = null
    var autoReminder_radioGroup: RadioGroup? = null

    lateinit var status_radioButton: RadioButton
    lateinit var autoReminder_RadioButton: RadioButton


    lateinit var viewModel: EventsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_event, container, false)

        val binding: FragmentEditEventBinding = FragmentEditEventBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(EventsViewModel::class.java)
        binding.editEventViewmodel = viewModel
        binding.lifecycleOwner = this
        if(isNetworkConnected(context!!))
        {
            try {
                viewModel.callEventCategoriesListAPIAndStore()

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        else
        {
            Toast.makeText(context,"No internet found.", Toast.LENGTH_LONG).show()
        }

        viewModel.getEventCategoriesListFromDatabase().observe(this, androidx.lifecycle.Observer<GetEventCategoriesList> { catgoryData ->
            try {
                categoriesListData = catgoryData.results!!
                val categoryId = categoriesListData.map { it.id.toString()}
                val categoryName = categoriesListData.map { it.name!!}
                setUpCategoryDropDown(categoryId,categoryName)

            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }
        })

        return view
    }

//    @SuppressLint("ClickableViewAccessibility")
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         eventid = arguments?.getInt("eventId")
        if(!eventid!!.equals(null)){
            viewModel.previousIdForUpdateEvent.value = eventid
        }

    status_radioGroup = view.findViewById(R.id.status_radioGroup)
    autoReminder_radioGroup = view.findViewById(R.id.autoreminder_radioGroup)
//        autorememberDropDown= view.findViewById(R.id.autoRemember_autocompleteView)
//        activeStatusDropDown = view.findViewById(R.id.activeStatus_autocompleteView)
        PriorityDropDown= view.findViewById(R.id.priority_autocompleteView)
        eventCategoryDropDown = view.findViewById(R.id.categories_autocompleteView)

    status_radioGroup!!.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
            status_radioButton = status_radioGroup!!.findViewById(checkedId)
            val index: Int = status_radioGroup!!.indexOfChild(status_radioButton)
            when (index) {
                0 ->
                    viewModel.eventActiveStatus.value = status_radioButton.text.toString()
                1 ->
                    viewModel.eventActiveStatus.value = status_radioButton.text.toString()
            }
        }
    })

    autoReminder_radioGroup!!.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
            autoReminder_RadioButton = autoReminder_radioGroup!!.findViewById(checkedId)
            val index: Int = autoReminder_radioGroup!!.indexOfChild(autoReminder_RadioButton)
            when (index) {
                0 ->
                    viewModel.eventAutoReminder.value = autoReminder_RadioButton.text.toString()
                1 ->
                    viewModel.eventAutoReminder.value = autoReminder_RadioButton.text.toString()
            }
        }
    })

    /*     val aRItems = resources.getStringArray(R.array.AutoRememberWithActiveStatusItems)
         val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, aRItems)
         autorememberDropDown!!.setAdapter(adapter)
         autorememberDropDown!!.setOnTouchListener(object : OnTouchListener {
             override fun onTouch(
                 paramView: android.view.View?,
                 paramMotionEvent: MotionEvent?
             ): kotlin.Boolean {
                 if (aRItems.size > 0) {
                     // show all suggestions
                     if (!autorememberDropDown!!.getText().toString().equals("")) adapter.getFilter()
                         .filter(null)
                     autorememberDropDown!!.showDropDown()
                 }
                 return false
             }
         })

     val asItems = resources.getStringArray(R.array.AutoRememberWithActiveStatusItems)
     val adapter3 = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, asItems)
     activeStatusDropDown!!.setAdapter(adapter3)

     activeStatusDropDown!!.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
     object : OnTouchListener {
         override fun onTouch(
             paramView: android.view.View?,
             paramMotionEvent: MotionEvent?
         ): kotlin.Boolean {
             if (asItems.size > 0) {
                 // show all suggestions
                 if (!activeStatusDropDown!!.getText().toString().equals("")) adapter3.getFilter()
                     .filter(null)
                 activeStatusDropDown!!.showDropDown()
             }
             return false
         }
     })*/

        val priItems = resources.getStringArray(R.array.priorityItems)
        val adapter2 = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, priItems)
        PriorityDropDown!!.setAdapter(adapter2)
        PriorityDropDown!!.setOnTouchListener(OnTouchListener { paramView, paramMotionEvent ->
            if (priItems.size > 0) {
                // show all suggestions
                if (!PriorityDropDown!!.getText().toString().equals("")) adapter2.filter
                    .filter(null)
                PriorityDropDown!!.showDropDown()
            }
            false
        })



        time_EditBox.setOnClickListener(this)
        date_EditBox.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view === time_EditBox) {
            selectTime()
        } else if (view === date_EditBox) {
            selectDate()
        }
    }

    private fun selectTime() {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(context,
            TimePickerDialog.OnTimeSetListener { timePicker, i, i1 ->
                timeTonotify = "$i:$i1"
                time_EditBox.setText(FormatTime(i, i1))
            }, hour, minute, false
        )
        timePickerDialog.show()
    }

    private fun selectDate() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(context!!,
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                date_EditBox.setText(year.toString() + "-" + (month + 1) + "-" + day)
            },
            year,
            month,
            day
        )
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis())
        datePickerDialog.show()
    }


    fun FormatTime(hour: Int, minute: Int): String? {
        var time: String
        time = ""
        val formattedMinute: String
        formattedMinute = if (minute / 10 == 0) {
            "0$minute"
        } else {
            "" + minute
        }
        time = if (hour == 0) {
            "12:$formattedMinute AM"
        } else if (hour < 12) {
            "$hour:$formattedMinute AM"
        } else if (hour == 12) {
            "12:$formattedMinute PM"
        } else {
            val temp = hour - 12
            "$temp:$formattedMinute PM"
        }
        return time
    }

    fun setUpCategoryDropDown(numberId: List<String>, categoryNameList: List<String>){

        // Country Spinner
        if (eventCategoryDropDown != null) {
            val countryAdapter = ArrayAdapter(context!!, R.layout.autocomplete_textview_dropdown_items,
                R.id.autoTextView_item_id,
                categoryNameList
            )

            eventCategoryDropDown!!.setAdapter(countryAdapter)
        }

        eventCategoryDropDown!!.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val countryNamePosition = parent!!.getItemAtPosition(position).toString()
                try {

                    val countryId = numberId.get(position)
                    categoryIndex = categoryNameList.indexOf(countryNamePosition)
                    viewModel.eventCategoriesId.value =  countryId

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