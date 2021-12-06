package com.prologic.strains.view.activity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.ecom.prologic.view.fragment.OrdersList
import com.prologic.strains.R
import com.prologic.strains.databinding.ActivityMainBinding
import com.prologic.strains.model.auth.UserResult
import com.prologic.strains.utils.*
import com.prologic.strains.utils.UrlValue.Companion.BASE_URL
import com.prologic.strains.view.fragment.CartItem
import com.prologic.strains.view.fragment.HomePage
import com.prologic.strains.view.fragment.MyProfile
import com.prologic.strains.view.fragment.SearchProduct
import com.prologic.strains.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : FragmentActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        fragmentActivity = this
        if (savedInstanceState == null) {
            replaceFragment(HomePage())
        }
        viewModel.userData.observeForever {
            if (it == null) {
                viewModel.user_lay_visibility.value = View.GONE
                viewModel.login_button_visibility.value = View.VISIBLE
                ordersClick.visibility = View.GONE
                profileClick.visibility = View.GONE
                logoutClick.visibility = View.GONE
            } else {
                viewModel.user_lay_visibility.value = View.VISIBLE
                viewModel.login_button_visibility.value = View.GONE
                ordersClick.visibility = View.VISIBLE
                profileClick.visibility = View.VISIBLE
                logoutClick.visibility = View.VISIBLE
                viewModel.user_name.value = (it.first_name + " " + it.last_name)
                viewModel.user_email.value = "<u> ${it.email}"
            }
        }
        viewModel.errorMessage.observeForever {
            if (it.action == 1) {
                AlertError.show(this, it.message, object : OnDialogCloseListener {
                    override fun onClick() {
                        viewModel.sharedPreference.clearSharedPrefernce()
                        deleteCardData()
                        val intent = Intent(applicationContext, SplashActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                })
            } else {
                AlertError.show(this, it.message)
            }
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

    override fun onResume() {
        super.onResume()
        viewModel.getCustomerById()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        hideSoftKeyBoard(this)
    }

    fun loginPage() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, 1001)
    }

    fun getUserData(): UserResult? {
        return viewModel.userData.value
    }


    fun setValue() {
        viewModel.setValue()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            fragmentActivity = this
            if (resultCode == RESULT_OK) {
                setValue()
            }
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
        } else if (view == searchClick) {
            addFragment(SearchProduct())
        } else if (view == ordersClick) {
            if (viewModel.userData != null) {
                addFragment(OrdersList())
            } else {
                loginPage()
            }

        } else if (view == logoutClick) {
            logoutDailog()
        } else if (view == profileClick) {
            if (viewModel.userData != null) {
                addFragment(MyProfile())
            } else {
                loginPage()
            }
        } else if (view == aboutUsClick) {
            val intent = Intent(fragmentActivity, MyWebView::class.java)
            intent.putExtra("title", "About Us")
            intent.putExtra("url", BASE_URL + "about-us")
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

