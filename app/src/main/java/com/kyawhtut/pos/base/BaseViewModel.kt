package com.kyawhtut.pos.base

import androidx.lifecycle.ViewModel
import com.kyawhtut.pos.data.db.entity.UserEntity

abstract class BaseViewModel(private val repo: BaseRepository) :
    ViewModel() {

    fun isLogin() = repo.isLogin()

    var taxAmount: Int
        set(value) {
            repo.taxAmount = value
        }
        get() = repo.taxAmount

    var limitAmount: Int
        set(value) {
            repo.limitAmount = value
        }
        get() = repo.limitAmount

    fun logout() {
        repo.logout()
    }

    fun getCurrentUser(): UserEntity? = repo.getCurrentUser()
}