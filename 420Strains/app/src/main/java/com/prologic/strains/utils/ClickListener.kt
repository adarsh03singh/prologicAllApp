package com.prologic.strains.utils
import com.prologic.strains.model.category.CategoryItem
import com.prologic.strains.model.order_list.OrderItem
import com.prologic.strains.model.product.ProductItem
import com.prologic.strains.search_dialog.SearchItem

interface OnDialogCloseListener {
    fun onClick()
}

interface OnSearchDialogListener {
    fun onClick(item: SearchItem)
}

interface CategoryItemListener {
    fun onClick(item: CategoryItem)
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
