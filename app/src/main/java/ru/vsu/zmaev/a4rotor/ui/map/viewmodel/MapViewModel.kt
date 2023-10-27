package ru.vsu.zmaev.a4rotor.ui.map.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.zmaev.a4rotor.data.model.point.PointData
import ru.vsu.zmaev.a4rotor.data.network.PointRequestBody
import ru.vsu.zmaev.a4rotor.ui.map.interactor.MapInteractor

class MapViewModel(
    private val interator: MapInteractor
) : ViewModel() {

    val point = MutableLiveData<PointData>()

    fun getPoint(requestBody: PointRequestBody) {
        viewModelScope.launch {
            point.postValue(interator.getPoint(requestBody).value!!.body())
        }
    }
}