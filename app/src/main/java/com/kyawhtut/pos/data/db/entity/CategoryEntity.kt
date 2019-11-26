package com.kyawhtut.pos.data.db.entity

import androidx.recyclerview.widget.DiffUtil
import androidx.room.*
import com.kyawhtut.pos.utils.ProductType
import com.kyawhtut.pos.utils.toColor
import org.joda.time.DateTime
import java.util.*

@Entity(tableName = "category_table")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    val id: Int,
    @ColumnInfo(name = "category_name")
    val categoryName: String,
    @ColumnInfo(name = "category_color")
    val categoryColor: Int,
    @ColumnInfo(name = "category_text_color")
    val categoryTextColor: Int,
    @ColumnInfo(name = "category_sell_count")
    val categorySellCount: Int,
    @ColumnInfo(name = "created_user_id")
    @ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["user_id"],
        childColumns = ["created_user_id"]
    )
    val createdUserId: Int,
    @ColumnInfo(name = "updated_user_id")
    @ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["user_id"],
        childColumns = ["updated_user_id"]
    )
    val updatedUserId: Int,
    @ColumnInfo(name = "category_available")
    val categoryAvailable: Int,
    @ColumnInfo(name = "created_date")
    val createdDate: String,
    @ColumnInfo(name = "updated_date")
    val updatedDate: String
) {

    @Ignore
    var categoryItemCount: Int = 0

    fun getCategoryItemCount(): String =
        if (categoryItemCount == 0) "Empty" else "$categoryItemCount"

    fun category2Product() = product {
        id = this@CategoryEntity.id
        productName = this@CategoryEntity.categoryName
        productColor = this@CategoryEntity.categoryColor
        productTextColor = this@CategoryEntity.categoryTextColor
        productCount = this@CategoryEntity.categoryItemCount
        type = ProductType.Category
    }

    companion object {

        fun categoryList2ProductList(data: List<CategoryWithProduct>): List<ProductEntity> {
            return data.map {
                it.convert2Product()
            }
        }

        val diffUtil = object : DiffUtil.ItemCallback<CategoryEntity>() {
            override fun areItemsTheSame(
                oldItem: CategoryEntity,
                newItem: CategoryEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CategoryEntity,
                newItem: CategoryEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class CategoryBuilder {
    var id: Int = 0
    var categoryName: String = ""
    var categoryColor: Int = "#8090fd".toColor()
    var categoryTextColor = "#ffffff".toColor()
    var categorySellCount = 0
    var createdUserId: Int = 0
    var updatedUserId: Int = 0
    var categoryAvailable: Int = 1
    var categoryCount: Int = 0
    var createdDate = DateTime.now().toString("dd-MM-yyyy", Locale.ENGLISH)
    var updatedDate = DateTime.now().toString("dd-MM-yyyy", Locale.ENGLISH)

    fun build() =
        CategoryEntity(
            id,
            categoryName,
            categoryColor,
            categoryTextColor,
            categorySellCount,
            createdUserId,
            updatedUserId,
            categoryAvailable,
            createdDate,
            updatedDate
        ).apply {
            this.categoryItemCount = categoryCount
        }
}

fun category(block: CategoryBuilder.() -> Unit): CategoryEntity =
    CategoryBuilder().apply(block).build()