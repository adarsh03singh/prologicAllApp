package com.prologic.laughinggoat.view.fragment

import android.graphics.Paint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.gson.Gson


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.blog.prologic.imageslider.constants.ScaleTypes
import com.blog.prologic.imageslider.interfaces.ItemClickListener
import com.blog.prologic.imageslider.models.SlideModel
import com.prologic.laughinggoat.R
import com.prologic.laughinggoat.databinding.ProductInfoBinding
import com.prologic.laughinggoat.model.product.ProductItem
import com.prologic.laughinggoat.model.product.Variation
import com.prologic.laughinggoat.utils.*
import com.prologic.laughinggoat.view.activity.MainActivity
import com.prologic.laughinggoat.view.activity.MyImageView
import com.prologic.laughinggoat.viewmodel.ProductInfoViewModel
import kotlinx.android.synthetic.main.product_info.*


class ProductInfo : Fragment() {
    lateinit var viewModel: ProductInfoViewModel
    lateinit var binding: ProductInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
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
        val title = viewModel.productItem.value!!.name
        shooterFragment = this
        (activity as MainActivity).setHeader(
            title,
            true,
            true,
            true,
            true
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.productItem.value = arguments?.getSerializable(intent_data) as ProductItem?
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
                        //   requireActivity().supportFragmentManager.popBackStack()
                    }
                })
        }

        viewModel.product_quantity.observeForever {
            if (it > 0) {
                addToCart.visibility = View.GONE
                minus.visibility = View.VISIBLE
            } else {
                addToCart.visibility = View.VISIBLE
                minus.visibility = View.GONE
            }
        }
        viewModel.productItem.observeForever {
            loadData()
        }
    }

    private fun loadData() {
        val productItem = viewModel.productItem.value
        if (productItem == null)
            return
        setHeader()
        viewModel.title_text.value = productItem.name

        addToCart.visibility = View.VISIBLE
        val imageList = ArrayList<SlideModel>()
        for (imageName in productItem.images) {
            imageList.add(SlideModel(imageName.src, ScaleTypes.CENTER_CROP))
        }
        image_slider.setImageList(imageList)
        image_slider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                val intent = Intent(context, MyImageView::class.java)
                intent.putExtra("imageUrl", imageList.get(position).imageUrl)
                intent.putExtra("imageTitle", productItem.name)
                startActivity(intent)
            }
        })
        spin1Lay.visibility = View.GONE
        spin2Lay.visibility = View.GONE
        name.text = productItem.name


        description.text = getHtmlSpanned(productItem.description)
        val categories = ArrayList<String>()
        productItem.categories.forEach {
            categories.add(it.name)
        }
        product_categories.text = categories.toString().replace("[", "").replace("]", "")
        if (productItem.short_description.isNotEmpty()) {
            short_description.visibility = View.VISIBLE
            short_description.text = getHtmlSpanned(productItem.short_description)
        } else {
            short_description.visibility = View.GONE
            short_description.text = ""
        }
        val attributes = productItem.attributes
        val variations = productItem.variations
        if (attributes.size == 0) {
            setPrice(productItem.regular_price, productItem.sale_price)
            layMain.visibility = View.VISIBLE
            start_end_price.visibility = View.GONE
            unit_option.visibility = View.GONE
            variation_lay.visibility = View.GONE
            viewModel.getProductQuantity()
        } else if (attributes.size > 0 && variations.size > 0) {
            layMain.visibility = View.VISIBLE
            start_end_price.visibility = View.VISIBLE
            unit_option.visibility = View.VISIBLE
            variation_lay.visibility = View.VISIBLE
            if (attributes.size > 0) {
                spin1Text.visibility = View.VISIBLE
                spin1Lay.visibility = View.VISIBLE
                spin1Text.text = attributes.get(0).name
                val adapter =
                    ArrayAdapter(requireContext(), R.layout.spin_1, attributes.get(0).options)
                spin1.adapter = adapter
                spin1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        getSelectedVariations(variations)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }
            }
            if (attributes.size > 1) {
                spin2Text.visibility = View.VISIBLE
                spin2Lay.visibility = View.VISIBLE
                spin2Text.text = attributes.get(1).name
                val adapter =
                    ArrayAdapter(requireContext(), R.layout.spin_1, attributes.get(1).options)
                spin2.adapter = adapter
                spin2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        getSelectedVariations(variations)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }
            }
        } else {
            layMain.visibility = View.GONE
        }

    }


    private fun setPrice(regularPrice: String, salePrice: String) {
        regular_price.text = currency + regularPrice
        sale_price.text = currency + salePrice
        if (regularPrice.isEmpty() && salePrice.isEmpty()) {
            viewModel.errorMessage.value = "You cannot add this item to cart, please contact admin!"
            addToCart.visibility = View.GONE
        } else {
            addToCart.visibility = View.VISIBLE
            if (salePrice.isEmpty()) {
                sale_price.visibility = View.GONE
                regular_price.setTextColor(resources.getColor(R.color.green))
            } else {
                regular_price.setTextColor(resources.getColor(R.color.gray))
                sale_price.visibility = View.VISIBLE
            }
        }
    }


    fun getSelectedVariations(variations: List<Variation>) {
        if (variations.size > 1) {
            start_end_price.text =
                "Min to Max : " + currency + variations[0].price + " - " + currency + variations[variations.size - 1].price
        }
        viewModel.variation.value = null
        for (variation in variations) {
            val variations_attributes = variation.attributes
            if (variations_attributes.size == 1) {
                val spin1Val = spin1.selectedItem.toString()
                if (variations_attributes.get(0).option.equals(spin1Val)) {
                    val unit_name = spin1Text.text.toString() + " " + spin1Val
                    unit_option.text = unit_name
                    setPrice(variation.regular_price, variation.sale_price)
                    viewModel.variation.value = variation
                    viewModel.getProductQuantity()
                }

            } else if (variations_attributes.size == 2) {
                val spin1Val = spin1.selectedItem.toString()
                val spin2Val = spin2.selectedItem.toString()
                if (variations_attributes.get(0).option.equals(spin1Val) && variations_attributes.get(
                        1
                    ).option.equals(spin2Val)
                ) {
                    val unit_name =
                        spin1Text.text.toString() + " : " + spin1Val + " & " + spin2Text.text.toString() + " : " + spin2Val
                    unit_option.text = unit_name
                    setPrice(variation.regular_price, variation.sale_price)
                    viewModel.variation.value = variation
                    viewModel.getProductQuantity()
                }
            }
        }

    }


}