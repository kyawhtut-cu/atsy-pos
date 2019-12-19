package com.kyawhtut.pos.ui.login

import androidx.lifecycle.LiveData
import com.kyawhtut.pos.base.BaseViewModel
import com.kyawhtut.pos.data.db.entity.RoleEntity
import com.kyawhtut.pos.data.db.entity.UserEntity
import com.kyawhtut.pos.utils.getCurrentTimeString

class LoginViewModel(private val repo: LoginRepository) : BaseViewModel(repo) {

    var userId: Int = 0
    var userName: String = ""
    var userPassword: String = ""
    var userDisplayName: String = ""
    var userRole: Int = 0
    var createdUserId: Int = getCurrentUser()?.id ?: 0
    var createdDate: String = getCurrentTimeString()

    fun initRoleData(roleList: List<String>) = repo.initRoleData(roleList)

    fun loginUser(): UserEntity? {
        return repo.loginUser(userName, userPassword)
    }

    fun canDeleteUserById(id: Int) = repo.canDeleteUserById(id)

    fun getUserById(id: Int) = repo.getUserById(id)

    fun getRoleData(): LiveData<List<RoleEntity>> = repo.getRoleData()

    fun createUser(callback: (Boolean) -> Unit) {
        callback(
            repo.createUser {
                userName = this@LoginViewModel.userName
                password = this@LoginViewModel.userPassword
                displayName = userDisplayName
                roleId = userRole
                createdUserId = this@LoginViewModel.createdUserId
                updatedUserId = this@LoginViewModel.createdUserId
            }
        )
    }

    fun updateUser(callback: () -> Unit) {
        repo.updateUser {
            id = userId
            userName = this@LoginViewModel.userName
            password = this@LoginViewModel.userPassword
            displayName = this@LoginViewModel.userDisplayName
            roleId = this@LoginViewModel.userRole
            createdUserId = this@LoginViewModel.createdUserId
            updatedUserId = getCurrentUser()?.id ?: this@LoginViewModel.createdUserId
            createdDate = this@LoginViewModel.createdDate
            updatedDate = getCurrentTimeString()
        }
        callback()
    }

    fun deleteUser() {
        repo.deleteItemById(userId)
    }
}