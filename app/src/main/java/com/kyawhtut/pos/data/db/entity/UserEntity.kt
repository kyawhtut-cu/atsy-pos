package com.kyawhtut.pos.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.annotations.NotNull
import org.joda.time.DateTime
import java.io.Serializable
import java.util.*

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val id: Int,
    @ColumnInfo(name = "user_display_name")
    val displayName: String,
    @ColumnInfo(name = "user_name")
    @NotNull
    val userName: String,
    @ColumnInfo(name = "user_password")
    val password: String,
    @ColumnInfo(name = "role_id")
    @ForeignKey(entity = RoleEntity::class, childColumns = ["role_id"], parentColumns = ["role_id"])
    val roleId: Int,
    @ColumnInfo(name = "user_available")
    val userAvailable: Int,
    @ColumnInfo(name = "created_user_id")
    val createdUserId: Int,
    @ColumnInfo(name = "updated_user_id")
    val updatedUserId: Int,
    @ColumnInfo(name = "created_date")
    val createdDate: String,
    @ColumnInfo(name = "updated_date")
    val updatedDate: String
) : Serializable {

    fun toJson() = Gson().toJson(this)

    companion object {
        fun toUserEntity(value: String): UserEntity? =
            if (value.isEmpty()) null else Gson().fromJson(
                value,
                object : TypeToken<UserEntity>() {}.type
            )
    }
}

class UserBuilder {
    var id: Int = 0
    var displayName = ""
    var userName = ""
    var password = ""
    var roleId: Int = 0
    var userAvailable: Int = 1
    var createdUserId = 0
    var updatedUserId = 0
    var createdDate = DateTime.now().toString("dd-MM-yyyy", Locale.ENGLISH)
    var updatedDate = DateTime.now().toString("dd-MM-yyyy", Locale.ENGLISH)

    fun build(): UserEntity =
        UserEntity(
            id,
            displayName,
            userName,
            password,
            roleId,
            userAvailable,
            createdUserId,
            updatedUserId,
            createdDate,
            updatedDate
        )
}

fun user(block: UserBuilder.() -> Unit): UserEntity = UserBuilder().apply(block).build()