package com.kyawhtut.pos.utils

sealed class ProductType {
    object Category : ProductType()
    object Product : ProductType()
}