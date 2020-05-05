package com.kyawhtut.pos.data.db.entity

import androidx.recyclerview.widget.DiffUtil
import androidx.room.*
import com.kyawhtut.pos.base.BaseColumn
import com.kyawhtut.pos.data.vo.TableCellVO
import com.kyawhtut.pos.data.vo.TableRowHeaderVO
import com.kyawhtut.pos.data.vo.rowHeader
import com.kyawhtut.pos.data.vo.tableCellList
import com.kyawhtut.pos.utils.ProductType
import com.kyawhtut.pos.utils.toBoolean
import com.kyawhtut.pos.utils.toInt
import org.joda.time.DateTime
import java.util.*

@Entity(tableName = "product_table")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    val id: Int,
    @ColumnInfo(name = "product_code")
    val productCode: String,
    @ColumnInfo(name = "product_name")
    val productName: String,
    @ColumnInfo(name = "product_description")
    val productDescription: String,
    @ColumnInfo(name = "product_price")
    val productPrice: Long,
    @ColumnInfo(name = "product_color")
    val productColor: Int,
    @ColumnInfo(name = "product_text_color")
    val productTextColor: Int,
    @ColumnInfo(name = "product_unit")
    val productUnit: String,
    @ColumnInfo(name = "product_count")
    val productCount: Int,
    @ColumnInfo(name = "product_retail_price")
    val productRetailPrice: Long,
    @ColumnInfo(name = "category_id")
    @ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["category_id"],
        childColumns = ["category_id"]
    )
    val categoryId: Int,
    @ColumnInfo(name = "product_available")
    val productAvailable: Int,
    @ColumnInfo(name = "show_alert_remain_amount")
    val productRemainAmountShow: Int,
    @ColumnInfo(name = "product_sell_count")
    val productSellCount: Int,
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
    @ColumnInfo(name = "created_date")
    val createdDate: String,
    @ColumnInfo(name = "updated_date")
    val updatedDate: String
) {

    @Ignore
    var type: ProductType = ProductType.Product

    fun getProductCountValue() = if (productCount == 0) "Empty" else "$productCount"

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ProductEntity>() {
            override fun areItemsTheSame(
                oldItem: ProductEntity,
                newItem: ProductEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ProductEntity,
                newItem: ProductEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class ProductBuilder {
    var id: Int = 0
    var productCode: String = ""
    var productName: String = ""
    var productDescription: String = ""
    var productPrice: Long = 0
    var productColor: Int = 0
    var productTextColor: Int = 0
    var productUnit: String = ""
    var productCount: Int = 0
    var productRetailPrice: Long = 0
    var categoryId: Int = 0
    var productAvailable = 1
    var productRemainAmountShow: Boolean = true
    var productSellCount: Int = 0
    var createdUserId = 0
    var updatedUserId = 0
    var type: ProductType = ProductType.Product
    var createdDate: String = DateTime.now().toString("dd-MM-yyyy", Locale.ENGLISH)
    var updatedDate: String = DateTime.now().toString("dd-MM-yyyy", Locale.ENGLISH)

    fun build() = ProductEntity(
        id,
        productCode,
        productName,
        productDescription,
        productPrice,
        productColor,
        productTextColor,
        productUnit,
        productCount,
        productRetailPrice,
        categoryId,
        productAvailable,
        productRemainAmountShow.toInt(),
        productSellCount,
        createdUserId,
        updatedUserId,
        createdDate,
        updatedDate
    ).apply {
        type = this@ProductBuilder.type
    }
}

class ProductColumn : BaseColumn(
    "Product Code",
    "Product Name",
    "Product Description",
    "Product Price",
    "Color",
    "Text Color",
    "Product Count",
    "Retail Price",
    "Sell Count",
    "Category Name",
    "Status",
    "Created User Name",
    "Updated User Name",
    "Show Alert",
    "Created Date",
    "Updated Date",
    "Action"
) {
    companion object {
        fun getRowHeaderList(list: List<ProductTable>): List<TableRowHeaderVO> {
            list.map {
                rowHeader {
                    data = "${it.product.id}"
                }
            }.run {
                return this
            }
        }

        private fun getTableCellList(list: ProductTable): List<TableCellVO> {
            tableCellList {
                tableCell {
                    cellId = "product_code"
                    data = list.product.productCode
                }
                tableCell {
                    cellId = "product_name"
                    data = list.product.productName
                }
                tableCell {
                    cellId = "product_description"
                    data = list.product.productDescription
                }
                tableCell {
                    cellId = "product_price"
                    data = "${list.product.productPrice}"
                }
                tableCell {
                    cellId = "product_color"
                    data = list.product.productColor
                }
                tableCell {
                    cellId = "product_text_color"
                    data = list.product.productTextColor
                }
                tableCell {
                    cellId = "product_count"
                    data = "${list.product.productCount}"
                }
                tableCell {
                    cellId = "product_sell_count"
                    data = "${list.product.productSellCount}"
                }
                tableCell {
                    cellId = "product_retail_Price"
                    data = "${list.product.productRetailPrice}"
                }
                tableCell {
                    cellId = "category_name"
                    data = list.categoryName
                }
                tableCell {
                    cellId = "product_status"
                    data = list.product.productAvailable
                }
                tableCell {
                    cellId = "created_user"
                    data = list.createdUser ?: ""
                }
                tableCell {
                    cellId = "updated_user"
                    data = list.updatedUser ?: ""
                }
                tableCell {
                    cellId = "show_alert"
                    data = if (list.product.productRemainAmountShow.toBoolean()) "Show" else "Off"
                }
                tableCell {
                    cellId = "created_date"
                    data = list.product.createdDate
                }
                tableCell {
                    cellId = "updated_date"
                    data = list.product.updatedDate
                }
                tableCell {
                    cellId = "${list.product.id}"
                    data = list.sellCount.size
                }
            }.run {
                return this
            }
        }

        fun getTableCellList(list: List<ProductTable>): List<List<TableCellVO>> {
            list.map {
                getTableCellList(it)
            }.run {
                return this
            }
        }
    }
}

data class ProductTable(
    @Embedded
    val product: ProductEntity,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "category_id",
        entity = CategoryEntity::class,
        projection = ["category_name"]
    )
    val categoryName: String,
    @Relation(
        parentColumn = "created_user_id",
        entityColumn = "user_id",
        entity = UserEntity::class,
        projection = ["user_display_name"]
    )
    var createdUser: String?,
    @Relation(
        parentColumn = "updated_user_id",
        entityColumn = "user_id",
        entity = UserEntity::class,
        projection = ["user_display_name"]
    )
    var updatedUser: String?,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "product_id",
        entity = SellEntity::class,
        projection = ["sell_id"]
    )
    val sellCount: List<Int>
)

fun product(block: ProductBuilder.() -> Unit) = ProductBuilder().apply(block).build()