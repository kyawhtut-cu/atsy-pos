package com.kyawhtut.pos.ui.home

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import com.kyawhtut.lib.minidrawer.*
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseRepositoryImpl
import com.kyawhtut.pos.data.db.dao.CartDao
import com.kyawhtut.pos.data.db.dao.ProductDao
import com.kyawhtut.pos.data.db.entity.UserEntity
import com.kyawhtut.pos.utils.getIntList
import com.kyawhtut.pos.utils.getStringList
import com.kyawhtut.pos.utils.toBoolean

class HomeRepositoryImpl(
    sh: SharedPreferences,
    rootUser: UserEntity,
    private val cartDao: CartDao,
    private val productDao: ProductDao
) : BaseRepositoryImpl(sh, rootUser), HomeRepository {

    override val isLowerItem: LiveData<Int>
        get() = productDao.getLowerItemList(limitAmount).map {
            it.size
        }.toLiveData()

    override fun registerSharedPreference(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sh.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun unregisterSharedPreference(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sh.unregisterOnSharedPreferenceChangeListener(listener)
    }

    private var functionIconList = arrayOf(
        R.drawable.ic_home_black,
        R.drawable.ic_database_black,
        R.drawable.ic_shopping_bag_black,
        R.drawable.ic_bundle_white,
        R.drawable.ic_worker_black,
        R.drawable.ic_analysis_black,
        R.drawable.ic_sale_black,
        R.drawable.ic_user_group_black,
        R.drawable.ic_setting_black,
        R.drawable.ic_logout_black
    )

    override val isCartDataHas: LiveData<Int> = cartDao.getCartCount().toLiveData()

    override fun getDrawerMenuList(
        context: Context,
        removeUnavailableFunction: Boolean
    ): List<DrawerItemBase> {
        if (isLogin()) {
            val role = when (getCurrentUser()?.roleId) {
                1 -> R.array.super_admin_permission
                2, 3 -> R.array.admin_permission
                4 -> R.array.assistant_permission
                else -> R.array.sale_permission
            }
            drawerBaseList {
                context.getIntList(role).forEachIndexed { index, i ->
                    if (i == 1) {
                        val menuTitle = context.getStringList(R.array.function)[index]
                        val menuDescription =
                            context.getStringList(R.array.function_description)[index]
                        val menuShowInMiniDrawer =
                            context.getIntList(R.array.show_in_mini_drawer)[index]
                        drawerItem {
                            icon = functionIconList[index]
                            title = menuTitle
                            description = menuDescription
                            isSelected = index == 0
                            showBadge = menuTitle == "Sale" && isCartDataHas.value != 0
                            badgeCount = if (menuTitle == "Sale") isCartDataHas.value ?: 0 else 0
                            showInMiniDrawer = menuShowInMiniDrawer.toBoolean()
                        }
                    }
                }
            }.toMutableList().run {
                this.add(0, drawerHeader {
                    icon = R.drawable.ic_worker_black
                    title = getCurrentUser()?.displayName ?: ""
                    description =
                        String.format("%s/%s", getCurrentUser()?.id, getCurrentUser()?.userName)
                })
                this.add(this.size - 2, drawerDivider {})
                this.add(this.size - 2, drawerTitle {
                    title = "Setting"
                })
                return this
            }
        }
        drawerBaseList {
            drawerHeader {
                icon = R.drawable.ic_worker_black
                title = "POS System"
                description = "POS System"
            }
            drawerItem {
                icon = R.drawable.ic_setting_black
                title = "Setting"
                description =
                    "Application နှင့်သက်ဆိုင်သည့် အချက်အလက်များပြင်ဆင်ရန် အတွက်အသုံးပြုပါသည်။"
                showInMiniDrawer = false
            }
        }.run {
            return this
        }
    }
}