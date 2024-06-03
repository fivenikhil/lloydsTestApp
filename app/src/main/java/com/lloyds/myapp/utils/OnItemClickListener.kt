package com.lloyds.myapp.utils

import com.lloyds.myapp.datamodel.MealItem
import com.lloyds.myapp.datamodel.Meals

interface OnItemClickListener {

    fun onItemClick(meals: Meals, position: Int)

    fun onItemClickItem(meals: MealItem, position: Int)
}