package com.lloyds.myapp.datamodel

import com.google.gson.annotations.SerializedName

data class MealModel(
    @SerializedName("meals")
    var meals: ArrayList<Meals> = arrayListOf()
)

data class Meals(
    @SerializedName("strCategory")
    var strCategory: String? = null
) {

}
