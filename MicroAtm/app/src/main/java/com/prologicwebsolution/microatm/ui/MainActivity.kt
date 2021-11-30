package com.prologicwebsolution.microatm.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.ui.loginUi.LoginActivity


class MainActivity : AppCompatActivity() {


 /*   override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val currentNightMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {} // Night mode is not active, we're using the light theme
            Configuration.UI_MODE_NIGHT_YES -> {} // Night mode is active, we're using dark theme
        }
    }*/

    lateinit var toolbar: Toolbar
    var menu: Menu? = null
    private lateinit var navController: NavController
//    private lateinit var appBarConfig: AppBarConfiguration

    @SuppressLint("ResourceAsColor", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

//         appBarConfig = AppBarConfiguration(
//                setOf(
//                        R.id.dashboardFragment
//                )
//        )
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

// use for hide menu and show menu which fragment we want to show
        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            val logout_button = menu?.findItem(R.id.logout_menu)
            val withdrawlHistory_button = menu?.findItem(R.id.withdrawl_history_menu)

            when (nd.id) {
                R.id.dashboardFragment -> {
                    logout_button?.setVisible(true)
                    withdrawlHistory_button?.setVisible(false)
                }

                R.id.transactionDetailFragment -> {
                    logout_button?.setVisible(false)
                    withdrawlHistory_button?.setVisible(false)
                }


                R.id.commissionFragment -> {
                    logout_button?.setVisible(false)
                    withdrawlHistory_button?.setVisible(false)
                }

                R.id.withdrawlStatusFragment -> {
                    logout_button?.setVisible(false)
                    withdrawlHistory_button?.setVisible(false)
                }

                R.id.connectMicroAtmFragment -> {
                    logout_button?.setVisible(false)
                    withdrawlHistory_button?.setVisible(false)
                }

                R.id.withdrawlFragment -> {
                    logout_button?.setVisible(false)
                    withdrawlHistory_button?.setVisible(false)
                }
                R.id.balanceEnquiryAndWithdrawFragment -> {
                    logout_button?.setVisible(false)
                    withdrawlHistory_button?.setVisible(false)
                }
            }
        }
    }
// this code use for hang mode by fluctuate color
    /*override fun onResume() {
        super.onResume()
        val isNightTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        // 4
        when (isNightTheme)
        {
            Configuration.UI_MODE_NIGHT_YES ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Configuration.UI_MODE_NIGHT_NO ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }*/

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
            } R.id.withdrawl_history_menu ->{
                navController.navigate(R.id.balanceEnquiryAndWithdrawFragment)
            }

        }
        return super.onOptionsItemSelected(item)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()

    }

}