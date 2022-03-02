package com.prologicwebsolution.microatm.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.databinding.ActivityMainBinding
import com.prologicwebsolution.microatm.ui.home.HomeFragment
import com.prologicwebsolution.microatm.util.fragmentActivity
import com.prologicwebsolution.microatm.util.hideSoftKeyBoard
import com.prologicwebsolution.microatm.util.replaceFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        fragmentActivity = this
        back_button.setOnClickListener { onBackPressed() }
        replaceFragment(HomeFragment())
    }

/*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.logout_menu -> {
             val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            R.id.withdrawl_history_menu -> {
                addFragment(BalanceEnquiryAndWithdrawFragment())
            }

        }
        return super.onOptionsItemSelected(item)
    }
*/

    fun setHeader(header: Boolean, isBack: Boolean, title: String) {
        viewModel.setHeader(header, isBack, title)
    }

    fun setHideHeader() {
        viewModel.setHideHeader()
    }

    override fun onBackPressed() {
        hideSoftKeyBoard()
        super.onBackPressed()

    }

}