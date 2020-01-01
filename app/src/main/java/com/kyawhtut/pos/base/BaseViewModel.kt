package com.kyawhtut.pos.base

import androidx.lifecycle.ViewModel
import com.kyawhtut.pos.data.db.entity.UserEntity

abstract class BaseViewModel(private val repo: BaseRepository) :
    ViewModel() {

    fun isLogin() = repo.isLogin()

    var taxAmount = repo.taxAmount
        set(value) {
            repo.taxAmount = value
        }

    var limitAmount = repo.limitAmount
        set(value) {
            repo.limitAmount = value
        }

    fun logout() {
        repo.logout()
    }

    fun getCurrentUser(): UserEntity? = repo.getCurrentUser()
}