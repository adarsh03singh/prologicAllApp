package com.prologic.laughinggoat.utils


import com.prologic.laughinggoat.model.FloralGalleryItem
import com.prologic.laughinggoat.model.blog.BlogItem
import com.prologic.laughinggoat.model.category.CategoryItem
import com.prologic.laughinggoat.model.event_vendor.EventVendorItem
import com.prologic.laughinggoat.model.order_list.OrderItem
import com.prologic.laughinggoat.model.product.ProductItem


import com.prologic.laughinggoat.search_dialog.SearchItem

interface OnDialogCloseListener {
    fun onClick()
}

interface OnSearchDialogListener {
    fun onClick(item: SearchItem)
}

interface CategoryItemListener {
    fun onClick(item: CategoryItem)
}

interface BlogItemListener {
    fun onClick(item: BlogItem)
}

interface EventVendorItemListener {
    fun onClick(item: EventVendorItem, url: String)
}

interface FloralGalleryItemListener {
    fun onClick(item: FloralGalleryItem)
}

interface ProductItemListener {
    fun onClick(item: ProductItem)
}

interface OrderListClickListener {
    fun onClick(item: OrderItem)
}

interface FeaturedProductClickListener {
    fun onClick(item: ProductItem)
}

interface ItemClickListener {
    fun onItemClicked(position: Int)
}
