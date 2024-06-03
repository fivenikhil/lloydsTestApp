package com.lloyds.myapp.utils

import android.content.Context
import com.lloyds.myapp.constants.AppConstant.Companion.MEAL_CATEGORY_ID
import com.lloyds.myapp.constants.AppConstant.Companion.MEAL_NAME

class SessionManager(context: Context) {

    private val prefName = "AppPref"
    private val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    private val editor = pref.edit()

    fun addMealName(name: String?) {
        editor.putString(MEAL_NAME, name)
        editor.apply()
    }

    fun getMealName(): String? {
        return pref.getString(MEAL_NAME, "")
    }

    fun addMealItemId(id: String?) {
        editor.putString(MEAL_CATEGORY_ID, id)
        editor.apply()
    }

    fun getMealItemId(): String? {
        return pref.getString(MEAL_CATEGORY_ID, "")
    }
}