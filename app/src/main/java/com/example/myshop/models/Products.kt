package com.example.myshop.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Products (
    val productId: String ="",
    val productTitle: String ="",
    val productPrice: String= "",
    val productDescription: String ="",
    val productQuantity: String= "",
    val productImage: String= ""

    ) : Parcelable


