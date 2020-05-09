package com.sanislo.nennospizza.domain.usecase

import android.content.Context
import com.sanislo.nennospizza.R

class GetTransitionNameUseCase(val context: Context) {
    fun invoke(adapterPosition: Int) = context.getString(R.string.pizza_details_transition, adapterPosition)
}