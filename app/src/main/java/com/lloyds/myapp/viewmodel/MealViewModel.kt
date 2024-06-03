package com.lloyds.myapp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.lloyds.myapp.datamodel.MealModel
import com.lloyds.myapp.datamodel.Meals
import com.lloyds.myapp.network.APIService
import com.lloyds.myapp.network.NetworkModule
import com.lloyds.myapp.utils.ConnectivityRepository
import kotlinx.coroutines.*
import java.lang.Exception

class MealViewModel(private val connectivityRepository: ConnectivityRepository) : ViewModel() {

    val responseContainer = MutableLiveData<MealModel>()
    val errorMessage = MutableLiveData<String>()
    val isShowProgress = MutableLiveData<Boolean>()
    val status = MutableLiveData<String>()
    val isOnline = connectivityRepository.isConnected.asLiveData()

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled : ${throwable.localizedMessage}")
    }

    fun getMealsCategory() {
        isShowProgress.value = true
        job = viewModelScope.launch {
            delay(500)

            try {
                val response = NetworkModule.apiInterface.mealCategory("list")
                withContext(Dispatchers.Main) {
                    Log.d("TAG->", response.raw().request.url.toString())
                    if (response.isSuccessful) {
                        Log.i(
                            "TAG",
                            "CategoryModel onResponse: code:" + response.code() + ", response:" + response.body()
                        )
                        Log.i("onResponse=", response.body().toString())
                        responseContainer.postValue(response.body())
                        isShowProgress.value = false
                    } else {
                        onError("Error : ${response.message()}")
                    }
                }
            } catch (ex: Exception) {
                onError("Exception handled: ${ex.localizedMessage}")
                status.value = ex.toString()
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        isShowProgress.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}