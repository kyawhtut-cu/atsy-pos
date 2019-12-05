package com.kyawhtut.pos.ui.customer

import com.kyawhtut.pos.base.BaseViewModel
import com.kyawhtut.pos.utils.getCurrentTimeString
import com.kyawhtut.pos.utils.toBoolean
import com.kyawhtut.pos.utils.toInt

class CustomerViewModel(private val repo: CustomerRepository) : BaseViewModel(repo) {

    var cId = 0
        set(value) {
            field = value
            if (value != 0)
                getCustomer()
        }
    var name = ""
    var phone = mutableListOf<String>()
    var address = ""
    var status = true
    var createdUser = getCurrentUser()?.id ?: 0
    var createDate = getCurrentTimeString()

    fun getCustomer() {
        with(repo.getCustomerById(cId)) {
            name = customerName
            phone = customerPhone.split(",").toMutableList()
            address = customerAddress
            status = customerAvailable.toBoolean()
            createdUser = createdUserId
            createDate = createdDate
        }
    }

    fun canDeleteCustomer() = repo.canDeleteCustomer(cId)

    fun insert() {
        repo.insertCustomer {
            customerName = name
            customerPhone = phone.joinToString(",")
            customerAddress = address
            customerAvailable = status.toInt()
            createdUserId = getCurrentUser()?.id ?: 0
            updatedUserId = getCurrentUser()?.id ?: 0
            createdDate = getCurrentTimeString()
            updatedDate = getCurrentTimeString()
        }
    }

    fun update() {
        repo.updateCustomer {
            id = cId
            customerName = name
            customerPhone = phone.joinToString(",")
            customerAddress = address
            customerAvailable = status.toInt()
            createdUserId = createdUser
            updatedUserId = getCurrentUser()?.id ?: 0
            createdDate = createDate
            updatedDate = getCurrentTimeString()
        }
    }

    fun delete() {
        repo.deleteItemById(cId)
    }
}