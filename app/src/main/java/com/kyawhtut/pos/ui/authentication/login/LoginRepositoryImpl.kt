package com.kyawhtut.pos.ui.authentication.login

import android.content.SharedPreferences
import androidx.lifecycle.toLiveData
import com.kyawhtut.pos.base.BaseRepositoryImpl
import com.kyawhtut.pos.data.db.dao.RoleDao
import com.kyawhtut.pos.data.db.dao.UserDao
import com.kyawhtut.pos.data.db.entity.UserBuilder
import com.kyawhtut.pos.data.db.entity.UserEntity
import com.kyawhtut.pos.data.db.entity.role
import com.kyawhtut.pos.data.sharedpreference.put
import com.kyawhtut.pos.utils.Constants

class LoginRepositoryImpl(
    private val userDao: UserDao,
    private val roleDao: RoleDao,
    rootUser: UserEntity,
    sh: SharedPreferences
) : BaseRepositoryImpl(sh, rootUser), LoginRepository {

    override fun initRoleData(roleList: List<String>) {
        if (roleDao.count() == 0) {
            roleList.forEachIndexed { index, s ->
                roleDao.insert(role {
                    roleName = s
                })
            }
        }
    }

    override fun getRoleData() = roleDao.flowable().map {
        it.filter {
            (getCurrentUser()?.roleId ?: 0) <= it.id
        }
    }.toLiveData()

    private fun checkUser(userName: String, password: String): UserEntity? =
        (if (userName == rootUser.userName && password == rootUser.password) rootUser
        else userDao.login(userName, password))

    override fun createUser(block: UserBuilder.() -> Unit): Boolean {
        return with(UserBuilder().apply(block).build()) {
            if (checkUser(userName, password) != null) false
            else userDao.insert(this).run { true }
        }
    }

    override fun updateUser(block: UserBuilder.() -> Unit) {
        userDao.update(UserBuilder().apply(block).build())
    }

    override fun getUserById(id: Int): UserEntity = userDao.get(id)

    override fun loginUser(userName: String, password: String): UserEntity? =
        checkUser(userName, password)?.let {
            sh.apply {
                put(Constants.KEY_LOGIN_USER, it.toJson())
                put(Constants.KEY_LOGIN, true)
            }
            it
        }
}