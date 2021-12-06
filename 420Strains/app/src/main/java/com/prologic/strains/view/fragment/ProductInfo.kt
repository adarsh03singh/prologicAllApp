package com.prologic.strains.view.fragment


import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Spinner

import androidx.fragment.app.Fragment
import com.prologic.strains.R
import com.prologic.strains.databinding.ProductInfoBinding
import com.prologic.strains.model.product.ProductItem
import com.prologic.strains.model.product.Variation
import com.prologic.strains.model.slider.SliderItem
import com.prologic.strains.model.slider.SliderResult
import com.prologic.strains.utils.*
import com.prologic.strains.utils.is_cart_update
import com.prologic.strains.utils.shooterFragment
import com.prologic.strains.view.activity.MainActivity
import com.prologic.strains.viewmodel.ProductInfoViewModel
import kotlinx.android.synthetic.main.product_info.*


class ProductInfo : Fragment() {
    lateinit var viewModel: ProductInfoViewModel

    lateinit var variationsArray: List<Variation>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var binding: ProductInfoBinding =
            DataBindingUtil.inflate(inflater, R.layout.product_info, container, false)
        viewModel = ViewModelProvider(this).get(ProductInfoViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setHeader()
            if (is_cart_update) {
                loadData()
            }
        }
    }

    private fun setHeader() {
        shooterFragment = this
        (activity as MainActivity).setHeader(
            viewModel.productItem.value!!.name,
            true,
            true,
            true,
            true
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.productItem.value = arguments?.getSerializable(intent_data) as ProductItem
        viewModel.setViewPager(viewPager)
        layMain.visibility = View.GONE
        viewModel.isLoaderVisible.observe(viewLifecycleOwner, Observer { it ->
            if (it)
                DialogLoading.show(requireActivity())
            else
                DialogLoading.hide()
        })


        viewModel.errorMessage.observeForever { it ->
            if (it.isNotEmpty())
                AlertError.show(requireActivity(), it, object : OnDialogCloseListener {
                    override fun onClick() {
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                })
        }

        viewModel.product_quantity.observeForever {
            quantity.text = it.toString()
            if (it > 0) {
                minus.visibility = View.VISIBLE
                quantity.visibility = View.VISIBLE
                plus.visibility = View.VISIBLE
                addToCart.visibility = View.GONE
            } else {
                minus.visibility = View.GONE
                quantity.visibility = View.GONE
                plus.visibility = View.GONE
                addToCart.visibility = View.VISIBLE
            }
        }
        viewModel.productItem.observeForever {
            loadData()
            val sliderResult = SliderResult()
            it.images.forEach {
                sliderResult.add(SliderItem(it.src))
            }
            viewModel.sliderResult.value = sliderResult
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startSliding()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopSliding()
    }

    private fun loadData() {
        val productItem = viewModel.productItem.value
        if (productItem == null)
            return
        setHeader()
        viewModel.title_text.value = productItem.name

        minus.visibility = View.GONE
        quantity.visibility = View.GONE
        plus.visibility = View.GONE
        addToCart.visibility = View.VISIBLE
        eventLay.visibility = View.GONE
        spin1Lay.visibility = View.GONE
        spin2Lay.visibility = View.GONE


        val categories: ArrayList<String> = ArrayList()
        productItem.categories.forEach {
            categories.add(it.name)
        }
        name.text = productItem.name
        description.text = getHtmlSpanned(productItem.description)
        product_categories.text = "Category : " + categories.joinToString(",")
        product_sku.text = "SKU : " + productItem.sku
        if (productItem.short_description.isNotEmpty()) {
            short_description.visibility = View.VISIBLE
            short_description.text = getHtmlSpanned(productItem.short_description)
        } else {
            short_description.visibility = View.GONE
            short_description.text = ""
        }
        variationsArray = productItem.variations
        val attributes = productItem.attributes
        if (attributes.size > 0 && variationsArray.size > 0) {
            start_end_price.visibility = View.VISIBLE
            unit_option.visibility = View.VISIBLE
            variation_lay.visibility = View.VISIBLE
            if (attributes.size > 0) {
                spin1Text.visibility = View.VISIBLE
                spin1Lay.visibility = View.VISIBLE
                spin1Text.text = attributes.get(0).name
                setSpinner(spin1, attributes.get(0).options)
            }
            if (attributes.size > 1) {
                spin2Text.visibility = View.VISIBLE
                spin2Lay.visibility = View.VISIBLE
                spin2Text.text = attributes.get(1).name
                setSpinner(spin2, attributes.get(1).options)
            }
        } else {
            start_end_price.visibility = View.GONE
            unit_option.visibility = View.GONE
            variation_lay.visibility = View.GONE
            viewModel.getProductQuantity()
            setPrice(productItem.getProductPrice(), productItem.in_stock)
        }
        layMain.visibility = View.VISIBLE
    }

    private fun setSpinner(spin: Spinner, list: List<String>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.spin_1, list)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spin.adapter = adapter
        spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                getSelectedVariations()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun setPrice(priceval: String, in_stock: Boolean) {
        if (priceval.isEmpty()) {
            price.text = ""
        } else {
            price.text = currency + priceval
        }
        if (in_stock) {
            eventLay.visibility = View.VISIBLE
            outOfStock.visibility = View.GONE
        } else {
            eventLay.visibility = View.GONE
            outOfStock.visibility = View.VISIBLE
        }

    }


    fun getSelectedVariations() {
        if (variationsArray.size > 1) {
            start_end_price.text =
                "Min to Max : " + currency + variationsArray[0].price + " - " + currency + variationsArray[variationsArray.size - 1].price
        }
        viewModel.variation.value = null
        for (variation in variationsArray) {
            val v_att = variation.attributes
            if (v_att.size == 1) {
                val spin1Val = spin1.selectedItem.toString()
                if (v_att[0].option.equals(spin1Val)) {
                    unit_option.text = spin1Text.text.toString() + " " + spin1Val
                    setPrice(variation.getProductPrice(), variation.in_stock)
                    viewModel.variation.value = variation
                    viewModel.getProductQuantity()
                }
            } else if (v_att.size == 2) {
                val spin1Val = spin1.selectedItem.toString()
                val spin2Val = spin2.selectedItem.toString()
                if (v_att[0].option.equals(spin1Val) && v_att[1].option.equals(spin2Val)) {
                    unit_option.text =
                        spin1Text.text.toString() + " : " + spin1Val + " & " + spin2Text.text.toString() + " : " + spin2Val
                    setPrice(variation.getProductPrice(), variation.in_stock)
                    viewModel.variation.value = variation
                    viewModel.getProductQuantity()
                }
            }
        }

    }


}