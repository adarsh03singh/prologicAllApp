package com.prologicwebsolution.microatm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.prologicwebsolution.microatm.util.GenericTextWatcher
import com.prologicwebsolution.microatm.R


class WalkthroughOtpFragment : Fragment() {

    var otp_textbox_one: EditText? = null
     var otp_textbox_two:EditText? = null
     var otp_textbox_three:EditText? = null
     var otp_textbox_four:EditText? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_walkthrough_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        otp_textbox_one = view.findViewById(R.id.otp_edit_box1);
        otp_textbox_two = view.findViewById(R.id.otp_edit_box2);
        otp_textbox_three = view.findViewById(R.id.otp_edit_box3);
        otp_textbox_four = view.findViewById(R.id.otp_edit_box4);


        val edit =
            arrayOf<EditText>(otp_textbox_one!!, otp_textbox_two!!, otp_textbox_three!!, otp_textbox_four!!)

        otp_textbox_one!!.addTextChangedListener(
            GenericTextWatcher(
                otp_textbox_one,
                edit
            )
        )
        otp_textbox_two!!.addTextChangedListener(
            GenericTextWatcher(
                otp_textbox_two,
                edit
            )
        )
        otp_textbox_three!!.addTextChangedListener(
            GenericTextWatcher(
                otp_textbox_three,
                edit
            )
        )
        otp_textbox_four!!.addTextChangedListener(
            GenericTextWatcher(
                otp_textbox_four,
                edit
            )
        )

//        val buttonVerify = view.findViewById<Button>(R.id.buttonVerify)
//        buttonVerify.setOnClickListener {
//            view.findNavController().navigate(R.id.loginFragment)
//        }
    }
}