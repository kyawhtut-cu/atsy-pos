package com.kyawhtut.pos.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kyawhtut.lib.minidrawer.drawerDivider
import com.kyawhtut.lib.minidrawer.drawerHeader
import com.kyawhtut.lib.minidrawer.drawerItem
import com.kyawhtut.lib.minidrawer.drawerTitle
import com.kyawhtut.pos.R
import kotlinx.android.synthetic.main.mini_drawer_test.*

class MiniDrawerTest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mini_drawer_test)

        arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).map {
            if (it != 5 && it != 9) drawerItem {
                icon = when (it) {
                    1 -> R.drawable.ic_database_black
                    2 -> R.drawable.ic_shopping_bag_black
                    3 -> R.drawable.ic_bundle_white
                    4 -> R.drawable.ic_worker_black
                    6 -> R.drawable.ic_analysis_black
                    7 -> R.drawable.ic_shopping_bag_black
                    8 -> R.drawable.ic_user_group_black
                    10 -> R.drawable.ic_setting_black
                    else -> R.drawable.ic_drawer_menu_default
                }
                title = "Function Title $it"
                description = "Function Description $it"
                isSelected = it == 1
                showBadge = it == 1
                badgeCount = it
                showInMiniDrawer = it != 10
            } else if (it == 9) drawerTitle {
                title = "Setting"
            } else drawerDivider {
                showInMiniDrawer = true
            }
        }.toMutableList().run {
            this.add(0, drawerHeader {
                title = getString(R.string.app_name)
                description = getString(R.string.app_name)
                icon = R.drawable.ic_worker_black
            })
            mini_drawer.drawerMenuList = this
        }

        mini_drawer.onMenuItemClick = { type, index ->

        }

        btn_remove_badge.setOnClickListener {
            mini_drawer.removeBadge(1)
        }

        btn_set_badge.setOnClickListener {
            mini_drawer.setBadge(2, 25)
        }
    }

    override fun onBackPressed() {
        if (mini_drawer.onBackPressed()) return
        super.onBackPressed()
    }
}