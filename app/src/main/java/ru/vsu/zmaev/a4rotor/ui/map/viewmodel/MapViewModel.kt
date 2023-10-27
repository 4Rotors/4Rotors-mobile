package ru.vsu.zmaev.a4rotor.ui.map.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vsu.zmaev.a4rotor.model.PointData
import ru.vsu.zmaev.a4rotor.model.PointRequestBody
import ru.vsu.zmaev.a4rotor.repository.MainRepositoryImpl

class MapViewModel constructor(
    private val repositoryImpl: MainRepositoryImpl
) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val point = MutableLiveData<PointData>()
    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getPoint(requestBody: PointRequestBody) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val res = repositoryImpl.getPoint(requestBody)
            withContext(Dispatchers.Main) {
                if (res.isSuccessful) {
                    point.postValue(res.body())
                } else {
                    onError(res.message())
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
    }
}