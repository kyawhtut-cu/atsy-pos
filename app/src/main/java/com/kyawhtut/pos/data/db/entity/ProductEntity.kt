package com.kyawhtut.pos.data.db.entity

import androidx.recyclerview.widget.DiffUtil
import androidx.room.*
import com.kyawhtut.pos.utils.ProductType
import org.joda.time.DateTime
import java.util.*

@Entity(tableName = "product_table")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    val id: Int,
    @ColumnInfo(name = "product_name")
    val productName: String,
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
    var productName: String = ""
    var productPrice: Long = 0
    var productColor: Int = 0
    var productTextColor: Int = 0
    var productUnit: String = ""
    var productCount: Int = 0
    var productRetailPrice: Long = 0
    var categoryId: Int = 0
    var productAvailable = 1
    var createdUserId = 0
    var updatedUserId = 0
    var type: ProductType = ProductType.Product
    var createdDate: String = DateTime.now().toString("dd-MM-yyyy", Locale.ENGLISH)
    var updatedDate: String = DateTime.now().toString("dd-MM-yyyy", Locale.ENGLISH)

    fun build() = ProductEntity(
        id,
        productName,
        productPrice,
        productColor,
        productTextColor,
        productUnit,
        productCount,
        productRetailPrice,
        categoryId,
        productAvailable,
        createdUserId,
        updatedUserId,
        createdDate,
        updatedDate
    ).apply {
        type = this@ProductBuilder.type
    }
}

fun product(block: ProductBuilder.() -> Unit) = ProductBuilder().apply(block).build()