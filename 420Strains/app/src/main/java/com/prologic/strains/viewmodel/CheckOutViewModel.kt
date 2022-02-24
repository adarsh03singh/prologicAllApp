package com.prologic.strains.viewmodel


import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prologic.strains.adapter.CheckOutCartAdapter
import com.prologic.strains.db.ProductRoom
import com.prologic.strains.model.auth.Billing
import com.prologic.strains.model.auth.Shipping
import com.prologic.strains.model.coupon.CouponItem
import com.prologic.strains.model.create_order.CouponLines
import com.prologic.strains.model.create_order.CreateOrder
import com.prologic.strains.model.create_order.OrderResponse
import com.prologic.strains.model.create_order.ProductItem
import com.prologic.strains.network.Repository
import com.prologic.strains.network.errorException
import com.prologic.strains.utils.*
import com.prologic.strains.view.fragment.ShippingBilling
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CheckOutViewModel(application: Application) : AndroidViewModel(application) {
    val apiRepository = Repository()
    val user_data = SharedPreference().getUser();
    val roomProductDao = getRoomDatabase().productRoomDao()
    var isLoaderVisible = MutableLiveData<Boolean>(false)
    var isCouponApplied = MutableLiveData<Boolean>(false)
    var errorMessage = MutableLiveData<String>("")
    var billing_details = MutableLiveData<String>("")
    var shipping_details = MutableLiveData<String>("")
    var productRoomArray: List<ProductRoom> = listOf()
    var orderResponse = MutableLiveData<OrderResponse>()
    var couponItem = MutableLiveData<CouponItem>()
    val coupon_code = MutableLiveData<String>()
    val productAmount = MutableLiveData<String>("")
    val couponAmount = MutableLiveData<String>("")
    val totalAmount = MutableLiveData<String>("")
    val coupon_title = MutableLiveData<String>("")
    var discountLay = MutableLiveData<Int>(View.GONE)
    val checkOutCartAdapter = CheckOutCartAdapter()
    var total_amt = 0.0
    var product_qtn = 0
    var product_amt = 0.0
    var coupon_amt = 0.0
    var customer_id = ""

    //lateinit var binding: CheckOutBinding
    lateinit var billing: Billing
    lateinit var shipping: Shipping

    fun getRoom() {
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    productRoomArray = roomProductDao.get()
                }
            }.onSuccess {
                checkOutCartAdapter.setAdapterUpdate(productRoomArray)

            }.onFailure {
                errorMessage.postValue(errorException(it))
            }
        }
    }

    fun setView() {
        if (user_data != null) {
            billing = user_data.billing
            shipping = user_data.shipping
            customer_id = user_data.id
        }
        getRoom()
        couponItem.observeForever {
            applyButton()
        }
        setAmount()
        setBilling()
        setShipping()
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
                coupon_amt = amount
            } else if (couponItem.value!!.discount_type.equals("percent")) {
                coupon_amt = ((product_amt * amount) / 100)
            } else {
                clearCoupon()
                return
            }
            isCouponApplied.value = true
            coupon_title.value = couponItem.value!!.description
            setAmount()
        } else {
            clearCoupon()
        }


    }

    private fun clearCoupon() {
        isCouponApplied.value = false
        coupon_amt = 0.0
        coupon_title.value = ""
        coupon_code.value = ""
        setAmount()
    }

    private fun setBilling() {
        if (!::billing.isInitialized)
            return
        if (billing.first_name.isNotEmpty() && billing.last_name.isNotEmpty()) {
            var value = ""
            value += "Name : " + billing.first_name + " " + billing.last_name + "<br> "
            value += "Address : " + billing.address_1 + " " + billing.address_2 + " " + billing.city + "<br> "
            if (billing.company.isNotEmpty())
                value += "Company : " + billing.company + "<br> "
            value += billing.state + " " + billing.country + " Postcode <b>" + billing.postcode + "</b><br>"
            value += "Contact :<br>Phone Number : " + billing.phone + "<br>Email Id : " + billing.email + "<br> "
            billing_details.value = value
        }
    }

    private fun setShipping() {
        if (!::shipping.isInitialized)
            return
        if (shipping.first_name.isNotEmpty() && shipping.last_name.isNotEmpty()) {
            var value = ""
            value += "Name : " + shipping.first_name + " " + shipping.last_name + "<br> "
            value += "Address : " + shipping.address_1 + " " + shipping.address_2 + " " + shipping.city + "<br> "
            if (shipping.company.isNotEmpty())
                value += "Company : " + shipping.company + "<br> "
            value += shipping.state + " " + shipping.country + " Postcode " + shipping.postcode + "<br>"
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
            country = bundle.getString("countryId")!!,
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
            country = bundle.getString("countryId")!!,
            postcode = bundle.getString("postcode")!!,
        )
        shipping = shipping2
        setShipping()

    }

    fun getBillingBundle(): Bundle {
        val bundle = Bundle()
        if (!::billing.isInitialized)
            return bundle
        bundle.putString("firstName", billing.first_name)
        bundle.putString("lastName", billing.last_name)
        bundle.putString("address_1", billing.address_1)
        bundle.putString("address_2", billing.address_2)
        bundle.putString("companyName", billing.company)
        bundle.putString("countryId", billing.country)
        bundle.putString("stateName", billing.state)
        bundle.putString("cityName", billing.city)
        bundle.putString("postcode", billing.postcode)
        bundle.putString("phoneNumber", billing.phone)
        bundle.putString("emailAddress", billing.email)
        return bundle
    }

    fun getShippingBundle(): Bundle {
        val bundle = Bundle()
        if (!::shipping.isInitialized)
            return bundle
        bundle.putString("firstName", shipping.first_name)
        bundle.putString("lastName", shipping.last_name)
        bundle.putString("address_1", shipping.address_1)
        bundle.putString("address_2", shipping.address_2)
        bundle.putString("companyName", shipping.company)
        bundle.putString("countryId", shipping.country)
        bundle.putString("stateName", shipping.state)
        bundle.putString("cityName", shipping.city)
        bundle.putString("postcode", shipping.postcode)
        return bundle
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



    fun checkBilling(): String {
        if (billing!!.first_name.isNullOrEmpty()) {
            return "First Name can't be blank!"
        } else if (billing!!.last_name.isNullOrEmpty()) {
            return "Lat Name can't be blank!"
        } else if (billing!!.address_1.isNullOrEmpty()) {
            return "Address1 can't be blank!"
        } else if (billing!!.address_2.isNullOrEmpty()) {
            return "Address2 can't be blank!"
        } else if (billing!!.city.isNullOrEmpty()) {
            return "City can't be blank!"
        } else if (billing!!.state.isNullOrEmpty()) {
            return "State can't be blank!"
        } else if (billing!!.country.isNullOrEmpty()) {
            return "Country can't be blank!"
        } else if (billing!!.postcode.isNullOrEmpty()) {
            return "Postcode can't be blank!"
        } else if (billing!!.phone.isNullOrEmpty()) {
            return "Phone No can't be blank!"
        } else if (billing!!.email.isNullOrEmpty()) {
            return "Email Id can't be blank!"
        }
        return ""
    }

    fun checkShipping(): String {
        if (shipping!!.first_name.isNullOrEmpty()) {
            return "First Name can't be blank!"
        } else if (shipping!!.last_name.isNullOrEmpty()) {
            return "Lat Name can't be blank!"
        } else if (shipping!!.address_1.isNullOrEmpty()) {
            return "Address1 can't be blank!"
        } else if (shipping!!.address_2.isNullOrEmpty()) {
            return "Address2 can't be blank!"
        } else if (shipping!!.city.isNullOrEmpty()) {
            return "City can't be blank!"
        } else if (shipping!!.state.isNullOrEmpty()) {
            return "State can't be blank!"
        } else if (shipping!!.country.isNullOrEmpty()) {
            return "Country can't be blank!"
        } else if (shipping!!.postcode.isNullOrEmpty()) {
            return "Postcode can't be blank!"
        }
        return ""
    }

    fun checkValidation(): Boolean {
        val billingError = checkBilling()
        val shippingError = checkShipping()
        var isValidation = false
        if (customer_id.isEmpty()) {
            errorMessage.postValue("Customer Id not available")
        } else if (!billingError.isNullOrEmpty()) {
            errorMessage.postValue("Billing Details : " + billingError)
        }/* else if (!shippingError.isNullOrEmpty()) {
            actionMessage.postValue( "Shipping Details : " + shippingError)
        } */ else if (productRoomArray.size == 0) {
            errorMessage.postValue("Please Add product")
        } else if (billing_details.value!!.isEmpty()) {
            errorMessage.postValue("Please select billing")
        } else
            isValidation = true
        return isValidation
    }


    fun createOrderApi() {
        val createOrder = CreateOrder()
        createOrder.customer_id = customer_id
        createOrder.payment_method = "cod"
        createOrder.payment_method_title = "Cash On Delivery"
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
        if (coupon_amt > 0)
            discountLay.value = View.VISIBLE
        else
            discountLay.value = View.GONE
        total_amt = product_amt - coupon_amt
        productAmount.value = currency + number2digits(product_amt)
        couponAmount.value = currency + number2digits(coupon_amt)
        totalAmount.value = "Total Amount : " + currency + number2digits(total_amt)
        var amount_log = ""
        amount_log += "product_amt: " + product_amt
        amount_log += "\ncoupon_amt : " + coupon_amt
        amount_log += "\ntotal_amt : " + total_amt
        Log.d(TAG, "Amount Info " + amount_log)
    }


}