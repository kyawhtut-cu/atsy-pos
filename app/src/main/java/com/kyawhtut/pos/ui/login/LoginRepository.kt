package com.kyawhtut.pos.ui.login

import androidx.lifecycle.LiveData
import com.kyawhtut.pos.base.BaseRepository
import com.kyawhtut.pos.data.db.entity.RoleEntity
import com.kyawhtut.pos.data.db.entity.UserBuilder
import com.kyawhtut.pos.data.db.entity.UserEntity

interface LoginRepository : BaseRepository {

    fun insertTrialAdmin()

    fun canDeleteUserById(id: Int): Boolean

    fun initRoleData(roleList: List<String>)

    fun getRoleData(): LiveData<List<RoleEntity>>

    fun createUser(block: UserBuilder.() -> Unit): Boolean

    fun updateUser(block: UserBuilder.() -> Unit)

    fun getUserById(id: Int): UserEntity

    fun loginUser(userName: String, password: String): UserEntity?
}