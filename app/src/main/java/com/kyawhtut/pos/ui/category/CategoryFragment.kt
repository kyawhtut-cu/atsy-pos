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
import com.kyawhtut.pos.data.db.entity.ProductEntity
import com.kyawhtut.pos.ui.base.BaseFragment
import com.kyawhtut.pos.ui.home.HomeActivity
import com.kyawhtut.pos.utils.ProductType
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_category.view.divider
import kotlinx.android.synthetic.main.item_category.view.item_card_view
import kotlinx.android.synthetic.main.item_category.view.iv_bundle
import kotlinx.android.synthetic.main.item_product.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryFragment : BaseFragment(R.layout.fragment_category) {

    private val viewModel: CategoryViewModel by viewModel()

    override fun onViewCreated(bundle: Bundle) {

        (activity as HomeActivity).onNavigationItemClick = { item, pos ->
            viewModel.addSource(if (pos == 0 && item.items.first() == "Items") ProductType.Category else ProductType.Product)
        }

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
                    (activity as HomeActivity).nextIndex(item.productName.toDisplay(FontChoose.isUnicode()))
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
                this.setOnClickListener { }
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
                    mText = "${item.productPrice} Ks"
                    setTextColor(item.productTextColor)
                }
            }.layoutManager(FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            })
        }
    }
}