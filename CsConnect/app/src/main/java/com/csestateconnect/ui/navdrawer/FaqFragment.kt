package com.csestateconnect.ui.navdrawer


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.csestateconnect.R
import kotlinx.android.synthetic.main.app_bar_home_acticity.*
import kotlinx.android.synthetic.main.fragment_faq.*

/**
 * A simple [Fragment] subclass.
 */
class FaqFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_faq, container, false)

//        setSupportActionBar(toolbar)
//
//        supportActionBar!!.title = "FAQ"
//        supportActionBar?.setDisplayShowHomeEnabled(true)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //  CS CONNECT CARDVIEW CODE START
        faq_cs_connect_drop_down.setOnClickListener {
            if (faq_cs_connect_view.visibility == View.GONE && faq_all_qna_cs_connect.visibility == View.GONE) {
                faq_cs_connect_view.visibility = View.VISIBLE
                faq_all_qna_cs_connect.visibility = View.VISIBLE
                faq_cs_connect_drop_down.visibility = View.GONE
                faq_cs_connect_drop_close.visibility = View.VISIBLE
                faq_cs_connect_logo.setImageResource(R.drawable.ic_logo_csconnect_orange)
                faq_cs_connect_cardview_heading.setTextColor(Color.parseColor("#D17E19"))

            } else {
                faq_cs_connect_view.visibility = View.GONE
                faq_all_qna_cs_connect.visibility = View.GONE
                faq_cs_connect_drop_down.visibility = View.VISIBLE
                faq_cs_connect_drop_close.visibility = View.GONE
                faq_cs_connect_cardview.minimumHeight = 50
                faq_cs_connect_logo.setImageResource(R.drawable.ic_logo_csconnect_black)
                faq_cs_connect_cardview_heading.setTextColor(Color.parseColor("#424646"))


            }
        }

        faq_cs_connect_drop_close.setOnClickListener {
            if (faq_cs_connect_view.visibility == View.VISIBLE && faq_all_qna_cs_connect.visibility == View.VISIBLE) {
                faq_cs_connect_view.visibility = View.GONE
                faq_all_qna_cs_connect.visibility = View.GONE
                faq_cs_connect_drop_down.visibility = View.VISIBLE
                faq_cs_connect_drop_close.visibility = View.GONE
                faq_cs_connect_logo.setImageResource(R.drawable.ic_logo_csconnect_black)
                faq_cs_connect_cardview_heading.setTextColor(Color.parseColor("#424646"))

            }

        }


        // Faq cs connect card view
        faq_cs_connect_cardview_1_linear_layout.setOnClickListener {
            if (faq_cs_connect_view.visibility == View.GONE && faq_all_qna_cs_connect.visibility == View.GONE) {

                faq_cs_connect_view.visibility = View.VISIBLE
                faq_all_qna_cs_connect.visibility = View.VISIBLE
                faq_cs_connect_drop_down.visibility = View.GONE
                faq_cs_connect_drop_close.visibility = View.VISIBLE
                faq_cs_connect_logo.setImageResource(R.drawable.ic_logo_csconnect_orange)
                faq_cs_connect_cardview_heading.setTextColor(Color.parseColor("#D17E19"))
            } else if (faq_cs_connect_view.visibility == View.VISIBLE && faq_all_qna_cs_connect.visibility == View.VISIBLE) {

                faq_cs_connect_view.visibility = View.GONE
                faq_all_qna_cs_connect.visibility = View.GONE
                faq_cs_connect_drop_down.visibility = View.VISIBLE
                faq_cs_connect_drop_close.visibility = View.GONE
                faq_cs_connect_cardview.minimumHeight = 50
                faq_cs_connect_logo.setImageResource(R.drawable.ic_logo_csconnect_black)
                faq_cs_connect_cardview_heading.setTextColor(Color.parseColor("#424646"))


            }
        }


        // Faq cs connect Q1
        faq_cs_connect_q1_linear_layout.setOnClickListener {
            if (faq_cs_connect_q1_drop_down.visibility == View.VISIBLE && faq_cs_connect_q1_answer.visibility == View.GONE) {

                faq_cs_connect_q1_answer.visibility = View.VISIBLE
                faq_cs_connect_q1_drop_down.visibility = View.GONE
                faq_cs_connect_q1_drop_close.visibility = View.VISIBLE
            } else if (faq_cs_connect_q1_drop_down.visibility == View.GONE && faq_cs_connect_q1_answer.visibility == View.VISIBLE) {

                faq_cs_connect_q1_answer.visibility = View.GONE
                faq_cs_connect_q1_drop_down.visibility = View.VISIBLE
                faq_cs_connect_q1_drop_close.visibility = View.GONE

            }
        }

        // Faq cs connect Q2
        faq_cs_connect_q2_linear_layout.setOnClickListener {
            if (faq_cs_connect_q2_drop_down.visibility == View.VISIBLE && faq_cs_connect_q2_answer.visibility == View.GONE) {

                faq_cs_connect_q2_answer.visibility = View.VISIBLE
                faq_cs_connect_q2_drop_down.visibility = View.GONE
                faq_cs_connect_q2_drop_close.visibility = View.VISIBLE

            } else if (faq_cs_connect_q2_drop_down.visibility == View.GONE && faq_cs_connect_q2_answer.visibility == View.VISIBLE) {

                faq_cs_connect_q2_answer.visibility = View.GONE
                faq_cs_connect_q2_drop_down.visibility = View.VISIBLE
                faq_cs_connect_q2_drop_close.visibility = View.GONE


            }
        }

        //  CS CONNECT CARDVIEW CODE END


        // CHANNEL PARTNERS CARDVIEW CODE START

        faq_channel_partners_drop_down.setOnClickListener {
            if (faq_channel_partners_view.visibility == View.GONE && faq_all_qna_channel_partners.visibility == View.GONE) {
                faq_channel_partners_view.visibility = View.VISIBLE
                faq_all_qna_channel_partners.visibility = View.VISIBLE
                faq_channel_partners_drop_down.visibility = View.GONE
                faq_channel_partners_drop_close.visibility = View.VISIBLE
                faq_channel_partners_logo.setImageResource(R.drawable.ic_logo_channel_partners_orange)
                faq_channel_partners_cardview_heading.setTextColor(Color.parseColor("#D17E19"))
            } else {
                faq_channel_partners_view.visibility = View.GONE
                faq_all_qna_channel_partners.visibility = View.GONE
                faq_channel_partners_drop_down.visibility = View.VISIBLE
                faq_channel_partners_drop_close.visibility = View.GONE
                faq_channel_partners_cardview.minimumHeight = 50
                faq_channel_partners_logo.setImageResource(R.drawable.ic_logo_channel_partners_black)
                faq_channel_partners_cardview_heading.setTextColor(Color.parseColor("#424646"))
            }
        }

        faq_channel_partners_drop_close.setOnClickListener {
            if (faq_channel_partners_view.visibility == View.VISIBLE && faq_all_qna_channel_partners.visibility == View.VISIBLE) {
                faq_channel_partners_view.visibility = View.GONE
                faq_all_qna_channel_partners.visibility = View.GONE
                faq_channel_partners_drop_down.visibility = View.VISIBLE
                faq_channel_partners_drop_close.visibility = View.GONE
                faq_channel_partners_logo.setImageResource(R.drawable.ic_logo_channel_partners_black)
                faq_channel_partners_cardview_heading.setTextColor(Color.parseColor("#424646"))
            }

        }


        // Faq channel_partners card view
        faq_channel_partners_cardview_1_linear_layout.setOnClickListener {
            if (faq_channel_partners_view.visibility == View.GONE && faq_all_qna_channel_partners.visibility == View.GONE) {

                faq_channel_partners_view.visibility = View.VISIBLE
                faq_all_qna_channel_partners.visibility = View.VISIBLE
                faq_channel_partners_drop_down.visibility = View.GONE
                faq_channel_partners_drop_close.visibility = View.VISIBLE
                faq_channel_partners_logo.setImageResource(R.drawable.ic_logo_channel_partners_orange)
                faq_channel_partners_cardview_heading.setTextColor(Color.parseColor("#D17E19"))
            } else if (faq_channel_partners_view.visibility == View.VISIBLE && faq_all_qna_channel_partners.visibility == View.VISIBLE) {

                faq_channel_partners_view.visibility = View.GONE
                faq_all_qna_channel_partners.visibility = View.GONE
                faq_channel_partners_drop_down.visibility = View.VISIBLE
                faq_channel_partners_drop_close.visibility = View.GONE
                faq_channel_partners_logo.setImageResource(R.drawable.ic_logo_channel_partners_black)
                faq_channel_partners_cardview_heading.setTextColor(Color.parseColor("#424646"))


            }
        }


        // Faq channel_partners Q1
        faq_channel_partners_q1_linear_layout.setOnClickListener {
            if (faq_channel_partners_q1_drop_down.visibility == View.VISIBLE && faq_channel_partners_q1_answer.visibility == View.GONE) {

                faq_channel_partners_q1_answer.visibility = View.VISIBLE
                faq_channel_partners_q1_drop_down.visibility = View.GONE
                faq_channel_partners_q1_drop_close.visibility = View.VISIBLE
            } else if (faq_channel_partners_q1_drop_down.visibility == View.GONE && faq_channel_partners_q1_answer.visibility == View.VISIBLE) {

                faq_channel_partners_q1_answer.visibility = View.GONE
                faq_channel_partners_q1_drop_down.visibility = View.VISIBLE
                faq_channel_partners_q1_drop_close.visibility = View.GONE

            }
        }

        // Faq channel_partners Q2
        faq_channel_partners_q2_linear_layout.setOnClickListener {
            if (faq_channel_partners_q2_drop_down.visibility == View.VISIBLE && faq_channel_partners_q2_answer.visibility == View.GONE) {

                faq_channel_partners_q2_answer.visibility = View.VISIBLE
                faq_channel_partners_q2_drop_down.visibility = View.GONE
                faq_channel_partners_q2_drop_close.visibility = View.VISIBLE
            } else if (faq_channel_partners_q2_drop_down.visibility == View.GONE && faq_channel_partners_q2_answer.visibility == View.VISIBLE) {

                faq_channel_partners_q2_answer.visibility = View.GONE
                faq_channel_partners_q2_drop_down.visibility = View.VISIBLE
                faq_channel_partners_q2_drop_close.visibility = View.GONE

            }
        }

        // Faq channel_partners Q3
        faq_channel_partners_q3_linear_layout.setOnClickListener {
            if (faq_channel_partners_q3_drop_down.visibility == View.VISIBLE && faq_channel_partners_q3_answer.visibility == View.GONE) {

                faq_channel_partners_q3_answer.visibility = View.VISIBLE
                faq_channel_partners_q3_drop_down.visibility = View.GONE
                faq_channel_partners_q3_drop_close.visibility = View.VISIBLE
            } else if (faq_channel_partners_q3_drop_down.visibility == View.GONE && faq_channel_partners_q3_answer.visibility == View.VISIBLE) {

                faq_channel_partners_q3_answer.visibility = View.GONE
                faq_channel_partners_q3_drop_down.visibility = View.VISIBLE
                faq_channel_partners_q3_drop_close.visibility = View.GONE

            }
        }


        // Faq channel_partners Q4
        faq_channel_partners_q4_linear_layout.setOnClickListener {
            if (faq_channel_partners_q3_drop_down.visibility == View.VISIBLE && faq_channel_partners_q4_answer.visibility == View.GONE) {

                faq_channel_partners_q4_answer.visibility = View.VISIBLE
                faq_channel_partners_q4_drop_down.visibility = View.GONE
                faq_channel_partners_q4_drop_close.visibility = View.VISIBLE
            } else if (faq_channel_partners_q4_drop_down.visibility == View.GONE && faq_channel_partners_q4_answer.visibility == View.VISIBLE) {

                faq_channel_partners_q4_answer.visibility = View.GONE
                faq_channel_partners_q4_drop_down.visibility = View.VISIBLE
                faq_channel_partners_q4_drop_close.visibility = View.GONE

            }
        }


        // CHANNEL PARTNERS CARDVIEW CODE END


        //  RELATIONSHIP MANAGER CARDVIEW CODE START

        faq_relationship_manager_drop_down.setOnClickListener {
            if (faq_relationship_manager_view.visibility == View.GONE && faq_all_qna_relationship_manager.visibility == View.GONE) {
                faq_relationship_manager_view.visibility = View.VISIBLE
                faq_all_qna_relationship_manager.visibility = View.VISIBLE
                faq_relationship_manager_drop_down.visibility = View.GONE
                faq_relationship_manager_drop_close.visibility = View.VISIBLE
                faq_relationship_manager_logo.setImageResource(R.drawable.ic_logo_rm_orange)
                faq_relationship_manager_cardview_heading.setTextColor(Color.parseColor("#D17E19"))

            } else {
                faq_relationship_manager_view.visibility = View.GONE
                faq_all_qna_relationship_manager.visibility = View.GONE
                faq_relationship_manager_drop_down.visibility = View.VISIBLE
                faq_relationship_manager_drop_close.visibility = View.GONE
                faq_relationship_manager_cardview.minimumHeight = 50
                faq_relationship_manager_logo.setImageResource(R.drawable.ic_logo_rm_black)
                faq_relationship_manager_cardview_heading.setTextColor(Color.parseColor("#424646"))


            }
        }

        faq_relationship_manager_drop_close.setOnClickListener {
            if (faq_relationship_manager_view.visibility == View.VISIBLE && faq_all_qna_relationship_manager.visibility == View.VISIBLE) {
                faq_relationship_manager_view.visibility = View.GONE
                faq_all_qna_relationship_manager.visibility = View.GONE
                faq_relationship_manager_drop_down.visibility = View.VISIBLE
                faq_relationship_manager_drop_close.visibility = View.GONE
                faq_relationship_manager_logo.setImageResource(R.drawable.ic_logo_rm_black)
                faq_relationship_manager_cardview_heading.setTextColor(Color.parseColor("#424646"))

            }

        }


        // Faq relationship manager card view
        faq_relationship_manager_cardview_1_linear_layout.setOnClickListener {
            if (faq_relationship_manager_view.visibility == View.GONE && faq_all_qna_relationship_manager.visibility == View.GONE) {

                faq_relationship_manager_view.visibility = View.VISIBLE
                faq_all_qna_relationship_manager.visibility = View.VISIBLE
                faq_relationship_manager_drop_down.visibility = View.GONE
                faq_relationship_manager_drop_close.visibility = View.VISIBLE
                faq_relationship_manager_logo.setImageResource(R.drawable.ic_logo_rm_orange)
                faq_relationship_manager_cardview_heading.setTextColor(Color.parseColor("#D17E19"))
            } else if (faq_relationship_manager_view.visibility == View.VISIBLE && faq_all_qna_relationship_manager.visibility == View.VISIBLE) {

                faq_relationship_manager_view.visibility = View.GONE
                faq_all_qna_relationship_manager.visibility = View.GONE
                faq_relationship_manager_drop_down.visibility = View.VISIBLE
                faq_relationship_manager_drop_close.visibility = View.GONE
                faq_relationship_manager_cardview.minimumHeight = 50
                faq_relationship_manager_logo.setImageResource(R.drawable.ic_logo_rm_black)
                faq_relationship_manager_cardview_heading.setTextColor(Color.parseColor("#424646"))


            }
        }


        // Faq relationship manager Q1
        faq_relationship_manager_q1_linear_layout.setOnClickListener {
            if (faq_relationship_manager_q1_drop_down.visibility == View.VISIBLE && faq_relationship_manager_q1_answer.visibility == View.GONE) {

                faq_relationship_manager_q1_answer.visibility = View.VISIBLE
                faq_relationship_manager_q1_drop_down.visibility = View.GONE
                faq_relationship_manager_q1_drop_close.visibility = View.VISIBLE
            } else if (faq_relationship_manager_q1_drop_down.visibility == View.GONE && faq_relationship_manager_q1_answer.visibility == View.VISIBLE) {

                faq_relationship_manager_q1_answer.visibility = View.GONE
                faq_relationship_manager_q1_drop_down.visibility = View.VISIBLE
                faq_relationship_manager_q1_drop_close.visibility = View.GONE

            }
        }


        //  RELATIONSHIP MANAGER CARDVIEW CODE END


        //  DEAL CARDVIEW CODE START

        faq_deal_drop_down.setOnClickListener {
            if (faq_deal_view.visibility == View.GONE && faq_all_qna_deal.visibility == View.GONE) {
                faq_deal_view.visibility = View.VISIBLE
                faq_all_qna_deal.visibility = View.VISIBLE
                faq_deal_drop_down.visibility = View.GONE
                faq_deal_drop_close.visibility = View.VISIBLE
                faq_deal_logo.setImageResource(R.drawable.ic_logo_deal_orange)
                faq_deal_cardview_heading.setTextColor(Color.parseColor("#D17E19"))

            } else {
                faq_deal_view.visibility = View.GONE
                faq_all_qna_deal.visibility = View.GONE
                faq_deal_drop_down.visibility = View.VISIBLE
                faq_deal_drop_close.visibility = View.GONE
                faq_deal_cardview.minimumHeight = 50
                faq_deal_logo.setImageResource(R.drawable.ic_logo_deal_black)
                faq_deal_cardview_heading.setTextColor(Color.parseColor("#424646"))


            }
        }

        faq_deal_drop_close.setOnClickListener {
            if (faq_deal_view.visibility == View.VISIBLE && faq_all_qna_deal.visibility == View.VISIBLE) {
                faq_deal_view.visibility = View.GONE
                faq_all_qna_deal.visibility = View.GONE
                faq_deal_drop_down.visibility = View.VISIBLE
                faq_deal_drop_close.visibility = View.GONE
                faq_deal_logo.setImageResource(R.drawable.ic_logo_deal_black)
                faq_deal_cardview_heading.setTextColor(Color.parseColor("#424646"))

            }

        }


        // Faq relationship manager card view
        faq_deal_cardview_1_linear_layout.setOnClickListener {
            if (faq_deal_view.visibility == View.GONE && faq_all_qna_deal.visibility == View.GONE) {

                faq_deal_view.visibility = View.VISIBLE
                faq_all_qna_deal.visibility = View.VISIBLE
                faq_deal_drop_down.visibility = View.GONE
                faq_deal_drop_close.visibility = View.VISIBLE
                faq_deal_logo.setImageResource(R.drawable.ic_logo_deal_orange)
                faq_deal_cardview_heading.setTextColor(Color.parseColor("#D17E19"))
            } else if (faq_deal_view.visibility == View.VISIBLE && faq_all_qna_deal.visibility == View.VISIBLE) {

                faq_deal_view.visibility = View.GONE
                faq_all_qna_deal.visibility = View.GONE
                faq_deal_drop_down.visibility = View.VISIBLE
                faq_deal_drop_close.visibility = View.GONE
                faq_deal_cardview.minimumHeight = 50
                faq_deal_logo.setImageResource(R.drawable.ic_logo_deal_black)
                faq_deal_cardview_heading.setTextColor(Color.parseColor("#424646"))


            }
        }


        // Faq relationship manager Q1
        faq_deal_q1_linear_layout.setOnClickListener {
            if (faq_deal_q1_drop_down.visibility == View.VISIBLE && faq_deal_q1_answer.visibility == View.GONE) {

                faq_deal_q1_answer.visibility = View.VISIBLE
                faq_deal_q1_drop_down.visibility = View.GONE
                faq_deal_q1_drop_close.visibility = View.VISIBLE
            } else if (faq_deal_q1_drop_down.visibility == View.GONE && faq_deal_q1_answer.visibility == View.VISIBLE) {

                faq_deal_q1_answer.visibility = View.GONE
                faq_deal_q1_drop_down.visibility = View.VISIBLE
                faq_deal_q1_drop_close.visibility = View.GONE

            }
        }

        //  DEAL CARDVIEW CODE END


        //  SUPPORT CARDVIEW CODE START

        faq_support_drop_down.setOnClickListener {
            if (faq_support_view.visibility == View.GONE && faq_all_qna_support.visibility == View.GONE) {
                faq_support_view.visibility = View.VISIBLE
                faq_all_qna_support.visibility = View.VISIBLE
                faq_support_drop_down.visibility = View.GONE
                faq_support_drop_close.visibility = View.VISIBLE
                faq_support_logo.setImageResource(R.drawable.ic_logo_support_orange)
                faq_support_cardview_heading.setTextColor(Color.parseColor("#D17E19"))

            } else {
                faq_support_view.visibility = View.GONE
                faq_all_qna_support.visibility = View.GONE
                faq_support_drop_down.visibility = View.VISIBLE
                faq_support_drop_close.visibility = View.GONE
                faq_support_cardview.minimumHeight = 50
                faq_support_logo.setImageResource(R.drawable.ic_logo_support_black)
                faq_support_cardview_heading.setTextColor(Color.parseColor("#424646"))


            }
        }

        faq_support_drop_close.setOnClickListener {
            if (faq_support_view.visibility == View.VISIBLE && faq_all_qna_deal.visibility == View.VISIBLE) {
                faq_support_view.visibility = View.GONE
                faq_all_qna_support.visibility = View.GONE
                faq_support_drop_down.visibility = View.VISIBLE
                faq_support_drop_close.visibility = View.GONE
                faq_support_logo.setImageResource(R.drawable.ic_logo_support_black)
                faq_support_cardview_heading.setTextColor(Color.parseColor("#424646"))

            }

        }


        // Faq support card view
        faq_support_cardview_1_linear_layout.setOnClickListener {
            if (faq_support_view.visibility == View.GONE && faq_all_qna_support.visibility == View.GONE) {

                faq_support_view.visibility = View.VISIBLE
                faq_all_qna_support.visibility = View.VISIBLE
                faq_support_drop_down.visibility = View.GONE
                faq_support_drop_close.visibility = View.VISIBLE
                faq_support_logo.setImageResource(R.drawable.ic_logo_support_orange)
                faq_support_cardview_heading.setTextColor(Color.parseColor("#D17E19"))
            } else if (faq_support_view.visibility == View.VISIBLE && faq_all_qna_support.visibility == View.VISIBLE) {

                faq_support_view.visibility = View.GONE
                faq_all_qna_support.visibility = View.GONE
                faq_support_drop_down.visibility = View.VISIBLE
                faq_support_drop_close.visibility = View.GONE
                faq_support_cardview.minimumHeight = 50
                faq_support_logo.setImageResource(R.drawable.ic_logo_support_black)
                faq_support_cardview_heading.setTextColor(Color.parseColor("#424646"))


            }
        }


        // Faq support Q1
        faq_support_q1_linear_layout.setOnClickListener {
            if (faq_support_q1_drop_down.visibility == View.VISIBLE && faq_support_q1_answer.visibility == View.GONE) {

                faq_support_q1_answer.visibility = View.VISIBLE
                faq_support_q1_drop_down.visibility = View.GONE
                faq_support_q1_drop_close.visibility = View.VISIBLE
            } else if (faq_support_q1_drop_down.visibility == View.GONE && faq_support_q1_answer.visibility == View.VISIBLE) {

                faq_support_q1_answer.visibility = View.GONE
                faq_support_q1_drop_down.visibility = View.VISIBLE
                faq_support_q1_drop_close.visibility = View.GONE

            }
        }


        //  SUPPORT CARDVIEW CODE END


        //  LEAD CARDVIEW CODE START

        faq_leads_drop_down.setOnClickListener {
            if (faq_leads_view.visibility == View.GONE && faq_all_qna_leads.visibility == View.GONE) {
                faq_leads_view.visibility = View.VISIBLE
                faq_all_qna_leads.visibility = View.VISIBLE
                faq_leads_drop_down.visibility = View.GONE
                faq_leads_drop_close.visibility = View.VISIBLE
                faq_leads_logo.setImageResource(R.drawable.ic_logo_leads_orange)
                faq_leads_cardview_heading.setTextColor(Color.parseColor("#D17E19"))

            } else {
                faq_leads_view.visibility = View.GONE
                faq_all_qna_leads.visibility = View.GONE
                faq_leads_drop_down.visibility = View.VISIBLE
                faq_leads_drop_close.visibility = View.GONE
                faq_leads_cardview.minimumHeight = 50
                faq_leads_logo.setImageResource(R.drawable.ic_logo_leads_black)
                faq_leads_cardview_heading.setTextColor(Color.parseColor("#424646"))


            }
        }

        faq_leads_drop_close.setOnClickListener {
            if (faq_leads_view.visibility == View.VISIBLE && faq_all_qna_leads.visibility == View.VISIBLE) {
                faq_leads_view.visibility = View.GONE
                faq_all_qna_leads.visibility = View.GONE
                faq_leads_drop_down.visibility = View.VISIBLE
                faq_leads_drop_close.visibility = View.GONE
                faq_leads_logo.setImageResource(R.drawable.ic_logo_leads_black)
                faq_leads_cardview_heading.setTextColor(Color.parseColor("#424646"))

            }

        }


        // Faq lead card view
        faq_leads_cardview_1_linear_layout.setOnClickListener {
            if (faq_leads_view.visibility == View.GONE && faq_all_qna_leads.visibility == View.GONE) {

                faq_leads_view.visibility = View.VISIBLE
                faq_all_qna_leads.visibility = View.VISIBLE
                faq_leads_drop_down.visibility = View.GONE
                faq_leads_drop_close.visibility = View.VISIBLE
                faq_leads_logo.setImageResource(R.drawable.ic_logo_leads_orange)
                faq_leads_cardview_heading.setTextColor(Color.parseColor("#D17E19"))
            } else if (faq_leads_view.visibility == View.VISIBLE && faq_all_qna_leads.visibility == View.VISIBLE) {

                faq_leads_view.visibility = View.GONE
                faq_all_qna_leads.visibility = View.GONE
                faq_leads_drop_down.visibility = View.VISIBLE
                faq_leads_drop_close.visibility = View.GONE
                faq_leads_cardview.minimumHeight = 50
                faq_leads_logo.setImageResource(R.drawable.ic_logo_leads_black)
                faq_leads_cardview_heading.setTextColor(Color.parseColor("#424646"))


            }
        }


        // Faq lead Q1
        faq_leads_q1_linear_layout.setOnClickListener {
            if (faq_leads_q1_drop_down.visibility == View.VISIBLE && faq_leads_q1_answer.visibility == View.GONE) {

                faq_leads_q1_answer.visibility = View.VISIBLE
                faq_leads_q1_drop_down.visibility = View.GONE
                faq_leads_q1_drop_close.visibility = View.VISIBLE
            } else if (faq_leads_q1_drop_down.visibility == View.GONE && faq_leads_q1_answer.visibility == View.VISIBLE) {

                faq_leads_q1_answer.visibility = View.GONE
                faq_leads_q1_drop_down.visibility = View.VISIBLE
                faq_leads_q1_drop_close.visibility = View.GONE

            }
        }


        // Faq lead Q2
        faq_leads_q2_linear_layout.setOnClickListener {
            if (faq_leads_q2_drop_down.visibility == View.VISIBLE && faq_leads_q2_answer.visibility == View.GONE) {

                faq_leads_q2_answer.visibility = View.VISIBLE
                faq_leads_q2_drop_down.visibility = View.GONE
                faq_leads_q2_drop_close.visibility = View.VISIBLE
            } else if (faq_leads_q2_drop_down.visibility == View.GONE && faq_leads_q2_answer.visibility == View.VISIBLE) {

                faq_leads_q2_answer.visibility = View.GONE
                faq_leads_q2_drop_down.visibility = View.VISIBLE
                faq_leads_q2_drop_close.visibility = View.GONE

            }
        }
        //  LEAD CARDVIEW CODE END
    }
    //    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//
//        if(item?.itemId == android.R.id.home){
//            finish()
//        }
//        return super.onOptionsItemSelected(item)
//
//    }
}

class Faq(question:String="Question Not Set", answer:String="Answer Not Set") {

    lateinit var question: String
    lateinit var answer: String

    init{
        this.question = question
        this.answer = answer
    }
}
