package com.prologic.strains.utils

import android.os.Bundle
import androidx.fragment.app.*
import com.prologic.strains.R
import com.prologic.strains.view.activity.MainActivity

var fragmentActivity: FragmentActivity? = null
var shooterFragment: Fragment? = null

fun getAppFragmentManager(): FragmentManager {
    return fragmentActivity!!.supportFragmentManager
}
fun addFragment(targetFragment: Fragment, bundle: Bundle) {
    targetFragment.arguments=bundle
    addFragment(targetFragment)
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
fun setOnBackResult(key: String, bundle: Bundle) {
    getAppFragmentManager().setFragmentResult(key, bundle)
    getAppFragmentManager().popBackStack()
}