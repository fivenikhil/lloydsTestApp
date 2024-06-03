package com.lloyds.myapp.network

import com.lloyds.myapp.constants.AppConstant.Companion.MEAL_CATEGORY_API
import com.lloyds.myapp.constants.AppConstant.Companion.MEAL_CATEGORY_ITEM_API
import com.lloyds.myapp.constants.AppConstant.Companion.MEAL_CATEGORY_ITEM_VIEW_API
import com.lloyds.myapp.datamodel.MCategoryItemView
import com.lloyds.myapp.datamodel.MealCategoryItem
import com.lloyds.myapp.datamodel.MealModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET(MEAL_CATEGORY_API + "list.php")
    suspend fun mealCategory(
        @Query("c") list: String,
    ): Response<MealModel>

    @GET(MEAL_CATEGORY_ITEM_API)
    suspend fun mealCategoryItem(
        @Query("c") list: String
    ): Response<MealCategoryItem>

    @GET(MEAL_CATEGORY_ITEM_VIEW_API)
    suspend fun categoryItemView(
        @Query("i") list: String
    ): Response<MCategoryItemView>
}