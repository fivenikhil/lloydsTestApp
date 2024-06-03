package com.lloyds.myapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lloyds.myapp.datamodel.MCategoryItemView
import com.lloyds.myapp.network.APIService
import com.lloyds.myapp.utils.ConnectivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class CategoryRecipeViewModel @Inject constructor(private val connectivityRepository: ConnectivityRepository, private val retrofitInstance: APIService) :
    ViewModel() {

    val responseContainer = MutableLiveData<MCategoryItemView>()
    val errorMessage = MutableLiveData<String>()
    val isShowProgress = MutableLiveData<Boolean>()
    val status = MutableLiveData<String>()
    val isOnline = connectivityRepository.isConnected.asLiveData()

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled : ${throwable.localizedMessage}")
    }

    fun getCategoryItemView(mealItemId: String) {
        isShowProgress.value = true
        job = viewModelScope.launch {
            delay(500)
            try {
                val response = retrofitInstance.categoryItemView(mealItemId)
                withContext(Dispatchers.Main) {
                    Log.d("TAG->", response.raw().request.url.toString())
                    if (response.isSuccessful) {
                        Log.i(
                            "TAG",
                            "RecipeItemModel onResponse: code:" + response.code() + ", response:" + response.body()
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