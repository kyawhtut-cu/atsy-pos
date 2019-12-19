package com.kyawhtut.pos.base

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import com.kyawhtut.fontchooserlib.FontChoose
import com.kyawhtut.pos.R
import com.kyawhtut.pos.utils.getDrawableValue
import com.mikepenz.actionitembadge.library.ActionItemBadge

abstract class BaseActivity(
    @LayoutRes private val layoutId: Int,
    @MenuRes private val menuId: Int? = null,
    @IdRes private val toolbar: Int? = null,
    private val isBackAction: Boolean = false
) : AppCompatActivity() {

    abstract fun setup(savedInstanceState: Bundle?, bundle: Bundle)
    open fun onClickMenu(id: Int) {}
    private var menuItemCart: MenuItem? = null
    protected var badgeCount: Int = 0
        set(value) {
            field = value
            if (menuItemCart != null)
                ActionItemBadge.update(
                    this,
                    menuItemCart,
                    getDrawableValue(R.drawable.ic_shopping_bag_white),
                    ActionItemBadge.BadgeStyles.RED,
                    value
                )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FontChoose.init(this, R.drawable.ic_g_letter)
        setContentView(layoutId)
        if (toolbar != null)
            setSupportActionBar(findViewById(toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(isBackAction)

        setup(savedInstanceState, intent.extras ?: Bundle())
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(FontChoose.updateBaseContextLocale(newBase))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (menuId != null) {
            menuInflater.inflate(menuId, menu)
            menuItemCart = menu.findItem(R.id.action_cart)
            if (menuItemCart != null)
                badgeCount = badgeCount
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> onBackPressed().run {
                true
            }
            else -> onClickMenu(item.itemId).run {
                true
            }
        }
    }
}
