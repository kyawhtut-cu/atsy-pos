package com.kyawhtut.pos.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime
import java.util.*

@Entity(tableName = "role_table")
data class RoleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "role_id")
    val id: Int,
    @ColumnInfo(name = "role_name")
    val roleName: String,
    @ColumnInfo(name = "created_date")
    val createdDate: String,
    @ColumnInfo(name = "updated_date")
    val updatedDate: String
)

class RoleBuilder {
    var id: Int = 0
    var roleName = ""
    var createdDate = DateTime.now().toString("dd-MM-yyyy", Locale.ENGLISH)
    var updatedDate = DateTime.now().toString("dd-MM-yyyy", Locale.ENGLISH)

    fun build(): RoleEntity = RoleEntity(id, roleName, createdDate, updatedDate)
}

fun role(block: RoleBuilder.() -> Unit): RoleEntity = RoleBuilder().apply(block).build()