package com.prologic.strains.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.prologic.strains.db.ProductRoom

import com.prologic.strains.model.product.ProductItem
import com.prologic.strains.model.product.Variation
import kotlinx.coroutines.*


class SharedPreferenceProduct {
    private val appSharedPrefs =
        MyApplication.getMyApplication().getSharedPreferences("ProductStore", Context.MODE_PRIVATE)
    private val prefsEditor: SharedPreferences.Editor = appSharedPrefs.edit()
    private val product_room_dao = getRoomDatabase().productRoomDao()

    fun clear() {
        prefsEditor.clear()
        prefsEditor.commit()
    }


    fun getQuantity(item: ProductItem, variation: Variation?): Int {
        var variation_id = ""
        if (variation != null)
            variation_id = variation.id
        val key = getProductUniqeKey(item.id, variation_id)
        return appSharedPrefs.getInt(key, 0)
    }

    fun setProduct(product_id: String, variation_id: String, quantity: Int) {
        val key = getProductUniqeKey(product_id, variation_id)
        prefsEditor.putInt(key, quantity)
        prefsEditor.commit()
    }

    private fun getProductRoomData(productItem: ProductItem, variation: Variation?): ProductRoom {
        val productRoom = ProductRoom()
        productRoom.product_id = productItem.id
        productRoom.name = productItem.name
        productRoom.short_description = productItem.short_description
        productRoom.description = productItem.description
        productRoom.categories = productItem.categories
        if (variation == null) {
            val key = getProductUniqeKey(productItem.id, "")
            productRoom.unique_id = key
            productRoom.image = productItem.images!![0].src
            productRoom.variation_id = ""
            productRoom.price = productItem.price
            productRoom.regular_price = productItem.regular_price
            productRoom.sale_price = productItem.sale_price
            productRoom.variation_attribute = arrayListOf()
        } else {
            val key = getProductUniqeKey(productItem.id, variation.id)
            productRoom.unique_id = key
            productRoom.image = variation.image.src
            productRoom.variation_id = variation.id
            productRoom.price = variation.price
            productRoom.regular_price = variation.regular_price
            productRoom.sale_price = variation.sale_price
            productRoom.variation_attribute = variation.attributes
        }
        Log.d(TAG, "ProductRoom\n" + productRoom.toString())
        return productRoom
    }

    fun updateQuantity(event: Int, item: ProductItem, variation: Variation?, quantity: Int) {
        var variation_id = ""
        if (variation != null)
            variation_id = variation.id

        val key = getProductUniqeKey(item.id, variation_id)
        prefsEditor.putInt(key, quantity)
        prefsEditor.commit()


        val productItem = getProductRoomData(item, variation)
        productItem.quantity = quantity

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            val error = "Error : " + throwable.localizedMessage

            Handler(Looper.getMainLooper()).post {
                showToast(error)
            }
        }
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            if (event == 1) {
                if (quantity == 1) {
                    product_room_dao.insert(productItem)
                } else {
                    product_room_dao.update(quantity, key)
                }
            } else if (event == 2) {
                if (quantity == 0) {
                    product_room_dao.deleteProduct(productItem.unique_id)
                } else {
                    product_room_dao.update(quantity, key)
                }
            }

        }
    }


}