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
import com.kyawhtut.fontchooserlib.util.FontUtils
import com.kyawhtut.pos.R
import com.kyawhtut.pos.ui.authentication.login.LoginActivity
import com.kyawhtut.pos.utils.startActivity

abstract class BaseActivity<V : BaseViewModel>(
    @LayoutRes private val layoutId: Int,
    @MenuRes private val menuId: Int? = null,
    @IdRes private val toolbar: Int? = null,
    private val checkLogin: Boolean = true
) : AppCompatActivity() {

    abstract fun setup(bundle: Bundle)
    open fun onClickMenu(id: Int) {}
    open fun onClickBack() {}
    abstract val viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FontChoose.init(this, R.drawable.ic_g_letter)
        if (checkLogin) checkLogin()
        setContentView(layoutId)
        if (toolbar != null)
            setSupportActionBar(findViewById(toolbar))

        setup(intent.extras ?: Bundle())
    }

    protected fun checkLogin() {
        if (!FontUtils.Builder(this).build().isFirst && !viewModel.isLogin()) startActivity<LoginActivity>().also {
            finish()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(FontChoose.updateBaseContextLocale(newBase))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (menuId != null)
            menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> onClickBack().run {
                true
            }
            else -> onClickMenu(item.itemId).run {
                true
            }
        }
    }
}
