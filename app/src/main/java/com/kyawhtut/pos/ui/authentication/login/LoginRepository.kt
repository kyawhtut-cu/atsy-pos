package com.kyawhtut.pos.ui.authentication.login

import androidx.lifecycle.LiveData
import com.kyawhtut.pos.data.db.entity.RoleEntity
import com.kyawhtut.pos.data.db.entity.UserBuilder
import com.kyawhtut.pos.data.db.entity.UserEntity
import com.kyawhtut.pos.ui.base.BaseRepository

interface LoginRepository : BaseRepository {

    fun initRoleData()

    fun getRoleData(): LiveData<List<RoleEntity>>

    fun createUser(block: UserBuilder.() -> Unit): Boolean

    fun loginUser(userName: String, password: String): UserEntity?
}