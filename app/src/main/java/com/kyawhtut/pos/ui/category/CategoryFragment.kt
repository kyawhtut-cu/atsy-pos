package com.kyawhtut.pos.ui.category

import android.os.Bundle
import androidx.lifecycle.observe
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.kyawhtut.fontchooserlib.FontChoose
import com.kyawhtut.fontchooserlib.util.toDisplay
import com.kyawhtut.lib.rv.bind
import com.kyawhtut.lib.rv.update
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseFragmentViewModel
import com.kyawhtut.pos.data.db.entity.ProductEntity
import com.kyawhtut.pos.phone.home.PhoneHomeActivity
import com.kyawhtut.pos.ui.category.dialog.CategoryAddDialog
import com.kyawhtut.pos.ui.product.ProductAddDialog
import com.kyawhtut.pos.utils.ProductType
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_category.view.divider
import kotlinx.android.synthetic.main.item_category.view.item_card_view
import kotlinx.android.synthetic.main.item_category.view.iv_bundle
import kotlinx.android.synthetic.main.item_product.view.*
import moe.feng.common.view.breadcrumbs.model.BreadcrumbItem
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CategoryFragment : BaseFragmentViewModel<CategoryViewModel>(R.layout.fragment_category) {

    var itemClick: (String) -> Unit = {
        (activity as PhoneHomeActivity).nextIndex(it)
    }
    var addProduct: (Int) -> Unit = {}

    override val viewModel: CategoryViewModel by viewModel()
    var state: ProductType = ProductType.Category

    override fun onViewCreated(bundle: Bundle) {

        viewModel.dataList.observe(this) {
            rv_category.update(it.toMutableList())
        }

        viewModel.addSource(ProductType.Category)

        rv_category.apply {
            bind(
                emptyList(),
                ProductEntity.diffUtil
            ).map(
                R.layout.item_category,
                { item, pos -> item.type == ProductType.Category }) { item, pos ->
                this.item_card_view.setOnClickListener {
                    viewModel.categoryId = item.id
                    itemClick(item.productName.toDisplay(FontChoose.isUnicode()))
                }
                this.item_card_view.setOnLongClickListener {
                    CategoryAddDialog.show(parentFragmentManager, item.id)
                    true
                }
                this.iv_bundle.setColorFilter(item.productTextColor)
                this.item_card_view.setCardBackgroundColor(item.productColor)
                this.divider.setBackgroundColor(item.productTextColor)
                this.tv_category_name.apply {
                    mText = item.productName
                    setTextColor(item.productTextColor)
                }
                this.tv_category_count.apply {
                    mText = item.getProductCountValue()
                    setTextColor(item.productTextColor)
                }
            }.map(
                R.layout.item_product,
                { item, idx -> item.type == ProductType.Product }) { item, pos ->
                Timber.d("Product bind -> %s", item)
                this.setOnClickListener {
                    addProduct(item.id)
                }
                this.setOnLongClickListener {
                    ProductAddDialog.show(parentFragmentManager, item.id)
                    true
                }
                this.item_card_view.setCardBackgroundColor(item.productColor)
                this.tv_product_name.apply {
                    mText = item.productName
                    setTextColor(item.productTextColor)
                }
                this.iv_bundle.setColorFilter(item.productTextColor)
                this.divider.setBackgroundColor(item.productTextColor)
                this.tv_product_count.apply {
                    mText = item.getProductCountValue()
                    setTextColor(item.productTextColor)
                }
                this.tv_product_price.apply {
                    mText = "${item.productRetailPrice} Ks"
                    setTextColor(item.productTextColor)
                }
            }.layoutManager(FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            })
        }
    }

    fun changeData(item: BreadcrumbItem, pos: Int) {
        state =
            if (pos == 0 && item.items.first() == "Sale") ProductType.Category else ProductType.Product
        viewModel.addSource(state)
    }

    fun filter(query: String) {
        viewModel.dataList.value?.filter {
            it.productName.toLowerCase().contains(query.toLowerCase()) || "${it.id}" == query
        }.run {
            rv_category.update(this?.toMutableList() ?: mutableListOf())
        }
    }
}