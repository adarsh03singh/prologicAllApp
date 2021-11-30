package com.csestateconnect.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.marginStart
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.db.data.listOfConcerns.Comments
import com.csestateconnect.utils.DatenTimeChatConverter
import com.csestateconnect.utils.DatenTimeConverter
import kotlinx.android.synthetic.main.concern_comment_chatview.view.*
import kotlinx.android.synthetic.main.concern_tickets_list_items.view.*

class ConcernDetailsChatAdapter(val commentList: List<Comments>, val userId: Int?) : RecyclerView.Adapter<ConcernDetailsChatAdapter.ViewHolder>() {

    var navController: NavController? = null

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        navController = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.concern_comment_chatview, p0,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (commentList[position].comment_by == userId) {
            holder.boxColorL.visibility = View.GONE
            holder.boxColorR.visibility = View.VISIBLE
            holder.boxTextR.text = commentList[position].body
            holder.boxDateR.text = DatenTimeChatConverter().dateConverter(commentList[position].created_at)
        }
        else {
            holder.boxColorL.visibility = View.VISIBLE
            holder.boxColorR.visibility = View.GONE
            holder.boxTextL.text = commentList[position].body
            holder.boxDateL.text = DatenTimeChatConverter().dateConverter(commentList[position].created_at)
        }
    }

    override fun getItemCount(): Int {
        return commentList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var boxTextL = itemView.chatbox_text
        var boxTextR = itemView.chatbox_textR
        var boxColorL = itemView.chatbox_cardview
        var boxColorR = itemView.chatbox_cardviewR
        var boxDateL = itemView.chatbox_date
        var boxDateR = itemView.chatbox_dateR

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}
