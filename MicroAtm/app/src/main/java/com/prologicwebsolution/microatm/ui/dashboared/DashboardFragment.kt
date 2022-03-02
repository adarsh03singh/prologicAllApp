package com.prologicwebsolution.microatm.ui.dashboared

import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.databinding.FragmentDashboardBinding
import com.prologicwebsolution.microatm.ui.MainActivity
import com.prologicwebsolution.microatm.ui.commission.CommissionFragment
import com.prologicwebsolution.microatm.ui.connectMicroAtm.ConnectMicroAtmFragment
import com.prologicwebsolution.microatm.ui.loginUi.LoginActivity
import com.prologicwebsolution.microatm.ui.transaction.TransactionDetailFragment
import com.prologicwebsolution.microatm.ui.withdrawl.WithdrawlFragment
import com.prologicwebsolution.microatm.ui.withdrawlStatus.WithdrawlStatusFragment
import com.prologicwebsolution.microatm.util.addFragment
import com.prologicwebsolution.microatm.util.getAppFragmentManager
import com.prologicwebsolution.microatm.util.shooterFragment
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment() : Fragment(){

    private val REQUEST_ENABLE_BT = 0
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
        setHeader()
        viewmodel.walletBalanceApiCall(view)

        back.setOnClickListener {
            getAppFragmentManager().popBackStack()
        }

        logOut_button.setOnClickListener {
           viewmodel.logout(view)
        }
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
            addFragment(TransactionDetailFragment())
        }

        imgeCommissionButton.setOnClickListener {
            addFragment(CommissionFragment())
        }

        imgeWithdrawlStatusButton.setOnClickListener {
            addFragment(WithdrawlStatusFragment())
        }

        buttonWithdraw.setOnClickListener {
            addFragment(WithdrawlFragment())
        }

        microATMImageButton.setOnClickListener {

            //check if bluetooth is available or not
            if (!mBlueAdapter!!.isEnabled) {
                showNotification(view, "Bluetooth is not available")
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_ENABLE_BT)
            } else {
                showNotification(view, "Bluetooth is available")
                addFragment(ConnectMicroAtmFragment())
            }
        }
    }

    private fun showNotification(view: View, msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden)
            setHeader()
    }

    private fun setHeader() {
        shooterFragment = this
        (activity as MainActivity).setHideHeader()
    }

    fun showDialog(view: View) {
        val dialogBuilder = AlertDialog.Builder(view.context)
        dialogBuilder.setMessage("Microatm App wants to turn on Blutooth")
            // if the dialog is cancelable
            .setCancelable(false)
            .setPositiveButton("Deny", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
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

    fun logOut(view: View) {
        val activity = view.context as Activity
        val intent = Intent(view.context, LoginActivity::class.java)
        startActivity(intent)
        activity.finish()
    }

}