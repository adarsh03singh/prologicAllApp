package com.csestateconnect.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.csestateconnect.R
import com.csestateconnect.databinding.ActivityHomeBinding
import com.csestateconnect.db.data.favouriteProject.GetFavouriteProjectsEntity
import com.csestateconnect.ui.GenerateOtpActivity
import com.csestateconnect.utils.ConnectivityReceiver
import com.csestateconnect.viewmodel.HomeViewModel
import com.csestateconnect.viewmodel.LeadsViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class HomeActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

   val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    private var sharePath = "no"
    private var snackbar: Snackbar? = null
    lateinit var viewModel: HomeViewModel
    lateinit var leadViewModel: LeadsViewModel
//    lateinit var drawer: DrawerLayout
    lateinit var toolbar: Toolbar
    var menu: Menu? = null
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration
    var headerImgView: ImageView? = null

    lateinit var counterText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityHomeBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_home
        )

        val appLinkIntent = intent
        val appLinkAction = appLinkIntent.action
        val appLinkData = appLinkIntent.data

        Log.d("HomeActivity", "applinkintent: $appLinkIntent appLinkAction: $appLinkAction appLinkData: $appLinkData")


        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                Log.d("HomeActivity", "Key: $key Value: $value")
            }
        }

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        leadViewModel = ViewModelProviders.of(this).get(LeadsViewModel::class.java)

        binding.viewmodel = viewModel
        binding.mContext = this
        binding.lifecycleOwner = this

//this code use for fileProvide When we use screenShot of this activity
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()

       toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment)


        //        val param = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//            RelativeLayout.LayoutParams.MATCH_PARENT)
//        param.setMargins(0, 0, 0, 0)//pass int values for left,top,right,bottom

