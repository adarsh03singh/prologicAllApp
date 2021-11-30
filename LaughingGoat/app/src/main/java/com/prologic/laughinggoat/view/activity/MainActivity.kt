package com.prologic.laughinggoat.view.activity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ecom.prologic.view.fragment.OrdersList
import com.prologic.laughinggoat.R
import com.prologic.laughinggoat.databinding.MainActivityBinding
import com.prologic.laughinggoat.model.auth.UserResult
import com.prologic.laughinggoat.utils.*
import com.prologic.laughinggoat.utils.UrlValue.Companion.BASE_URL
import com.prologic.laughinggoat.view.fragment.*
import com.prologic.laughinggoat.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : FragmentActivity() {
    lateinit var binding: MainActivityBinding
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        viewModel.binding = binding
        viewModel.setValue()
        fragmentActivity = this
        if (savedInstanceState == null) {
            replaceFragment(HomePage())
        }

        back_button.setOnClickListener { onBackPressed() }
        menu_button.setOnClickListener { drawerLayout.openDrawer(Gravity.LEFT) }

        loginClick.setOnClickListener { callMenu(loginClick) }
        profileClick.setOnClickListener { callMenu(profileClick) }
        ordersClick.setOnClickListener { callMenu(ordersClick) }
        logoutClick.setOnClickListener { callMenu(logoutClick) }

        aboutUsClick.setOnClickListener { callMenu(aboutUsClick) }
        contactClick.setOnClickListener { callMenu(contactClick) }
        searchClick.setOnClickListener { callMenu(searchClick) }

        cartClick.setOnClickListener {
            addFragmentBottomTop(CartItem())

        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        hideSoftKeyBoard(fragmentActivity as Activity)
    }
    fun loginPage() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, 1001)
    }

    fun getUserData(): UserResult? {
        return viewModel.user_data
    }

    fun setValue() {
        viewModel.setValue()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            setValue()
        }
    }

    fun callMenu(view: View) {
        drawerLayout.closeDrawers()
        Handler().postDelayed({
            menuIntent(view)
        }, 300)
    }

    private fun menuIntent(view: View) {
        if (view == loginClick) {
            loginPage()
        } else if (view == ordersClick) {
            if (viewModel.user_data != null) {
                addFragment(OrdersList())
            } else {
                loginPage()
            }

        } else if (view == logoutClick) {
            logoutDailog()
        } else if (view == profileClick) {
            if (viewModel.user_data != null) {
                addFragment(MyProfile())
            } else {
                loginPage()
            }
        } else if (view == aboutUsClick) {
            val intent = Intent(fragmentActivity, MyWebView::class.java)
            intent.putExtra("title", "About Us")
            intent.putExtra("url", BASE_URL + "about")
            fragmentActivity?.startActivity(intent)
        } else if (view == contactClick) {
            val intent = Intent(fragmentActivity, MyWebView::class.java)
            intent.putExtra("title", "Contact Us")
            intent.putExtra("url", BASE_URL + "contact-us")
            fragmentActivity?.startActivity(intent)
        }
    }

/*    fun getActivity(): MainActivity {
        return mainActivity!!
    }*/

    fun setHeader(
        title: String,
        isBack: Boolean,
        isCart: Boolean,
        isHeader: Boolean,
        isSearch: Boolean
    ) {
        viewModel.title_text.value = title
        if (isHeader) {
            viewModel.header_lay.postValue(View.VISIBLE)
        } else {
            viewModel.header_lay.postValue(View.GONE)
        }
        if (isSearch) {
            viewModel.search_visibility.postValue(View.VISIBLE)
        } else {
            viewModel.search_visibility.postValue(View.GONE)
        }

        if (isBack) {
            viewModel.menu_visibility.value = View.GONE
            viewModel.back_visibility.value = View.VISIBLE
        } else {
            viewModel.menu_visibility.value = View.VISIBLE
            viewModel.back_visibility.value = View.GONE
        }
        if (isCart) {
            viewModel.my_cart_visibility.value = View.VISIBLE
        } else {
            viewModel.my_cart_visibility.value = View.GONE
        }
    }

/*    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }*/


    private fun logoutDailog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawableResource(R.color.trans)
        dialog.window!!.setBackgroundDrawableResource(R.color.trans)
        val metrics = DisplayMetrics()
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(metrics)
        dialog.setContentView(R.layout.logout_alert)
        dialog.setCancelable(false)
        val layout = dialog.findViewById<FrameLayout>(R.id.layout)
        val params = layout.layoutParams
        params.width = metrics.widthPixels - metrics.widthPixels * 10 / 100
        layout.layoutParams = params
        val topView = dialog.findViewById<ImageView>(R.id.topView)
        val msg = dialog.findViewById<TextView>(R.id.msg)
        val noClick = dialog.findViewById<Button>(R.id.noClick)
        val yesClick = dialog.findViewById<Button>(R.id.yesClick)
        msg.text = getHtmlSpanned("Are you sure you want to <b>logout</b>?")
        noClick.setOnClickListener {
            dialog.dismiss()
        }
        yesClick.setOnClickListener {
            viewModel.sharedPreference.clearSharedPrefernce()
            deleteCardData()
            dialog.dismiss()
            DialogLoading.show(this, "Please wait . . . . ")
            Handler().postDelayed({
                DialogLoading.hide()
                val intent = Intent(applicationContext, SplashActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }, 2000)
        }

        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        topView.startAnimation(shake)
        dialog.show()

    }

}

