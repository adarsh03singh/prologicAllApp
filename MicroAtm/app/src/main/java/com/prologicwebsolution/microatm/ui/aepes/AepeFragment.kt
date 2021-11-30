package com.prologicwebsolution.microatm.ui.aepes


import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.aeps_utils.AddTextWatcher
import com.prologicwebsolution.microatm.aeps_utils.FormXML
import com.prologicwebsolution.microatm.aeps_utils.SplitXML
import com.prologicwebsolution.microatm.databinding.FragmentAepesBinding
import com.prologicwebsolution.microatm.ui.MainActivity
import com.prologicwebsolution.microatm.ui.aeps_bank.BankListActivity
import com.prologicwebsolution.microatm.util.Constants
import kotlinx.android.synthetic.main.fragment_aepes.*
import java.security.AccessController
import java.util.*


class AepsFragment : Fragment() {

    var CUSTOM_ACTION_INFO_FINGERPRINT = "in.gov.uidai.rdservice.fp.INFO"
    var CUSTOM_ACTION_CAPTURE_FINGERPRINT = "in.gov.uidai.rdservice.fp.CAPTURE"
    var INFO_REQUEST = 0
    var CAPTURE_REQUEST = 1
    var BANK_REQUEST = 2
    var DeviceINFO_KEY = "DEVICE_INFO"
    var RD_SERVICE_INFO = "RD_SERVICE_INFO"
    var PID_DATA = "PID_DATA"
    var PID_OPTIONS = "PID_OPTIONS"
    private var dialog: ProgressDialog? = null
    lateinit var viewmodel: AepsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_aepes, container, false)
        val binding = FragmentAepesBinding.bind(view)
        val tnx_type = arguments?.getString("tnx_type")!!
        (activity as MainActivity).toolbar.setTitle(arguments?.getString("title")!!)
        viewmodel = ViewModelProviders.of(this).get(AepsViewModel::class.java)
        binding.viewModel = viewmodel
        binding.lifecycleOwner = this
        viewmodel.setTnxType(tnx_type)



        viewmodel.intent_open.observe(this.viewLifecycleOwner, Observer {
            if (it.equals("bank")) {
                val intent = Intent(activity, BankListActivity::class.java)
                startActivityForResult(intent, BANK_REQUEST)
            } else if (it.equals("aeps")) {
                openFPSensor()
            }
        })
        viewmodel.progress_bar.observe(this.viewLifecycleOwner, Observer {
            if (it) {
                show()
            } else {
                dismiss()
            }
        })
        viewmodel.error.observe(this.viewLifecycleOwner, Observer {
            if (!it.isEmpty())
                showToast(it)
        })
        //AddTextWatcher.setEditText(aadhar_no)


        return view
    }

    fun show() {
        dialog = ProgressDialog(activity)
        dialog!!.setMessage("Please wait.")
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun dismiss() {
        if (dialog != null)
            if (dialog!!.isShowing)
                dialog!!.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewmodel.clearData()

    }

    fun openFPSensor() {
        try {
            val sendIntent = Intent()
            sendIntent.action = CUSTOM_ACTION_INFO_FINGERPRINT
            startActivityForResult(sendIntent, INFO_REQUEST)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            if (AccessController.getContext() != null)
                showToast("Error Occurred")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null)
            return

        if (requestCode == BANK_REQUEST) {
            viewmodel.bank_code.postValue(data.getStringExtra("code"))
            viewmodel.bank_name.postValue(data.getStringExtra("value"))
        } else if (requestCode == INFO_REQUEST) {
            var pidDataXml = data.getStringExtra("DNC")
            var text = data.getStringExtra("DNR")
            val strDeviceInfo = data.getStringExtra(DeviceINFO_KEY)
            val strRDServiceInfo = data.getStringExtra(RD_SERVICE_INFO)
            Log.e("Shetty D Info", strDeviceInfo!!)
            if (pidDataXml != null) {
                showToast("Device is not registered")
            } else if (text != null) {
                showToast("Device is not registered")
            } else if (strRDServiceInfo != null && !strRDServiceInfo.isEmpty()) {
                var rdServiceInfo = SplitXML().SplitRDServiceInfo(strRDServiceInfo)
                if (rdServiceInfo != null) {
                    if (rdServiceInfo.status.equals("Ready", ignoreCase = true)) {
                        var deviceInfo = SplitXML().SplitDeviceInfo(strDeviceInfo)
                        val enviroment = "P"
                        var captureRequestXML = ""
                        when (getScannerType(strDeviceInfo)) {
                            0 ->  //device is next biometric
                                captureRequestXML =
                                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone =\"yes\"?><PidOptions ver =\"1.0\"><Opts env=\"$enviroment\" fCount=\"1\" fType =\"0\" format=\"0\" pidVer=\"2.0\" /><Demo></Demo><CustOpts></CustOpts></PidOptions>"
                            1 ->  //device is Precision biometric
                                captureRequestXML =
                                    "<PidOptions ver=\"1.0\"><Opts env=\"$enviroment\" fCount =\"1\" fType=\"0\" iCount=\"0\" iType=\"\" pCount=\"0\" pType=\"\" format=\"0\"  pidVer =\"2.0\" timeout=\"5000\" wadh=\"\" posh=\"UNKNOWN\" /><Demo></Demo><CustOpts><Param name =\"\" value=\"\" /></CustOpts></PidOptions>"
                            2 ->  //device is Tatvik biometric
                                captureRequestXML =
                                    "<PidOptions ver=\"1.0\"><Opts env=\"$enviroment\" fCount =\"1\" fType=\"0\" iCount=\"0\" iType=\"\" pCount=\"0\" pType=\"\" format=\"0\" pidVer =\"2.0\" timeout=\"5000\" wadh=\"\" posh=\"UNKNOWN\" /><Demo></Demo><CustOpts><Param name =\"\" value=\"\" /></CustOpts></PidOptions>"
                            3 ->  //device is Mantra biometric
                                captureRequestXML =
                                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone =\"yes\"?><PidOptions ver =\"1.0\"><Opts env=\"$enviroment\" fCount=\"1\" fType =\"0\" format=\"0\" pidVer=\"2.0\" timeout =\"5000\"/><Demo></Demo><CustOpts></CustOpts></PidOptions>"
                            4 -> {
                                //device is Morpho biometric
                                FormXML.posh = "UNKNOWN"
                                captureRequestXML =
                                    "<PidOptions ver=\"1.0\"><Opts env=\"$enviroment\" fCount =\"1\" fType=\"0\" iCount=\"0\" iType=\"\" pCount=\"0\" pType=\"\" format=\"0\" pidVer =\"2.0\" timeout=\"2000\" wadh=\"\" posh=\"UNKNOWN\" /><Demo></Demo><CustOpts><Param name =\"\" value=\"\" /></CustOpts></PidOptions>"
                            }
                            5 -> //device is Startek biometric
                                captureRequestXML =
                                    "<?xml version=\"1.0\" encoding=\"UTF-8\"  standalone =\"yes\"?><PidOptions><Opts fCount=\"1\" fType=\"0\" iCount=\"0\" iType=\"\" pCount =\"0\" pType=\"\" format=\"0\" pidVer=\"2.0\" timeout=\"20000\" otp=\"\" env=\"$enviroment\" wadh=\"\" posh=\"UNKNOWN\"/><Demo/><CustOpts><Param name=\"\" value =\"\"/></CustOpts></PidOptions>"
                            6 -> //device is Secugen biometric
                                captureRequestXML =
                                    "<PidOptions ver=\"1.0\"><Opts fCount=\"1\" fType=\"0\" format =\"0\" timeout=\"10000\" pidVer=\"2.0\" posh=\"UNKNOWN\" env=\"$enviroment\"  / ></PidOptions > "
                            else ->
                                captureRequestXML = ""
                        }
                        Log.d(Constants.TAG, "onActivityResult: $captureRequestXML")
                        if (!captureRequestXML.isEmpty()) {
                            val sendIntent = Intent()
                            sendIntent.action = CUSTOM_ACTION_CAPTURE_FINGERPRINT
                            sendIntent.putExtra(PID_OPTIONS, captureRequestXML)
                            startActivityForResult(sendIntent, CAPTURE_REQUEST)
                        }
                    } else {
                        showToast("Device is not ready")
                    }
                }
            } else {
                showToast("RD information is null")
            }
        } else {
            if (requestCode == CAPTURE_REQUEST) {
                val pidDataXml = data.getStringExtra(PID_DATA)
                val splitXML = SplitXML()
                val response = splitXML.SplitCaptureResponse(pidDataXml)
                val aepsCaptureResponseModel =
                    splitXML.SplitAepsCaptureResponseModel(pidDataXml)
                val jsonData = aepsCaptureResponseModel.getJSON(viewmodel.aadhar_no.value)
                Log.d(Constants.TAG, "DATA: $jsonData")
                Log.d(Constants.TAG, "PID_DATA: $jsonData")
                if (jsonData != null) {
                    var errCode = jsonData!!.getString("errCode")
                    if (errCode.equals("0") || errCode.equals("00")) {
                        viewmodel.finger_print_status?.postValue("Fingerprint Captured")
                        if (pidDataXml != null) {
                            viewmodel.getAepsApi(jsonData.toString(), pidDataXml)
                        } else {
                            showToast("PID Data not getting")
                        }
                    } else {
                        showToast("Device Capture Failed " + jsonData!!.getString("errInfo"))
                    }
                }
            }
        }
    }

    fun showToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }

    private fun getScannerType(info: String): Int {
        val sensorNameList = Arrays.asList(*resources.getStringArray(R.array.fingerprint))
        val scannerType = -1
        for (i in sensorNameList.indices) {
            if (info.toUpperCase().contains(sensorNameList[i].toUpperCase())) {
                return i
            }
        }
        showToast("getScannerType $scannerType")
        return scannerType
    }
}