package com.prologicwebsolution.eventshare.ui.festivalsImages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.prologicwebsolution.eventshare.R
import com.prologicwebsolution.eventshare.adapter.CalendarAdapter
import com.prologicwebsolution.eventshare.adapter.ImageCategoryAdapter
import com.prologicwebsolution.eventshare.databinding.FragmentFestivalImageBinding
import com.prologicwebsolution.eventshare.ui.imageList.ImageListfragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FestivalImageFragment : Fragment() {


    lateinit var viewmodel: FestivalImagesViewModel
    private val lastDayInCalendar = Calendar.getInstance(Locale.ENGLISH)
    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)

    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private val currentDay = currentDate[Calendar.DAY_OF_MONTH]
    private val currentMonth = currentDate[Calendar.MONTH]
    private val currentYear = currentDate[Calendar.YEAR]

    private var selectedDay: Int = currentDay
    private var selectedMonth: Int = currentMonth
    private var selectedYear: Int = currentYear

    private val dates = ArrayList<Date>()

    lateinit var  calendar_recycler_view: RecyclerView
    lateinit var  calendar_prev_button: ImageView
    lateinit var  calendar_next_button: ImageView
    lateinit var  txt_current_month: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_festival_image, container, false)
        val binding: FragmentFestivalImageBinding = FragmentFestivalImageBinding.bind(view)
        viewmodel = ViewModelProviders.of(this.requireActivity()).get(FestivalImagesViewModel::class.java)
        binding.festivalImageViewModel = viewmodel
        binding.lifecycleOwner = this

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendar_recycler_view = view.findViewById(R.id.calendar_recycler_view)
        calendar_prev_button = view.findViewById(R.id.calendar_prev_button)
        calendar_next_button = view.findViewById(R.id.calendar_next_button)
        txt_current_month = view.findViewById(R.id.txt_current_month)

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(calendar_recycler_view)

        lastDayInCalendar.add(Calendar.YEAR, 3)

        setUpCalendar()

        calendar_prev_button.setOnClickListener {
            if (cal.after(currentDate)) {
                cal.add(Calendar.MONTH, -1)
                if (cal == currentDate)
                    setUpCalendar()
                else
                    setUpCalendar(changeMonth = cal)
            }
        }

        calendar_next_button.setOnClickListener {
            if (cal.before(lastDayInCalendar)) {
                cal.add(Calendar.MONTH, 1)
                setUpCalendar(changeMonth = cal)
            }
        }
    }

    private fun setUpCalendar(changeMonth: Calendar? = null) {
        // first part
        txt_current_month.text = sdf.format(cal.time)
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        selectedDay =
            when {
                changeMonth != null -> changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
                else -> currentDay
            }
        selectedMonth =
            when {
                changeMonth != null -> changeMonth[Calendar.MONTH]
                else -> currentMonth
            }
        selectedYear =
            when {
                changeMonth != null -> changeMonth[Calendar.YEAR]
                else -> currentYear
            }

        // second part
        var currentPosition = 0
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)

        while (dates.size < maxDaysInMonth) {
            if (monthCalendar[Calendar.DAY_OF_MONTH] == selectedDay)
                currentPosition = dates.size
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // third part
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        calendar_recycler_view.layoutManager = layoutManager
        val calendarAdapter = CalendarAdapter(requireContext(), dates, currentDate, changeMonth)
        calendar_recycler_view.adapter = calendarAdapter

        when {
            currentPosition > 2 -> calendar_recycler_view.scrollToPosition(currentPosition - 3)
            maxDaysInMonth - currentPosition < 2 -> calendar_recycler_view.scrollToPosition(currentPosition)
            else -> calendar_recycler_view.scrollToPosition(currentPosition)
        }

        calendarAdapter.setOnItemClickListener(object : CalendarAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickCalendar = Calendar.getInstance()
                clickCalendar.time = dates[position]
                selectedDay = clickCalendar[Calendar.DAY_OF_MONTH]
            }
        })
    }

    private  fun setImagesFestivalImageryRecyclerView(){
        //getting recyclerview from xml
        val recyclerView = requireView().findViewById(R.id.categoryRecyclerview) as RecyclerView

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        //crating an arraylist to store categoryList using the data class ImageCategoryModel
        val categoryList = ArrayList<ImageListfragment.ImageCategoryModel>()

        //adding some dummy data to the list
        categoryList.add(ImageListfragment.ImageCategoryModel("GSt"))
        categoryList.add(ImageListfragment.ImageCategoryModel("Insurance"))
        categoryList.add(ImageListfragment.ImageCategoryModel("ITR"))
        categoryList.add(ImageListfragment.ImageCategoryModel("Insurance"))
        categoryList.add(ImageListfragment.ImageCategoryModel("gst"))
        categoryList.add(ImageListfragment.ImageCategoryModel("Itr"))
        categoryList.add(ImageListfragment.ImageCategoryModel("Licence"))
        categoryList.add(ImageListfragment.ImageCategoryModel("Insurance"))
        categoryList.add(ImageListfragment.ImageCategoryModel("ITR"))
        categoryList.add(ImageListfragment.ImageCategoryModel("Insurance"))

        //creating our adapter
        val adapter = ImageCategoryAdapter(categoryList)

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter
    }

}