package com.example.myshop.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Products (
    val user_id: String = "",
    val user_name: String = "",
    val productTitle: String ="",
    val productPrice: String= "",
    val productDescription: String ="",
    val productQuantity: String= "",
    val productImage: String= "",
    var product_id: String = ""

    ) : Parcelable


