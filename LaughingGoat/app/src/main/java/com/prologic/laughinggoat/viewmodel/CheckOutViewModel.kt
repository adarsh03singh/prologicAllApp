package com.prologic.laughinggoat.viewmodel

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson

import com.prologic.laughinggoat.R
import com.prologic.laughinggoat.adapter.CheckOutCartAdapter
import com.prologic.laughinggoat.databinding.CheckOutBinding
import com.prologic.laughinggoat.db.ProductRoom
import com.prologic.laughinggoat.model.auth.Billing
import com.prologic.laughinggoat.model.auth.Shipping

import com.prologic.laughinggoat.model.coupon.CouponItem
import com.prologic.laughinggoat.model.create_order.*
import com.prologic.laughinggoat.model.shipping.ShippingResult
import com.prologic.laughinggoat.model.taxes.Item
import com.prologic.laughinggoat.model.taxes.TaxesResult
import com.prologic.laughinggoat.network.Repository
import com.prologic.laughinggoat.network.errorException
import com.prologic.laughinggoat.utils.*
import com.prologic.laughinggoat.view.fragment.ShippingBilling


import kotlinx.coroutines.*
import sqip.CardEntry

class CheckOutViewModel(application: Application) : AndroidViewModel(application) {
    val apiRepository = Repository()
    val user_data = SharedPreference().getUser();
    val roomProductDao = getRoomDatabase().productRoomDao()
    var isLoaderVisible = MutableLiveData<Boolean>(false)
    var errorMessage = MutableLiveData<String>("")
    var billing_details = MutableLiveData<String>("")
    var shipping_details = MutableLiveData<String>("")
    var productRoomArray: List<ProductRoom> = listOf()
    var orderResponse = MutableLiveData<OrderResponse>()
    var couponItem = MutableLiveData<CouponItem>()
    var shippingResult: ShippingResult? = null
    var taxesItems: Item? = null
    val coupon_code = MutableLiveData<String>()
    val coupon_title = MutableLiveData<String>("")
    var coupon_title_visibility = MutableLiveData<Int>(View.GONE)
    val checkOutCartAdapter = CheckOutCartAdapter()
    var total_amt = 0.0
    var product_qtn = 0
    var product_amt = 0.0
    var coupon_amt = 0.0
    var tax_percent = 0.0
    var tax_amt = 0.0
    var shipping_amt = 0.0
    var category_id = ""
    var customer_id = ""
    var min_qty = 0
    var max_qty = 0
    lateinit var binding: CheckOutBinding
    var billing: Billing = user_data!!.billing
    var shipping: Shipping = user_data!!.shipping

