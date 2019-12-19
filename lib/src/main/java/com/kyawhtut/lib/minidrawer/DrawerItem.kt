package com.kyawhtut.lib.minidrawer

import com.kyawhtut.lib.R

sealed class DrawerItemType(val type: String) {
    object HEADER : DrawerItemType("header")
    object TITLE : DrawerItemType("title")
    object ITEM : DrawerItemType("item")
    object DIVIDER : DrawerItemType("divider")
}

class DrawerItemBase private constructor(
    val type: DrawerItemType,
    var data: Any
) {
    class Builder {
        var type: DrawerItemType = DrawerItemType.ITEM
        var data: Any = Object()

        fun build() = DrawerItemBase(type, data)
    }
}

class DrawerItemBaseList : ArrayList<DrawerItemBase>() {

    fun drawerItemBase(block: DrawerItemBase.Builder.() -> Unit) {
        add(DrawerItemBase.Builder().apply(block).build())
    }

    fun drawerHeader(block: DrawerHeader.Builder.() -> Unit) {
        add(drawerBase {
            type = DrawerItemType.HEADER
            data = DrawerHeader.Builder().apply(block).build()
        })
    }

    fun drawerItem(block: DrawerItem.Builder.() -> Unit) {
        add(drawerBase {
            type = DrawerItemType.ITEM
            data = DrawerItem.Builder().apply(block).build()
        })
    }

    fun drawerDivider(block: DrawerDivider.Builder.() -> Unit) {
        add(drawerBase {
            type = DrawerItemType.DIVIDER
            data = DrawerDivider.Builder().apply(block).build()
        })
    }

    fun drawerTitle(block: DrawerTitle.Builder.() -> Unit) {
        add(drawerBase {
            type = DrawerItemType.TITLE
            data = DrawerTitle.Builder().apply(block).build()
        })
    }
}

class DrawerHeader private constructor(
    var icon: Int,
    var title: String,
    var description: String,
    var showShadow: Boolean
) {
    class Builder {
        var icon: Int = R.drawable.ic_drawer_menu_default
        var title = ""
        var description = ""
        var showShadow = true

        fun build() = DrawerHeader(icon, title, description, showShadow)
    }
}

class DrawerTitle private constructor(
    var title: String
) {
    class Builder {
        var title = ""
        fun build() = DrawerTitle(title)
    }
}

class DrawerDivider private constructor(
    val showInMiniDrawer: Boolean
) {
    class Builder {
        var showInMiniDrawer: Boolean = false

        fun build() = DrawerDivider(showInMiniDrawer)
    }
}

class DrawerItem private constructor(
    var icon: Int,
    var title: String,
    var description: String,
    var isSelected: Boolean,
    var showBadge: Boolean,
    var badgeCount: Int,
    var showInMiniDrawer: Boolean
) {
    class Builder {
        var icon: Int = R.drawable.ic_drawer_menu_default
        var title: String = ""
        var description: String = ""
        var isSelected: Boolean = false
        var showBadge: Boolean = false
        var badgeCount: Int = 0
        var showInMiniDrawer: Boolean = true

        fun build() = DrawerItem(
            icon,
            title,
            description,
            isSelected,
            showBadge,
            badgeCount,
            showInMiniDrawer
        )
    }
}

fun drawerBaseList(block: DrawerItemBaseList.() -> Unit) = DrawerItemBaseList().apply(block)

fun drawerBase(block: DrawerItemBase.Builder.() -> Unit) =
    DrawerItemBase.Builder().apply(block).build()

fun drawerHeader(block: DrawerHeader.Builder.() -> Unit) = drawerBase {
    type = DrawerItemType.HEADER
    data = DrawerHeader.Builder().apply(block).build()
}

fun drawerItem(block: DrawerItem.Builder.() -> Unit) = drawerBase {
    type = DrawerItemType.ITEM
    data = DrawerItem.Builder().apply(block).build()
}

fun drawerDivider(block: DrawerDivider.Builder.() -> Unit) = drawerBase {
    type = DrawerItemType.DIVIDER
    data = DrawerDivider.Builder().apply(block).build()
}

fun drawerTitle(block: DrawerTitle.Builder.() -> Unit) = drawerBase {
    type = DrawerItemType.TITLE
    data = DrawerTitle.Builder().apply(block).build()
}

