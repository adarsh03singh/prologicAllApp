package com.prologicwebsolution.microatm.ui.dashboared

import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.databinding.FragmentDashboardBinding
import kotlinx.android.synthetic.main.fragment_dashboard.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener


class DashboardFragment() : Fragment(){


    private var backPressedTime:Long = 0
    lateinit var backToast:Toast
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val REQUEST_ENABLE_BT = 0
    private val REQUEST_DISCOVER_BT = 1
    var mBlueAdapter: BluetoothAdapter? = null
    lateinit var viewmodel: DashboardViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val binding: FragmentDashboardBinding = FragmentDashboardBinding.bind(view)
        viewmodel = ViewModelProviders.of(this.requireActivity()).get(DashboardViewModel::class.java)
        binding.dashboardViewModel = viewmodel
        binding.lifecycleOwner = this


        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.walletBalanceApiCall(view)

        sw_refresh.setOnRefreshListener {
            sw_refresh.isRefreshing = false
        }

        //adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter()

        val imgeTransactionButton = view.findViewById<ImageView>(R.id.imageViewTransaction)
        val imgeCommissionButton = view.findViewById<ImageView>(R.id.imageViewCommission)
        val imgeWithdrawlStatusButton = view.findViewById<ImageView>(R.id.imageViewWithdrawlStatus)
        val buttonWithdraw = view.findViewById<Button>(R.id.buttonWithdraw)

        imgeTransactionButton.setOnClickListener {
            view.findNavController().navigate(R.id.transactionDetailFragment)
        }

        imgeCommissionButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_dashboardFragment_to_commissionFragment)
        }

        imgeWithdrawlStatusButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_dashboardFragment_to_withdrawlStatusFragment)
        }

        buttonWithdraw.setOnClickListener {
            view.findNavController().navigate(R.id.action_dashboardFragment_to_withdrawlFragment)
        }

        microATMImageButton.setOnClickListener {

            //check if bluetooth is available or not



            if (!mBlueAdapter!!.isEnabled) {
                showNotification(view, "Bluetooth is not available")
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_ENABLE_BT)
            } else {
                showNotification(view, "Bluetooth is available")
                view.findNavController().navigate(R.id.action_dashboardFragment_to_connectMicroAtmFragment)
            }
        }
    }



    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    private fun showNotification(view: View,msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }


    fun showDialog(view: View){
      val  message: String? = null
        val dialogBuilder = AlertDialog.Builder(view.context)

        // set message of alert dialog
        dialogBuilder.setMessage("Microatm App wants to turn on Blutooth")
                // if the dialog is cancelable
                .setCancelable(false)
                .setPositiveButton("Deny", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
                })
                // negative button text and action
                .setNegativeButton("Ok", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
                    if (!mBlueAdapter!!.isEnabled()) {
                        //intent to on bluetooth
                        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        startActivityForResult(intent, REQUEST_ENABLE_BT)
                    }

                })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
//        alert.setTitle("Oops!")
        // show alert dialog
        alert.show()
    }

}