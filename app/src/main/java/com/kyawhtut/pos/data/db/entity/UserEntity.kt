package com.kyawhtut.pos.data.db.entity

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kyawhtut.pos.base.BaseColumn
import com.kyawhtut.pos.data.vo.TableCellVO
import com.kyawhtut.pos.data.vo.TableRowHeaderVO
import com.kyawhtut.pos.data.vo.rowHeader
import com.kyawhtut.pos.data.vo.tableCellList
import com.kyawhtut.pos.utils.getCurrentTimeString
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
    var createdDate = getCurrentTimeString()

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
            getCurrentTimeString()
        )
}

class UserColumn : BaseColumn(
    "Display Name",
    "User Name",
    "User Password",
    "Role",
    "Status",
    "Created User Name",
    "Updated User Name",
    "Created Date",
    "Updated Date",
    "Action"
) {

    companion object {
        fun getRowHeaderList(list: List<UserTable>): List<TableRowHeaderVO> {
            list.map {
                rowHeader {
                    data = "${it.user.id}"
                }
            }.run {
                return this
            }
        }

        private fun getTableCellList(userTable: UserTable): List<TableCellVO> = tableCellList {
            tableCell {
                cellId = "display_name"
                data = userTable.user.displayName
            }
            tableCell {
                cellId = "user_name"
                data = userTable.user.userName
            }
            tableCell {
                cellId = "user_password"
                data = userTable.user.password
            }
            tableCell {
                cellId = "user_role"
                data = userTable.roleName
            }
            tableCell {
                cellId = "user_available"
                data = userTable.user.userAvailable
            }
            tableCell {
                cellId = "created_user_name"
                data = userTable.createUserName ?: ""
            }
            tableCell {
                cellId = "updated_user_name"
                data = userTable.updatedUserName ?: ""
            }
            tableCell {
                cellId = "created_date"
                data = userTable.user.createdDate
            }
            tableCell {
                cellId = "updated_date"
                data = userTable.user.updatedDate
            }
            tableCell {
                cellId = "${userTable.user.id}"
                data = 0
            }
        }

        fun getTableCellList(list: List<UserTable>): List<List<TableCellVO>> {
            list.map {
                getTableCellList(it)
            }.run {
                return this
            }
        }
    }
}

data class UserTable(
    @Embedded
    val user: UserEntity,
    @Relation(
        parentColumn = "created_user_id",
        entityColumn = "user_id",
        entity = UserEntity::class,
        projection = ["user_display_name"]
    )
    var createUserName: String?,
    @Relation(
        parentColumn = "updated_user_id",
        entityColumn = "user_id",
        entity = UserEntity::class,
        projection = ["user_display_name"]
    )
    var updatedUserName: String?,
    @Relation(
        parentColumn = "role_id",
        entityColumn = "role_id",
        entity = RoleEntity::class,
        projection = ["role_name"]
    )
    val roleName: String
)

fun user(block: UserBuilder.() -> Unit): UserEntity = UserBuilder().apply(block).build()