package com.csestateconnect.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.csestateconnect.ui.home.lead_Frags.CsAssignLeadFragment
import com.csestateconnect.ui.home.lead_Frags.MyLeadsFragment


class LeadHomeAdapter(val context: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> { return CsAssignLeadFragment()
            }
            1 -> { return MyLeadsFragment()
            }
            else -> return CsAssignLeadFragment()
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}