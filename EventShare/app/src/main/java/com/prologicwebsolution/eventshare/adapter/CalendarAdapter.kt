package com.prologicwebsolution.eventshare.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prologicwebsolution.eventshare.R
import com.prologicwebsolution.eventshare.data.FestivalImageModel
import com.prologicwebsolution.eventshare.data.imageData.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter(private val context: Context,
                      private val data: ArrayList<Date>,
                      private val currentDate: Calendar,
                      private val changeMonth: Calendar?): RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    var navController: NavController? = null
    private var mListener: AdapterView.OnItemClickListener? = null
    private var index = -1
    private var selectCurrentDate = true
    private val currentMonth = currentDate[Calendar.MONTH]
    private val currentYear = currentDate[Calendar.YEAR]
    private val currentDay = currentDate[Calendar.DAY_OF_MONTH]
    private val selectedDay =
        when {
            changeMonth != null -> changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
            else -> currentDay
        }
    private val selectedMonth =
        when {
            changeMonth != null -> changeMonth[Calendar.MONTH]
            else -> currentMonth
        }
    private val selectedYear =
        when {
            changeMonth != null -> changeMonth[Calendar.YEAR]
            else -> currentYear
        }



    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        navController = Navigation.findNavController(parent)
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.calendar_day_layout, parent, false), mListener!!)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss", Locale.ENGLISH)
        val cal = Calendar.getInstance()
        cal.time = data[position]

        val displayMonth = cal[Calendar.MONTH]
        val displayYear= cal[Calendar.YEAR]
        val displayDay = cal[Calendar.DAY_OF_MONTH]

        try {
            val dayInWeek = sdf.parse(cal.time.toString())!!
            sdf.applyPattern("EEE")
            holder.txtDayInWeek!!.text = sdf.format(dayInWeek).toString()
        } catch (ex: ParseException) {
            Log.v("Exception", ex.localizedMessage!!)
        }
        holder.txtDay!!.text = cal[Calendar.DAY_OF_MONTH].toString()
        // we will add onClickListener here

        if (displayYear >= currentYear)
            if (displayMonth >= currentMonth || displayYear > currentYear)
                if (displayDay >= currentDay || displayMonth > currentMonth || displayYear > currentYear) {
                    holder.linearLayout!!.setOnClickListener {
                        index = position
                        selectCurrentDate = false
                        holder.listener.onItemClick(position)
                        notifyDataSetChanged()
                    }

                    if (index == position)
                        makeItemSelected(holder)
                    else {
                        if (displayDay == selectedDay
                            && displayMonth == selectedMonth
                            && displayYear == selectedYear
                            && selectCurrentDate)
                            makeItemSelected(holder)
                        else
                            makeItemDefault(holder)
                    }
                } else makeItemDisabled(holder)
            else makeItemDisabled(holder)
        else makeItemDisabled(holder)


        // we will add Festival images in recyclerView here
        val layoutManager = LinearLayoutManager(holder.festivalImageRecyclerView.getContext(),
            LinearLayoutManager.HORIZONTAL, false
        )
        holder.festivalImageRecyclerView!!.layoutManager = layoutManager
        val festivalImageList = ArrayList<FestivalImageModel>()

        //adding some dummy data to the list
        festivalImageList.add(FestivalImageModel(R.drawable.company_registration))
        festivalImageList.add(FestivalImageModel(R.drawable.friendship_day))
        festivalImageList.add(FestivalImageModel(R.drawable.ganesh_chaturthi))
        festivalImageList.add(FestivalImageModel(R.drawable.independence_day))
        festivalImageList.add(FestivalImageModel(R.drawable.krishna_janmashtami))
        festivalImageList.add(FestivalImageModel(R.drawable.muharram))
        festivalImageList.add(FestivalImageModel(R.drawable.national_mounta_climbing_day))
        festivalImageList.add(FestivalImageModel(R.drawable.national_sports_day))
        festivalImageList.add(FestivalImageModel(R.drawable.onam))
        festivalImageList.add(FestivalImageModel(R.drawable.raksha_bandhan))

        //creating our adapter
        val adapter = FestivalImagesAdapter(festivalImageList, data){
            val bundle = bundleOf("imagePosition" to position)
            navController!!.navigate(R.id.imageShareFragment, bundle)
        }

        //now adding the adapter to recyclerview
        holder.festivalImageRecyclerView.adapter = adapter
    }

    inner class ViewHolder(itemView: View, val listener: AdapterView.OnItemClickListener): RecyclerView.ViewHolder(itemView) {
        var txtDay = itemView.findViewById<TextView>(R.id.txt_date)
        var txtDayInWeek = itemView.findViewById<TextView>(R.id.txt_day)
        var linearLayout = itemView.findViewById<LinearLayout>(R.id.calendar_linear_layout)
        var festivalImageRecyclerView = itemView.findViewById<RecyclerView>(R.id.festivallImagesRecyclerView)

    }


    interface OnItemClickListener : AdapterView.OnItemClickListener {
        fun onItemClick(position: Int)
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            TODO("Not yet implemented")
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    private fun makeItemDisabled(holder: ViewHolder) {
        holder.txtDay!!.setTextColor(ContextCompat.getColor(context, R.color.purple_500))
        holder.txtDayInWeek!!.setTextColor(ContextCompat.getColor(context, R.color.purple_500))
        holder.linearLayout!!.setBackgroundColor(Color.WHITE)
        holder.linearLayout!!.isEnabled = false
    }

    private fun makeItemSelected(holder: ViewHolder) {
        holder.txtDay!!.setTextColor(Color.parseColor("#5b09d6"))
        holder.txtDayInWeek!!.setTextColor(Color.parseColor("#5b09d6"))//"#09d64d"
//        holder.linearLayout!!.setBackgroundColor(ContextCompat.getColor(context, R.color.teal_700))
        holder.linearLayout!!.isEnabled = false
    }

    private fun makeItemDefault(holder: ViewHolder) {
        holder.txtDay!!.setTextColor(Color.BLACK)
        holder.txtDayInWeek!!.setTextColor(Color.BLACK)
        holder.linearLayout!!.setBackgroundColor(Color.WHITE)
        holder.linearLayout!!.isEnabled = true
    }

}

//data class FestivalImageModel(var images: Int)
private fun AdapterView.OnItemClickListener.onItemClick(position: Int) {

}

