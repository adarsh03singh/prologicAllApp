package com.csestateconnect.ui.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.csestateconnect.R
import com.csestateconnect.adapters.ConcernDetailsChatAdapter
import com.csestateconnect.databinding.FragmentConcernTicketDetailsBinding
import com.csestateconnect.db.data.leads.ListOfLeads
import com.csestateconnect.db.data.listOfConcerns.Comments
import com.csestateconnect.db.data.listOfConcerns.GetListOfConcerns
import com.csestateconnect.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_concern_ticket_details.*
import kotlinx.coroutines.runBlocking

/**
 * A simple [Fragment] subclass.
 */
class ConcernTicketDetailsFragment : Fragment() {
    lateinit var viewmodel: HomeViewModel
    var concernDetailsAdapter: ConcernDetailsChatAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_concern_ticket_details, container, false)

        val binding: FragmentConcernTicketDetailsBinding = FragmentConcernTicketDetailsBinding.bind(view)
        viewmodel = ViewModelProviders.of(this.requireActivity()).get(HomeViewModel::class.java)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        val concernId = arguments?.get("concernId").toString()

        if (concernId.isNotEmpty()) {
            if (concernId != "null") {

                viewmodel.getallConcernsData().observe(this, Observer<GetListOfConcerns> { data ->

                    val idList = data.results?.map { it.id }
                    var concernIndex : Int = -1
                    if (!idList.isNullOrEmpty()) {
                        concernIndex = idList.indexOf(concernId.toInt())
                    }

                    if (concernIndex != -1) {
                        viewmodel.getConcernDetailById(data, concernIndex)
                    }

                    val userId = data.results?.get(concernIndex)?.user?.id
                    val commentsList = data.results?.get(concernIndex)?.comments
                    if (commentsList != null && commentsList.isNotEmpty()){
                        ticket_chat_recyclerview.visibility = View.VISIBLE
                        setUpConcernsDetailRecyclerView(commentsList, userId)
                    }
                })
            }
        }

        val id = arguments?.get("notifyId").toString()

        if(id.isNotEmpty()) {
            if (id != "null") {

                runBlocking {
                    viewmodel.getListOfConcerns()
                }

                viewmodel.getallConcernsData().observe(this, Observer<GetListOfConcerns> { data ->

                    val idList = data.results?.map { it.id }
                    var concernIndex : Int = -1
                    if (!idList.isNullOrEmpty()) {
                        concernIndex = idList.indexOf(id.toInt())
                    }

                    if (concernIndex != -1) {
                        viewmodel.getConcernDetailById(data, concernIndex)
                    }

                    val userId = data.results?.get(concernIndex)?.user?.id
                    val commentsList = data.results?.get(concernIndex)?.comments
                    if (commentsList != null && commentsList.isNotEmpty()){
                        ticket_chat_recyclerview.visibility = View.VISIBLE
                        setUpConcernsDetailRecyclerView(commentsList, userId)
                    }
                })
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun setUpConcernsDetailRecyclerView(
        commentsList: List<Comments>,
        userId: Int?
    ) {

        concernDetailsAdapter = ConcernDetailsChatAdapter(commentsList, userId)
        ticket_chat_recyclerview.adapter = concernDetailsAdapter
        ticket_chat_recyclerview.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        ticket_chat_recyclerview.setHasFixedSize(true)
    }
}
