package com.sanislo.nennospizza.presentation.details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PizzaDetailsInput(
        val pizzaName: String,
        val imgUrl: String?,
        val adapterPosition: Int
) : Parcelable