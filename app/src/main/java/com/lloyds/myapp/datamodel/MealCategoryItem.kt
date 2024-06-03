package com.lloyds.myapp.datamodel

import com.google.gson.annotations.SerializedName

data class MealCategoryItem(
    @SerializedName("meals")
    var meals: MutableList<MealItem> = arrayListOf()
) {
}

data class MealItem(
    @SerializedName("strMeal")
    var strMeal: String? = null,
    @SerializedName("strMealThumb")
    var strMealThumb: String? = null,
    @SerializedName("idMeal")
    var idMeal: String? = null
)  {

}
