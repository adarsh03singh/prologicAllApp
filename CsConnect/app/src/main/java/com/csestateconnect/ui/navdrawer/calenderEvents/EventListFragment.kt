package com.csestateconnect.ui.navdrawer.calenderEvents

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.csestateconnect.R
import com.csestateconnect.adapters.CalenderEventAdapter
import com.csestateconnect.databinding.FragmentEventListBinding
import com.csestateconnect.db.data.events.eventsDataList.GetEventsDataEntity
import com.csestateconnect.viewmodel.EventsViewModel
import kotlinx.android.synthetic.main.fragment_event_list.*
import java.util.*

class EventListFragment : Fragment() {

    lateinit var viewModel: EventsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_event_list, container, false)

        val binding: FragmentEventListBinding = FragmentEventListBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(EventsViewModel::class.java)
        binding.eventListViewmodel = viewModel
        binding.lifecycleOwner = this

        if(isNetworkConnected(context!!))
        {
            try {
                viewModel.callEventdataListAPIAndStore()

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        else
        {
            Toast.makeText(context,"No internet found.", Toast.LENGTH_LONG).show()
        }

        viewModel.getEventDataListFromDatabase().observe(this, androidx.lifecycle.Observer<GetEventsDataEntity> { eventData ->
            try {
//                for (i in 0..eventData.results!!.size - 1 ) {

                    val cilData = eventData.results

                    val clientdata = cilData!!.map { it } as MutableList

                        setUpEventListRecyclerView(clientdata)
//                }

            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }
        })
        return view

    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val startDate = Calendar.getInstance()
//        startDate.add(Calendar.MONTH, -1)
//
//        val endDate = Calendar.getInstance()
//        endDate.add(Calendar.MONTH, 1)
//
        addEvent_button.setOnClickListener {
            view.findNavController().navigate(R.id.addEventFragment)
        }
//
//        val horizontalCalendar = HorizontalCalendar.Builder(view,
//            R.id.calendarView
//        ).datesNumberOnScreen(5).build()
//
//        horizontalCalendar.setCalendarListener(object : HorizontalCalendarListener() {
//            override fun onDateSelected(date: Date?, position: Int) {
//
//            }
//        })
    }

    fun setUpEventListRecyclerView(clList : MutableList<com.csestateconnect.db.data.events.eventsDataList.Result>)
    {

        val eventRecyclerViewAdapter = CalenderEventAdapter(clList) { eventId ->
            if(!eventId.equals(null)) {
            viewModel.callRemoveEventApi(eventId.toInt(), view!!)
            }

        }


        eventRecyclerView.adapter = eventRecyclerViewAdapter
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(
            context, // Context
            LinearLayoutManager.VERTICAL, false
        )
        eventRecyclerView.layoutManager = linearLayoutManager
        eventRecyclerView.setHasFixedSize(true)

    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }


}