package com.kyawhtut.lib.pagination

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyawhtut.lib.R
import com.kyawhtut.lib.rv.bind
import com.kyawhtut.lib.rv.update
import kotlinx.android.synthetic.main.item_page.view.*
import kotlinx.android.synthetic.main.pagination_view.view.*

class PaginationView : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        addView(LayoutInflater.from(context).inflate(R.layout.pagination_view, this, false))

        rv_pagination.bind(emptyList<PaginationItem>(), R.layout.item_page) { item, pos ->
            when (item.paginationType) {
                is PaginationType.NUMBER -> {
                    this.tv_page.visibility = View.VISIBLE
                    this.iv_dot.visibility = View.INVISIBLE
                }
                is PaginationType.DOT -> {
                    this.tv_page.visibility = View.INVISIBLE
                    this.iv_dot.visibility = View.VISIBLE
                }
            }
            this.setOnClickListener {
                if (item.paginationType == PaginationType.NUMBER) pageClick?.invoke(item.pageNumber)
            }
            this.root.background = ContextCompat.getDrawable(
                context,
                if (item.isCurrentPage) R.drawable.bg_outline_box_active else R.drawable.bg_outline_box
            )
            this.tv_page.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (item.isCurrentPage) android.R.color.white else android.R.color.darker_gray
                )
            )
            this.tv_page.text = "${item.pageNumber}"
        }.layoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))
    }

    var currentPage: Int = 0
        set(value) {
            field = value
            calculatePageCount()
        }

    var pageCount: Int = 0
        set(value) {
            field = value
            if (value > 0)
                calculatePageCount()
        }
    var pageClick: ((Int) -> Unit)? = null

    private fun isFirst(page: Int) = page == 0
    private fun isSecond(page: Int) = page == 1
    private fun isLast(page: Int) = page == 4
    private fun isSecondLast(page: Int) = page == 3

    private val isCurrentPageBetweenSecondLast: Boolean
        get() = currentPage <= 2 || currentPage >= pageCount - 1

    private fun calculatePageCount() {
        val list = mutableListOf<PaginationItem>()
        var dotCount = 0
        if (pageCount < 5) {
            (0 until pageCount).forEach {
                list.add(
                    pagination {
                        pageNumber = it + 1
                        isCurrentPage = (it + 1) == currentPage
                    }
                )
            }
        } else {
            (0 until 5).forEach {
                if (isFirst(it) || isLast(it) || ((isSecond(it) || isSecondLast(it)) && isCurrentPageBetweenSecondLast)) {
                    list.add(
                        pagination {
                            pageNumber =
                                if (isFirst(it)) 1 else if (isSecond(it)) 2 else if (isLast(it)) pageCount else pageCount - 1
                            isCurrentPage = when {
                                it < 2 -> {
                                    it + 1 == currentPage
                                }
                                it > 3 -> {
                                    pageCount == currentPage
                                }
                                else -> pageCount - currentPage == 1
                            }
                        }
                    )
                } else {
                    dotCount += 1
                    list.add(
                        pagination {
                            pageNumber = it + 1
                            paginationType = PaginationType.DOT
                        }
                    )
                }
            }
        }
        if (dotCount == 3) {
            list.removeAt(2)
            list.add(
                2,
                pagination {
                    pageNumber = currentPage
                    isCurrentPage = true
                    paginationType = PaginationType.NUMBER
                }
            )
        }
        rv_pagination.update(list)
    }
}