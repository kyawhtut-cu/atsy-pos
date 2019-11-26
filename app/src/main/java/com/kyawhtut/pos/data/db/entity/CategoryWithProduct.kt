package com.kyawhtut.pos.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithProduct(
    @Embedded
    val categoryEntity: CategoryEntity,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "category_id"
    )
    val productList: List<ProductEntity>
) {

    fun convert2Product(): ProductEntity {
        categoryEntity.categoryItemCount = productList.size
        return categoryEntity.category2Product()
    }
}