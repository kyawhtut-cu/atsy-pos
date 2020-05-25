package com.kyawhtut.lib.minidrawer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.kyawhtut.lib.R
import com.kyawhtut.lib.rv.bind
import com.kyawhtut.lib.rv.update
import kotlinx.android.synthetic.main.item_drawer_header.view.*
import kotlinx.android.synthetic.main.item_drawer_menu.view.*
import kotlinx.android.synthetic.main.item_drawer_title.view.*
import kotlinx.android.synthetic.main.layout_minidrawer.view.*

class MiniDrawer : FrameLayout {

    private var contentView: View? = null
    private var slidingLayout: View? = null
    private var miniWidth = UIUtils.convertPixelsToDp(56f, context)
    private var menuWidth = UIUtils.convertPixelsToDp(240f, context)
    var drawerMenuList: List<DrawerItemBase> = mutableListOf()
        set(value) {
            field = value
            lastSelectedIndex = value.indexOfFirst {
                var data: DrawerItem? = null
                if (it.type is DrawerItemType.ITEM)
                    data = it.data as DrawerItem
                data != null && data.isSelected
            }
            rv_full_menu.update(value.toMutableList())
            rv_mini_menu.update(getMiniDrawerItemList().toMutableList())
        }

    var onMenuItemClick: (DrawerItemType, Int) -> Unit = { _, _ -> }
    var lastSelectedIndex = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MiniDrawer, defStyleAttr, 0)
        miniWidth = a.getDimension(
            R.styleable.MiniDrawer_mini_width,
            UIUtils.convertPixelsToDp(56f, context)
        )
        menuWidth = a.getDimension(
            R.styleable.MiniDrawer_menu_width,
            UIUtils.convertPixelsToDp(240f, context)
        )
        a.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount != 1) return
        contentView = getChildAt(0) as? ViewGroup ?: return
        this.removeAllViews()
        slidingLayout =
            LayoutInflater.from(context).inflate(R.layout.layout_minidrawer, this, false)
        this.addView(slidingLayout)
        content_panel.addView(contentView)

        content_panel.setOnClickListener { }

        frame_first.layoutParams.width = menuWidth.toInt()
        frame_second.layoutParams.width = miniWidth.toInt()

        (content_panel.layoutParams as MarginLayoutParams).marginStart = miniWidth.toInt()

        rv_full_menu.bind(drawerMenuList)
            .map(
                R.layout.item_drawer_header,
                { item, _ -> item.type is DrawerItemType.HEADER }) { item, _ ->
                val data = item.data as DrawerHeader
                this.iv_app_logo.visibility = View.GONE
                this.gp_full_view.visibility = View.VISIBLE
                this.iv_app_logo_two.setImageResource(data.icon)
                this.shadow_bottom.visibility = if (data.showShadow) View.VISIBLE else View.GONE
                this.tv_header_title.mText = data.title
                this.tv_header_description.mText = data.description
            }
            .map(
                R.layout.item_drawer_menu,
                { item, _ -> item.type is DrawerItemType.ITEM }) { item, pos ->
                val data = item.data as DrawerItem
                this.iv_status.visibility = View.GONE
                this.divider_view_one.visibility = View.GONE
                this.divider_view_two.visibility =
                    if (data.isSelected && data.showInMiniDrawer) View.VISIBLE else View.GONE

                this.iv_status_right.apply {
                    visibility = if (data.showBadge) View.VISIBLE else View.GONE
                    mText = if (data.badgeCount == 0) "" else "${data.badgeCount}"
                }

                this.iv_function_icon.setImageResource(data.icon)
                this.tv_function_title.mText = data.title
                this.tv_function_description.mText = data.description
                this.setOnClickListener {
                    if (lastSelectedIndex != pos)
                        onMenuItemClick(item.type, pos)
                    setSelectedPosition(pos)
                    closeDrawer()
                }
            }
            .map(
                R.layout.item_drawer_divider,
                { item, _ -> item.type is DrawerItemType.DIVIDER }) { _, _ ->
            }
            .map(
                R.layout.item_drawer_title,
                { item, _ -> item.type is DrawerItemType.TITLE }) { item, _ ->
                val data = item.data as DrawerTitle
                this.tv_title.text = data.title
            }

        rv_mini_menu.bind(getMiniDrawerItemList())
            .map(
                R.layout.item_drawer_header,
                { item, _ -> item.type is DrawerItemType.HEADER }) { item, _ ->
                val data = item.data as DrawerHeader
                this.gp_full_view.visibility = View.GONE
                this.iv_app_logo.apply {
                    setImageResource(data.icon)
                    visibility = View.VISIBLE
                }
                this.shadow_bottom.visibility = View.GONE
                this.setOnClickListener { openDrawer() }
            }
            .map(
                R.layout.item_drawer_menu,
                { item, _ -> item.type is DrawerItemType.ITEM }) { item, _ ->
                val data = item.data as DrawerItem
                this.gp_function.visibility = View.GONE
                this.iv_status_right.visibility = View.GONE
                this.divider_view_two.visibility = View.GONE
                this.divider_view_one.visibility =
                    if (data.isSelected && data.showInMiniDrawer) View.VISIBLE else View.GONE

                this.iv_status.apply {
                    visibility = if (data.showBadge) View.VISIBLE else View.GONE
                    mText = if (data.badgeCount == 0) "" else "${data.badgeCount}"
                }
                this.iv_function_icon.setImageResource(data.icon)
                this.tv_function_title.mText = data.title
                this.tv_function_description.mText = data.description
                this.setOnClickListener {
                    with(drawerMenuList.indexOf(item)) {
                        if (lastSelectedIndex != this)
                            onMenuItemClick(item.type, this)
                        setSelectedPosition(this)
                    }
                }
            }
            .map(
                R.layout.item_drawer_divider,
                { item, _ -> item.type is DrawerItemType.DIVIDER }) { _, _ ->
            }
    }

    fun setMiniWidth(width: Float) {
        miniWidth = width
        frame_second.layoutParams.width = width.toInt()

        (content_panel.layoutParams as MarginLayoutParams).marginStart = width.toInt()
    }

    fun setMenuWidth(width: Float) {
        menuWidth = width
        frame_first.layoutParams.width = width.toInt()
    }

    private fun checkPosition(pos: Int) {
        if (drawerMenuList[pos].type !is DrawerItemType.ITEM) {
            throw Exception("Menu position must be menu item position.")
        }
    }

    private fun changeMenuSelectedPosition(pos: Int, isSelected: Boolean) {
        checkPosition(pos)
        val obj = drawerMenuList[pos]
        val miniPosition = getMiniDrawerItemList().indexOf(obj)
        (obj.data as DrawerItem).isSelected = isSelected
        rv_mini_menu.update(miniPosition, obj)
        rv_full_menu.update(pos, obj)
    }

    private fun isShowInMiniDrawer(pos: Int): Boolean {
        checkPosition(pos)
        return (drawerMenuList[pos].data as DrawerItem).showInMiniDrawer
    }

    fun setSelectedPosition(menuPos: Int) {
        if (!isShowInMiniDrawer(menuPos)) return
        if (lastSelectedIndex == menuPos) return
        if (lastSelectedIndex != -1) {
            changeMenuSelectedPosition(lastSelectedIndex, false)
        }
        lastSelectedIndex = menuPos
        changeMenuSelectedPosition(lastSelectedIndex, true)
        rv_full_menu.scrollToPosition(menuPos)
    }

    fun setBadge(menuPos: Int, count: Int, showBadge: Boolean = true) {
        checkPosition(menuPos)
        val obj = drawerMenuList[menuPos]
        val miniPosition = getMiniDrawerItemList().indexOf(obj)
        val data = obj.data as DrawerItem
        data.badgeCount = count
        data.showBadge = showBadge
        obj.data = data
        rv_full_menu.update(menuPos, obj)
        rv_mini_menu.update(miniPosition, obj)
    }

    fun removeBadge(menuPos: Int) {
        checkPosition(menuPos)
        setBadge(menuPos, 0, false)
    }

    fun getTitle(pos: Int): String {
        checkPosition(pos)
        return (drawerMenuList[pos].data as DrawerItem).title
    }

    private fun getMiniDrawerItemList(): List<DrawerItemBase> {
        drawerMenuList.filter {
            // todo remove title for mini drawer layout
            it.type !is DrawerItemType.TITLE
        }.filter {
            var data: DrawerItem? = null
            if (it.data is DrawerItem) {
                data = it.data as DrawerItem
            }
            // todo remove #showInMiniDrawer false but header item must be added
            it.type is DrawerItemType.HEADER || (data != null && data.showInMiniDrawer)
        }.run {
            return this
        }
    }

    fun openDrawer() {
        sliding_layout.openPane()
    }

    fun closeDrawer() {
        sliding_layout.closePane()
    }

    val isDrawerOpen: Boolean
        get() = sliding_layout.isOpen

    fun onBackPressed(): Boolean {
        if (isDrawerOpen) closeDrawer().run { return true }
        return false
    }
}