//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            when (destination.id) {
//                R.id.projectHorizontalImagesFragment ->
//                    toolbar.visibility = View.GONE
//                else -> {
//                    toolbar.visibility = View.VISIBLE
//                }
//            }
//
//        }

        // For bottom navigation
        bottom_bar.setupWithNavController(navController)

        // For navigation drawer`
        appBarConfig = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_projects,
                R.id.navigation_assigned_leads,
               /* R.id.navigation_comission,*/
                R.id.navigation_get_touch,
                R.id.lead2TabFragment
            ), drawer_layout
        )

        setupActionBarWithNavController(navController, appBarConfig)
        navViewDrawer.setupWithNavController(navController)

        header_view.setOnClickListener {
            navController.navigate(R.id.header_view)
            closeDrawer()
        }

        nav_bcp.setOnClickListener {
            navController.navigate(R.id.nav_bcp)
            closeDrawer()
        }

        nav_notify.setOnClickListener {
            navController.navigate(R.id.nav_notification)
            closeDrawer()
        }

        nav_favorites.setOnClickListener {
            navController.navigate(R.id.nav_favorites)
            closeDrawer()
        }

        nav_clients.setOnClickListener {
            navController.navigate(R.id.client_listFragment)
            closeDrawer()
        }

        nav_events.setOnClickListener {
            navController.navigate(R.id.horizontalCalenderFragment)
            closeDrawer()
        }

        nav_reimbursements.setOnClickListener {
            navController.navigate(R.id.reimbursementListFragment)
            closeDrawer()
        }

        nav_faq.setOnClickListener {
            navController.navigate(R.id.nav_faq)
            closeDrawer()
        }

        nav_get_touch.setOnClickListener {
            navController.navigate(R.id.navigation_get_touch)
            closeDrawer()
        }

        nav_about_us.setOnClickListener {
            navController.navigate(R.id.nav_about_us)
            closeDrawer()
        }

        nav_terms_conditions.setOnClickListener {
            navController.navigate(R.id.nav_terms_conditions)
            closeDrawer()
        }

        nav_privacy_policy.setOnClickListener {
            navController.navigate(R.id.nav_privacy_policy)
            closeDrawer()
        }

        nav_log_out.setOnClickListener {
         val canLogout = viewModel.logOut(application)
            if (canLogout != null) {
                if (canLogout) {
                    val intent = Intent(applicationContext, GenerateOtpActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                    finish()
                } else {
                    Toast.makeText(this.getApplication(), "Please connect to Internet and try again", Toast.LENGTH_SHORT).show()
                }
            }

        }

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
//            if (nd.id == R.id.nav_get_touch){
//                concernTicket.visibility = View.VISIBLE
//            }

            if (nd.id == R.id.navigation_home || nd.id == R.id.navigation_projects || /*nd.id == R.id.navigation_comission*/
                nd.id == R.id.navigation_get_touch || nd.id == R.id.navigation_assigned_leads || nd.id == R.id.lead2TabFragment
            ) {
                bottom_bar.visibility = View.VISIBLE
                floating_button.visibility = View.VISIBLE
            } else {
                bottom_bar.visibility = View.GONE
                floating_button.visibility = View.GONE
            }

            val filter = menu?.findItem(R.id.action_filter)
            val search = menu?.findItem(R.id.search_menu)
            val newLead = menu?.findItem(R.id.create_new_lead)
            val assignLead = menu?.findItem(R.id.get_assign_lead)
            val share_project = menu?.findItem(R.id.action_share)
            val fav_project = menu?.findItem(R.id.action_favorite)
            val broker_request = menu?.findItem(R.id.broker_request)

            when (nd.id) {
                R.id.navigation_home -> {
                    search?.setVisible(true)
                    filter?.setVisible(false)
                    newLead?.setVisible(false)
                    assignLead?.setVisible(false)
                    share_project?.setVisible(false)
                    fav_project?.setVisible(false)
                    broker_request?.setVisible(false)
                }
                R.id.navigation_projects -> {
                    search?.setVisible(true)
                    filter?.setVisible(true)
                    newLead?.setVisible(false)
                    assignLead?.setVisible(false)
                    share_project?.setVisible(false)
                    fav_project?.setVisible(false)
                    broker_request?.setVisible(false)
                }
                R.id.navigation_assigned_leads -> {
                    search?.setVisible(false)
                    newLead?.setVisible(false)
                    assignLead?.setVisible(true)
                    filter?.setVisible(true)
                    share_project?.setVisible(false)
                    fav_project?.setVisible(false)
                    broker_request?.setVisible(false)
                }
                R.id.lead2TabFragment -> {
                    newLead?.setVisible(true)
                    assignLead?.setVisible(false)
                    search?.setVisible(false)
                    filter?.setVisible(true)
                    share_project?.setVisible(false)
                    fav_project?.setVisible(false)
                    broker_request?.setVisible(false)
                }

               /* R.id.navigation_comission -> {
                    search?.setVisible(false)
                    filter?.setVisible(true)
                    newLead?.setVisible(false)
                    assignLead?.setVisible(false)
                    share_project?.setVisible(false)
                    fav_project?.setVisible(false)
                    broker_request?.setVisible(false)
                }*/
                R.id.projectDetailFragment -> {
                    search?.setVisible(false)
                    filter?.setVisible(false)
                    newLead?.setVisible(false)
                    assignLead?.setVisible(false)
                    share_project?.setVisible(true)
                    fav_project?.setVisible(true)
                    broker_request?.setVisible(false)

//                    viewModel.callGetFavouriteProjectAPIAndStore()
                    val sharedPref: SharedPreferences = getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
                    val previousId = sharedPref.getString("projectId", "")
                    val favProIdId = sharedPref.getString("favProjeId", "")

                    var projId: String? = null
                    var listLiveData :LiveData<List<GetFavouriteProjectsEntity>> = viewModel.getFavouriteProjectsDatabase()
                    listLiveData.observe(this, object:Observer<List<GetFavouriteProjectsEntity>> {
                        override fun onChanged(@Nullable favProjectsEntities:List<GetFavouriteProjectsEntity>) {

                            try {
                                val favData = favProjectsEntities.get(0).favouriteProjects!!
                                favData.forEach {
                                    val Id = it!!.id
                                    if (Id.toString() == previousId) {
                                        projId = Id.toString()

                                    }
                                }
                                if (favProIdId == previousId) {
                                    fav_project!!.setIcon(R.drawable.ic_bookmark_selected)

                                }else if (projId != null) {
                                    fav_project!!.setIcon(R.drawable.ic_bookmark_selected)

                                }else
                                    fav_project!!.setIcon(R.drawable.ic_bookmark)


                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                                listLiveData.removeObservers(this@HomeActivity)
                        }
                    })

                    }
                R.id.brokersFragment -> {
                    search?.setVisible(false)
                    filter?.setVisible(false)
                    newLead?.setVisible(false)
                    assignLead?.setVisible(false)
                    share_project?.setVisible(false)
                    fav_project?.setVisible(false)
                    broker_request?.setVisible(true)
                }

                else -> {
                    search?.setVisible(false)
                    filter?.setVisible(false)
                    newLead?.setVisible(false)
                    assignLead?.setVisible(false)
                    share_project?.setVisible(false)
                    fav_project?.setVisible(false)
                    broker_request?.setVisible(false)
                }
            }
        }

       /* floating_button.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel: 9694783673")
            startActivity(intent)

        }*/
        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
//        setImage()
//
//        concernTicket.setOnClickListener {
//            navController.navigate(R.id.action_nav_get_touch_to_concernTicketsListFragment)
//        }
    }

    override fun onPostResume() {
        super.onPostResume()
        viewModel.getChannelPartnersData(applicationContext)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this

    }

    private fun showNetworkMessage(isConnected: Boolean) {

        if (!isConnected) {
            snackBar()

        } else {
            snackbar?.dismiss()

        }
    }

