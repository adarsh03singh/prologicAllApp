package com.csestateconnect.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import kotlinx.android.synthetic.main.event_items.view.*

class CalenderEventAdapter(private val eventData : MutableList<com.csestateconnect.db.data.events.eventsDataList.Result>,
                           val clickItem: (String) ->Unit) :
    RecyclerView.Adapter<CalenderEventAdapter.ViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        val v: View = LayoutInflater.from(p0?.context)
            .inflate(R.layout.event_items,p0,false)
        return ViewHolder(v)
    }

/*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {

    val eList = eventData[p1]
    holder.eventSubject?.text = "Subject - " +  eList!!.subject
    holder.eventDescription?.text ="Description - " + eList!!.description
    holder.eventDatetime?.text =  eList!!.date + " , "+ eList!!.time
    holder.updateEvent?.setOnClickListener {
//        clickItem(parent.personName!!)
        val eventId = eList.id
        val bundle = bundleOf("eventId" to eventId)
        holder.itemView.findNavController().navigate(R.id.editEventFragment, bundle)
    }

    holder.deleteEvent?.setOnClickListener {
        clickItem(eList.id.toString())
        eventData.removeAt(p1)
        notifyItemRemoved(p1)
        notifyItemRangeChanged(p1, getItemCount())
    }
    }

    override fun getItemCount(): Int {
        return eventData.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var eventSubject = itemView.eventSubject
        var eventDescription = itemView.eventDescription
        var eventDatetime = itemView.eventDataetime
        var updateEvent = itemView.update_event_item
        var deleteEvent = itemView.delete_event_item
        var layoutparent = itemView.parentLayout
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }
}