    fun getRoom() {
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    productRoomArray = roomProductDao.get()
                }
            }.onSuccess {
                checkOutCartAdapter.setAdapterUpdate(productRoomArray)
                getShippingMethod()
            }.onFailure {
                errorMessage.postValue(errorException(it))
            }
        }
    }

    fun setView(binding: CheckOutBinding) {
        this.binding = binding
        customer_id = user_data?.id.toString()
        getRoom()
        couponItem.observeForever {
            applyButton()
        }
        getTaxes()
        setBilling()
        setShipping()
    }


    fun getRoomStoreCategories(): ArrayList<String> {
        val categoryId = ArrayList<String>()
        productRoomArray.forEach {
            it.categories?.forEach {
                categoryId.add(it.id)
            }
        }
        return categoryId
    }

    private fun setShippingMethod() {
        val settings = shippingResult?.settings
        if (settings != null) {
            shipping_amt = settings.cost.value.toDouble()
            binding.shippingTitle.text = shippingResult?.title + " :"
            min_qty = settings.min_qty.value.toInt()
            max_qty = settings.max_qty.value.toInt()
            category_id = settings.incl_product_cat.value
        }
        setAmount()

    }

    fun applyButton() {
        if (couponItem.value != null) {
            val minimum_amount = couponItem.value!!.minimum_amount.toDouble()
            val amount = couponItem.value!!.amount.toDouble()
            if (product_amt < minimum_amount) {
                errorMessage.value =
                    "The minimum spend for this coupon is " + currency + " " + minimum_amount
                return
            } else if (couponItem.value!!.discount_type.equals("fixed_cart")) {
                coupon_amt = product_amt - amount
            } else if (couponItem.value!!.discount_type.equals("percent")) {
                coupon_amt = ((product_amt * amount) / 100)
            } else {
                clearCoupon()
                return
            }
            coupon_title.value = couponItem.value!!.description
            coupon_title_visibility.value = View.VISIBLE
            binding.applyCancel.setBackgroundResource(R.drawable.circle_red_button)
            binding.applyCancel.text = "Cancel"
            binding.couponCodeEt.isEnabled = false
            setAmount()
        } else {
            clearCoupon()
        }
    }

    private fun clearCoupon() {
        binding.couponCodeEt.isEnabled = true
        coupon_amt = 0.0
        coupon_title.value = ""
        coupon_title_visibility.value = View.GONE
        binding.applyCancel.setBackgroundResource(R.drawable.circle_green_button)
        binding.applyCancel.text = "Apply"
        coupon_code.value = ""
        setAmount()
    }

    private fun setBilling() {
        if (billing.first_name.isNotEmpty() && billing.last_name.isNotEmpty()) {
            var value = ""
            value += "<b>Name : " + billing.first_name + " " + billing.last_name + "</b><br> "
            value += "<b>Address : </b>" + billing.address_1 + " " + billing.address_2 + " " + billing.city + "<br> "
            if (billing.company.isNotEmpty())
                value += "<b>Company : " + billing.company + "</b><br> "
            value += billing.state + " " + billing.country + " Postcode <b>" + billing.postcode + "</b><br>"
            value += "<b>Contact : </b><br>Phone Number : " + billing.phone + "<br>Email Id : " + billing.email + "<br> "
            billing_details.value = value
        }
    }

    private fun setShipping() {
        if (shipping.first_name.isNotEmpty() && shipping.last_name.isNotEmpty()) {
            var value = ""
            value += "<b>Name : " + shipping.first_name + " " + shipping.last_name + "</b><br> "
            value += "<b>Address : </b>" + shipping.address_1 + " " + shipping.address_2 + " " + shipping.city + "<br> "
            if (shipping.company.isNotEmpty())
                value += "<b>Company : " + shipping.company + "</b><br> "
            value += shipping.state + " " + shipping.country + " Postcode <b>" + shipping.postcode + "</b><br>"
            shipping_details.value = value
        }
    }

    fun setBillingBundle(bundle: Bundle) {
        val billing2 = Billing(
            first_name = bundle.getString("firstName")!!,
            last_name = bundle.getString("lastName")!!,
            address_1 = bundle.getString("address_1")!!,
            address_2 = bundle.getString("address_2")!!,
            city = bundle.getString("cityName")!!,
            company = bundle.getString("companyName")!!,
            state = bundle.getString("stateName")!!,
            country = bundle.getString("countryRegionId")!!,
            postcode = bundle.getString("postcode")!!,
            phone = bundle.getString("phoneNumber")!!,
            email = bundle.getString("emailAddress")!!,
        )
        billing = billing2
        setBilling()
    }

    fun setShippingBundle(bundle: Bundle) {
        val shipping2 = Shipping(
            first_name = bundle.getString("firstName")!!,
            last_name = bundle.getString("lastName")!!,
            address_1 = bundle.getString("address_1")!!,
            address_2 = bundle.getString("address_2")!!,
            city = bundle.getString("cityName")!!,
            company = bundle.getString("companyName")!!,
            state = bundle.getString("stateName")!!,
            country = bundle.getString("countryRegionId")!!,
            postcode = bundle.getString("postcode")!!,
        )
        shipping = shipping2
        setShipping()

    }

    fun getBillingBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString("firstName", billing.first_name)
        bundle.putString("lastName", billing.last_name)
        bundle.putString("address_1", billing.address_1)
        bundle.putString("address_2", billing.address_2)
        bundle.putString("companyName", billing.company)
        bundle.putString("countryRegionName", billing.country)
        bundle.putString("countryRegionId", billing.country)
        bundle.putString("stateName", billing.state)
        bundle.putString("cityName", billing.city)
        bundle.putString("postcode", billing.postcode)
        bundle.putString("phoneNumber", billing.phone)
        bundle.putString("emailAddress", billing.email)
        return bundle
    }

    fun getShippingBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString("firstName", shipping.first_name)
        bundle.putString("lastName", shipping.last_name)
        bundle.putString("address_1", shipping.address_1)
        bundle.putString("address_2", shipping.address_2)
        bundle.putString("companyName", shipping.company)
        bundle.putString("countryRegionName", shipping.country)
        bundle.putString("countryRegionId", shipping.country)
        bundle.putString("stateName", shipping.state)
        bundle.putString("cityName", shipping.city)
        bundle.putString("postcode", shipping.postcode)
        return bundle
    }

    fun getShippingMethod() {
        isLoaderVisible.value = true
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    shippingResult = apiRepository.getShippingMethod()
                }
            }.onSuccess {
                isLoaderVisible.value = false
                setShippingMethod()
            }.onFailure {
                isLoaderVisible.value = false
                errorMessage.postValue(errorException(it))
            }
        }

    }

    fun getTaxes() {
        var taxesResult: TaxesResult? = null
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    taxesResult = apiRepository.getTaxes()
                }
            }.onSuccess {
                if (taxesResult != null) {
                    taxesItems = taxesResult!!.get(0)
                    setAmount()
                }
            }.onFailure {
                errorMessage.postValue(errorException(it))
            }
        }
    }


    fun getCoupon() {
        isLoaderVisible.postValue(true)
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    val couponResult = (apiRepository.getCoupon(coupon_code.value!!))
                    if (couponResult.size > 0) {
                        couponItem.postValue(couponResult.get(0))
                    } else {
                        errorMessage.postValue("This coupon code not exits!")
                    }
                }
            }.onSuccess {
                isLoaderVisible.postValue(false)
            }.onFailure {
                isLoaderVisible.postValue(false)
                errorMessage.postValue(errorException(it))
            }
        }

    }

    fun move_billing_page(view: View) {
        val fragment = ShippingBilling()
        val bundle = getBillingBundle()
        bundle.putString(view_type, com.prologic.laughinggoat.utils.billing)
        bundle.putString(title_name, "Billing Details")
        fragment.arguments = bundle
        addFragment(fragment)
    }

    fun move_shipping_page(view: View) {
        val fragment = ShippingBilling()
        val bundle = getShippingBundle()
        bundle.putString(view_type, com.prologic.laughinggoat.utils.shipping)
        bundle.putString(title_name, "Shipping Details")
        fragment.arguments = bundle
        addFragment(fragment)
    }

    fun checkValidation(): Boolean {
        var isValidation = false
        val roomStoreCategories = getRoomStoreCategories()
        if (category_id.isEmpty()) {
            errorMessage.postValue("Shipping Loading")
        } else if (!roomStoreCategories.contains(category_id)) {
            errorMessage.postValue("This product is not available for shipping")
        } else if (product_qtn < min_qty) {
            errorMessage.postValue("Can buy at least $min_qty products")
        } else if (product_qtn > max_qty) {
            errorMessage.postValue("Can buy up to $max_qty products")
        } else if (customer_id.isEmpty()) {
            errorMessage.postValue("Customer Id not available")
        } else if (taxesItems == null) {
            errorMessage.postValue("Please wait product tax loading")
        } else if (shippingResult == null) {
            errorMessage.postValue("Please wait product shipping loading")
        } else if (productRoomArray.size == 0) {
            errorMessage.postValue("Please Add product")
        } else if (billing_details.value!!.isEmpty()) {
            errorMessage.postValue("Please select billing")
        } else if (shipping_details.value!!.isEmpty()) {
            errorMessage.postValue("Please select shipping")
        } else
            isValidation = true
        return isValidation
    }

    fun createOrderApi() {
        val createOrder = CreateOrder()
        createOrder.customer_id = customer_id
        createOrder.payment_method = "square_credit_card"
        createOrder.payment_method_title = "Credit Card"
        createOrder.set_paid = true
        createOrder.billing = billing
        createOrder.shipping = shipping
        for (item in productRoomArray) {
            val productItem = ProductItem()
            productItem.product_id = item.product_id
            if (item.variation_id.isNotEmpty())
                productItem.variation_id = item.variation_id
            productItem.quantity = item.quantity
            createOrder.line_items.add(productItem)
        }
        if (couponItem.value != null) {
            val couponLines = CouponLines(code = couponItem.value!!.code)
            createOrder.coupon_lines.add(couponLines)
        }
        if (taxesItems != null) {
            val taxLines = TaxLines(
                id = taxesItems!!.id,
                label = taxesItems!!.name,
                tax_total = tax_amt.toString()
            )
            createOrder.tax_lines.add(taxLines)
        }
        if (shippingResult != null) {
            val shipping_lines = ShippingLine(
                method_id = shippingResult!!.method_id,
                method_title = shippingResult!!.method_title,
                total = shipping_amt.toString()
            )
            createOrder.shipping_lines.add(shipping_lines)
        }
        Log.d(TAG, "CreateOrder\n" + gson.toJson(createOrder))

        isLoaderVisible.value = true
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    orderResponse.postValue(apiRepository.createOrder(createOrder))
                }
            }.onSuccess {
                isLoaderVisible.value = false
            }.onFailure {
                isLoaderVisible.value = false
                errorMessage.value = errorException(it)
            }
        }
    }

    fun setAmount() {
        total_amt = 0.0
        tax_amt = 0.0
        if (coupon_amt > 0)
            binding.discountLay.visibility = View.VISIBLE
        else
            binding.discountLay.visibility = View.GONE

        if (taxesItems != null) {
            tax_percent = taxesItems!!.rate.toDouble()
            if (tax_percent > 0 && product_amt > 0) {
                tax_amt = (product_amt * tax_percent) / 100
            }
        }

        total_amt = (product_amt + tax_amt + shipping_amt) - coupon_amt
        binding.taxAmount.text = currency + roundOfDecimal(tax_amt)
        binding.productAmount.text = currency + roundOfDecimal(product_amt)
        binding.couponAmount.text = currency + roundOfDecimal(coupon_amt)
        binding.shippingAmount.text = currency + roundOfDecimal(shipping_amt)
        binding.totalAmount.text = "Total Amount : " + currency + roundOfDecimal(total_amt)
        var amount_log = ""
        amount_log += "product_amt: " + product_amt
        amount_log += "\ncoupon_amt : " + coupon_amt
        amount_log += "\ntax_amt : " + tax_amt
        amount_log += "\nshipping_amt : " + shipping_amt
        amount_log += "\ntotal_amt : " + total_amt
        Log.d(TAG, "Amount Info " + amount_log)
    }


}