//    fun setImage(){
//        Coroutines.main{
//            val imgval = viewModel.headerImge
//            if(imgval != null) {
////                headerImgView!!.setBackground(null)
//                Glide.with(this).load(imgval).into(headerImgView!!)
//            }
//        }
//    }

    override fun onSupportNavigateUp(): Boolean {
//        findNavController().popBackStack(R.id.navigation_home, false)
//        navController.popBackStack(R.id.navigation_home, false)
        return navController.navigateUp(appBarConfig)
                || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val destId = navController.currentDestination?.id

        if (destId == R.id.lead2TabFragment || destId == R.id.navigation_assigned_leads || destId == R.id.header_view ||
                destId == R.id.nav_notification || destId == R.id.nav_favorites || destId == R.id.nav_faq ||
            destId == R.id.navigation_get_touch || destId == R.id.nav_about_us || destId == R.id.nav_terms_conditions ||
            destId == R.id.nav_privacy_policy || destId == R.id.client_listFragment|| destId == R.id.horizontalCalenderFragment
          || destId == R.id.reimbursementListFragment){
            navController.popBackStack(R.id.navigation_home, false)
        }
        else if (destId == R.id.dealDetailsFragment) {

            val a = navController.popBackStack(R.id.navigation_comission, false)
            if (!a) navController.popBackStack(R.id.navigation_home,false)

        }
        else if (destId == R.id.clients_detailFragment) {

            val a = navController.popBackStack(R.id.client_listFragment, false)
            if (!a) navController.popBackStack(R.id.navigation_home,false)
        }

        else if (destId == R.id.reimbursementDetailFragment) {

            val a = navController.popBackStack(R.id.reimbursementListFragment, false)
            if (!a) navController.popBackStack(R.id.navigation_home,false)
        }

        else if (destId == R.id.navigation_projects) {

           // To clear cityData from sharedPref

            val preferences = getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
            val editor = preferences.edit()
            editor.remove("cityName")
            editor.remove("applyButtonEnabled")
            editor.apply()
            super.onBackPressed()
        }
         else {
            super.onBackPressed()
        }

//        if(destId == R.id.projectImagesFragment ){
//            navController.popBackStack(R.id.projectDetailFragment, true)
//        } else {
//            super.onBackPressed()
//        }
    }

    fun closeDrawer() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.main_menu, menu)

        val badgeLayout: ConstraintLayout =
            menu.findItem(R.id.broker_request).actionView as ConstraintLayout

        counterText = badgeLayout.findViewById(R.id.menu_badge) as TextView

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.create_new_lead -> {
                navController.navigate(R.id.createLeadFragment)
                return true
            }

            R.id.get_assign_lead -> {
                snackbar = Snackbar.make(drawer_layout, "Please wait", Snackbar.LENGTH_LONG)
                val view = snackbar!!.view
                leadViewModel.postLeadRequestButton(view)

                return true
            }

            R.id.search_menu -> {
                navController.navigate(R.id.searchFragment)
                return true
            }

            R.id.action_share -> {

                screenshoot()

                return true
            }
            R.id.action_favorite -> {

                   if(isNetworkConnected(applicationContext)) {
                       //get project id from
                       val sharedPref: SharedPreferences =
                           getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                       val previousId = sharedPref.getString("projectId", "")
                       var projId: String? = null


                       var listLiveData: LiveData<List<GetFavouriteProjectsEntity>> =
                           viewModel.getFavouriteProjectsDatabase()
                       listLiveData.observe(
                           this,
                           object : Observer<List<GetFavouriteProjectsEntity>> {
                               override fun onChanged(@Nullable favProjectsEntities: List<GetFavouriteProjectsEntity>) {

                                   try {
                                       val favData = favProjectsEntities.get(0).favouriteProjects!!
                                       favData.forEach {
                                           val id = it!!.id
                                           if (id.toString() == previousId) {
                                               projId = id.toString()
                                           }
                                       }

                                       if (projId != null) {
                                           //remove favourite projects and change icon
                                           item.icon = getDrawable(R.drawable.ic_bookmark)
                                           viewModel.callRemoveFavouriteProjectsApi(previousId.toInt())
                                           viewModel.callGetFavouriteProjectAPIAndStore()

                                           val editor = sharedPref.edit()
                                           editor.remove("favProjeId").apply()

                                           if (viewModel.checkResponseRemove.value == true) {
                                               Snackbar.make(
                                                   getWindow().getDecorView(),
                                                   "project has been removed.",
                                                   Snackbar.LENGTH_LONG
                                               ).show()
                                           }

                                       } else {
                                           //add favourite projects and change icon
                                           item.icon = getDrawable(R.drawable.ic_bookmark_selected)
                                           viewModel.callAddFavouriteProjectsApi(previousId.toInt())
                                           viewModel.callGetFavouriteProjectAPIAndStore()
                                           if (viewModel.checkResponseRemove.value == true) {
                                               Snackbar.make(
                                                   getWindow().getDecorView(),
                                                   "project has been added.",
                                                   Snackbar.LENGTH_LONG
                                               ).show()
                                           }

                                           val editor = sharedPref.edit()
                                           editor.putString("favProjeId", previousId.toString())
                                           editor.apply()

                                       }
                                   } catch (e: Exception) {
                                       e.printStackTrace()
                                   }
                                   listLiveData.removeObservers(this@HomeActivity)
                               }

                           })

                   }else{
                       Snackbar.make(
                           getWindow().getDecorView(),
                           "No internet found.",
                           Snackbar.LENGTH_LONG
                       ).show()
                   }
                return true
            }

            R.id.broker_request -> {
                //  change broker request icon number here
                counterText.setText("1")
            }

            R.id.action_filter -> {
               /* if (navController.currentDestination?.id == R.id.navigation_comission){
                    navController.navigate(R.id.dealFilterFragment)
                }*/
                 if(navController.currentDestination?.id == R.id.navigation_projects){
                    navController.navigate(R.id.projectFilterFragment)
                }
                else if(navController.currentDestination?.id == R.id.navigation_assigned_leads){
                    val bundle = bundleOf("leadFilter" to "assigned")
                    navController.navigate(R.id.leadFilterFragment, bundle)
                }
                else {
                    val bundle = bundleOf("leadFilter" to "submitted")
                    navController.navigate(R.id.leadFilterFragment, bundle)
                }
                return true
            }
//            R.id.action_tickets -> {
//
//            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun snackBar(){
        snackbar = Snackbar.make(drawer_layout, "You are offline", Snackbar.LENGTH_LONG)
        snackbar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
        var view = snackbar!!.getView()
        view.setBackgroundColor(resources.getColor(R.color.colorchery))
        snackbar?.show()
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    private fun screenshoot() {



        if(checkPermissions() == true){
        val date = Date()
        val now = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", date)
        val mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg"
//        val filename = Environment.getExternalStorageDirectory() + "/ScreenShooter/" + now + ".jpg"
        val root = getWindow().getDecorView()
        root.setDrawingCacheEnabled(true)
        val bitmap = Bitmap.createBitmap(root.getDrawingCache())
        root.setDrawingCacheEnabled(false)
        val file = File(mPath)
        file.getParentFile().mkdirs()
        try
        {
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            val uri = Uri.fromFile(file)
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("image/*")
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(intent)
        }
        catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
    }
    }


    private fun checkPermissions(): Boolean {
        val camera = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )
        val wtite =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val read =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        val listPermissionsNeeded = java.util.ArrayList<String>()
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

}
