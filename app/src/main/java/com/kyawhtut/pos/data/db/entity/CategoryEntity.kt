package com.kyawhtut.pos.data.db.entity

import androidx.recyclerview.widget.DiffUtil
import androidx.room.*
import com.kyawhtut.pos.base.BaseColumn
import com.kyawhtut.pos.data.vo.TableCellVO
import com.kyawhtut.pos.data.vo.TableRowHeaderVO
import com.kyawhtut.pos.data.vo.rowHeader
import com.kyawhtut.pos.data.vo.tableCellList
import com.kyawhtut.pos.utils.ProductType
import com.kyawhtut.pos.utils.getCurrentTimeString
import com.kyawhtut.pos.utils.toColor

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
    var createdDate = getCurrentTimeString()

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
            getCurrentTimeString()
        ).apply {
            this.categoryItemCount = categoryCount
        }
}

class CategoryColumn : BaseColumn(
    "Category Name",
    "Color",
    "Text Color",
    "Sell Count",
    "Created User Name",
    "Updated User Name",
    "Status",
    "Product Count",
    "Created Date",
    "Updated Date",
    "Action"
) {

    companion object {
        fun getRowHeaderList(
            list: List<CategoryTable>
        ): List<TableRowHeaderVO> {
            list.map {
                rowHeader {
                    data = "${it.categoryEntity.id}"
                }
            }.run {
                return this
            }
        }

        private fun getTableCellList(cat: CategoryTable): MutableList<TableCellVO> = tableCellList {
            tableCell {
                cellId = "Category Name"
                data = cat.categoryEntity.categoryName
            }
            tableCell {
                cellId = "Category color"
                data = cat.categoryEntity.categoryColor
            }
            tableCell {
                cellId = "Category Text Color"
                data = cat.categoryEntity.categoryTextColor
            }
            tableCell {
                cellId = "Category Count"
                data = "${cat.categoryEntity.categorySellCount}"
            }
            tableCell {
                cellId = "Created User"
                data = cat.createdUser ?: ""
            }
            tableCell {
                cellId = "Update User"
                data = cat.updatedUser ?: ""
            }
            tableCell {
                cellId = "Category Available"
                data = cat.categoryEntity.categoryAvailable
            }
            tableCell {
                cellId = "Category count"
                data = "${cat.productCount.size}"
            }
            tableCell {
                cellId = "Created Date"
                data = cat.categoryEntity.createdDate
            }
            tableCell {
                cellId = "Update Date"
                data = cat.categoryEntity.updatedDate
            }
            tableCell {
                cellId = "${cat.categoryEntity.id}"
                data = cat.productCount.size
            }
        }

        fun getTableCellList(
            list: List<CategoryTable>
        ): List<List<TableCellVO>> {
            list.map {
                getTableCellList(it)
            }.run {
                return this
            }
        }
    }
}

data class CategoryTable(
    @Embedded
    val categoryEntity: CategoryEntity,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "category_id",
        entity = ProductEntity::class,
        projection = ["product_name"]
    )
    val productCount: List<String>,
    @Relation(
        parentColumn = "created_user_id",
        entityColumn = "user_id",
        projection = ["user_display_name"],
        entity = UserEntity::class
    )
    var createdUser: String?,
    @Relation(
        parentColumn = "updated_user_id",
        entityColumn = "user_id",
        projection = ["user_display_name"],
        entity = UserEntity::class
    )
    var updatedUser: String?
)

class CategoryTableBuilder {
    var categoryEntity = CategoryBuilder().build()
    var productCount = mutableListOf<String>()
    var createUser = ""
    var updatedUser = ""

    fun build() = CategoryTable(categoryEntity, productCount, createUser, updatedUser)
}

fun category(block: CategoryBuilder.() -> Unit): CategoryEntity =
    CategoryBuilder().apply(block).build()