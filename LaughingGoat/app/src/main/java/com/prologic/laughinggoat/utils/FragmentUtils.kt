package com.prologic.laughinggoat.utils

import androidx.fragment.app.*
import com.prologic.laughinggoat.R
import com.prologic.laughinggoat.view.activity.MainActivity

var fragmentActivity: MainActivity? = null
var shooterFragment: Fragment? = null

fun getAppFragmentManager(): FragmentManager {
    return fragmentActivity!!.supportFragmentManager
}

fun addFragment(targetFragment: Fragment) {
    val transaction = getAppFragmentManager().beginTransaction()
    transaction.setCustomAnimations(
        R.anim.enter,
        R.anim.exit,
        R.anim.pop_enter,
        R.anim.pop_exit
    )
    transaction.hide(shooterFragment!!)
    transaction.addToBackStack(targetFragment.tag)
    transaction.add(R.id.main_content, targetFragment, targetFragment.tag)
    transaction.commit()
}

fun addFragmentBottomTop(targetFragment: Fragment) {
    val transaction = getAppFragmentManager().beginTransaction()
    transaction.setCustomAnimations(
        R.anim.slide_in_up,
       0,
        0,
        R.anim.slide_out_up
    )
    transaction.hide(shooterFragment!!)
    transaction.addToBackStack(targetFragment.tag)
    transaction.add(R.id.main_content, targetFragment, targetFragment.tag)
    transaction.commit()
}

fun addFragmentFadeAnim(targetFragment: Fragment) {
    val transaction = getAppFragmentManager().beginTransaction()
    transaction.setCustomAnimations(
        R.anim.enter,
        R.anim.exit,
        R.anim.pop_enter,
        R.anim.pop_exit
    )
    transaction.hide(shooterFragment!!)
    transaction.addToBackStack(targetFragment.tag)
    transaction.add(R.id.main_content, targetFragment, targetFragment.tag)
    transaction.commit()
}

fun replaceFragment(fragment: Fragment) {
    val transaction = getAppFragmentManager().beginTransaction()
    transaction.replace(R.id.main_content, fragment)
    transaction.commit()
}

fun clearFragments() {
    val fragmentManager = getAppFragmentManager()
    while (fragmentManager.getBackStackEntryCount() > 0) {
        fragmentManager.popBackStackImmediate()
    }
}