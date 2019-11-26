package com.kyawhtut.pos.ui.base

import androidx.lifecycle.ViewModel
import com.kyawhtut.pos.data.db.entity.UserEntity

abstract class BaseViewModel(private val repo: BaseRepository) :
    ViewModel() {

    fun isLogin() = repo.isLogin()

    fun logout() {
        repo.logout()
    }

    fun getCurrentUser(): UserEntity? = repo.getCurrentUser()
}