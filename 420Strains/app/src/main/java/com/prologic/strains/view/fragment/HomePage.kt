package com.prologic.strains.view.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.prologic.strains.R
import com.prologic.strains.databinding.HomePageBinding
import com.prologic.strains.model.category.CategoryItem
import com.prologic.strains.utils.*
import com.prologic.strains.view.activity.MainActivity
import com.prologic.strains.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.home_page.*
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit


class HomePage : Fragment() {
    lateinit var viewModel: HomeViewModel
    lateinit var binding: HomePageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.home_page, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.errorMessage.observeForever { it ->
            if (!it.isNullOrEmpty())
                AlertError.show(fragmentActivity!!, it)
        }

        viewModel.isLoaderVisible.observeForever { it ->
            if (swipeRefreshLayout != null)
                swipeRefreshLayout.isRefreshing = it
        }
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.initialize()
        }
        setHeader()
        viewModel.setViewPager(viewPager)
    

        CoroutineScope(Dispatchers.IO).launch {
            delay(TimeUnit.SECONDS.toMillis(2))
            withContext(Dispatchers.Main) {
                shopAlert()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getToday(requireContext())
        viewModel.startSliding()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopSliding()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden)
            setHeader()
    }

    private fun setHeader() {
        shooterFragment = this
        (activity as MainActivity).setHeader(getString(R.string.app_name), false, true, true, true)
    }

    private fun shopAlert() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        requireActivity().window.setBackgroundDrawableResource(R.color.trans)
        dialog.window!!.setBackgroundDrawableResource(R.color.trans)
        val metrics = DisplayMetrics()
        val windowManager =
            requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(metrics)
        dialog.setContentView(R.layout.alert_home_dialog)
        dialog.setCancelable(false)
        val layout = dialog.findViewById<LinearLayout>(R.id.layout)
        val params = layout.layoutParams
        params.width = metrics.widthPixels - metrics.widthPixels * 2 / 100
        layout.layoutParams = params
        val cancel: ImageView = dialog.findViewById(R.id.cancel)
        val shop: Button = dialog.findViewById(R.id.shop)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        shop.setOnClickListener {
            dialog.dismiss()
            val bundle = Bundle()
            bundle.putSerializable(intent_data, CategoryItem("", "All Products",""))
            val fragment = ProductList()
            fragment.arguments = bundle
            addFragment(fragment)
        }
        dialog.show()
    }

}


