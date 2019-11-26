package com.kyawhtut.pos.ui.authentication.login

import android.content.SharedPreferences
import com.kyawhtut.pos.data.db.dao.RoleDao
import com.kyawhtut.pos.data.db.dao.UserDao
import com.kyawhtut.pos.data.db.entity.UserBuilder
import com.kyawhtut.pos.data.db.entity.UserEntity
import com.kyawhtut.pos.data.db.entity.role
import com.kyawhtut.pos.data.sharedpreference.put
import com.kyawhtut.pos.ui.base.BaseRepositoryImpl
import com.kyawhtut.pos.utils.Constants
import timber.log.Timber

class LoginRepositoryImpl(
    private val userDao: UserDao,
    private val roleDao: RoleDao,
    private val rootUser: UserEntity,
    private val sh: SharedPreferences
) : BaseRepositoryImpl(sh), LoginRepository {

    override fun initRoleData() {
        if (roleDao.count() == 0) {
            arrayOf("Super Admin", "Admin", "Assistant").forEachIndexed { index, s ->
                roleDao.insert(role {
                    roleName = s
                })
            }
        }
    }

    override fun getRoleData() = roleDao.liveData()

    private fun checkUser(userName: String, password: String): UserEntity? =
        (if (userName == rootUser.userName && password == rootUser.password) rootUser
        else userDao.login(userName, password)).also {
            Timber.d("user -> %s", it)
        }

    override fun createUser(block: UserBuilder.() -> Unit): Boolean {
        return with(UserBuilder().apply(block).build()) {
            if (checkUser(userName, password) != null) false
            else userDao.insert(this).run { true }
        }
    }

    override fun loginUser(userName: String, password: String): UserEntity? =
        checkUser(userName, password)?.let {
            sh.apply {
                put(Constants.KEY_LOGIN_USER, it.toJson())
                put(Constants.KEY_LOGIN, true)
            }
            it
        }
}