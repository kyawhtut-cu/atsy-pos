package com.kyawhtut.pos.ui.authentication.login

import androidx.lifecycle.LiveData
import com.kyawhtut.pos.data.db.entity.RoleEntity
import com.kyawhtut.pos.data.db.entity.UserEntity
import com.kyawhtut.pos.ui.base.BaseViewModel

class LoginViewModel(private val repo: LoginRepository) : BaseViewModel(repo) {

    var userName: String = ""
    var userPassword: String = ""
    var userDisplayName: String = ""
    var userRole: Int = 0

    init {
        repo.initRoleData()
    }

    fun loginUser(): UserEntity? {
        return repo.loginUser(userName, userPassword)
    }

    fun getRoleData(): LiveData<List<RoleEntity>> = repo.getRoleData()

    fun createUser(callback: (Boolean) -> Unit) {
        callback(
            repo.createUser {
                userName = this@LoginViewModel.userName
                password = this@LoginViewModel.userPassword
                displayName = userDisplayName
                roleId = userRole
            }
        )
    }
}