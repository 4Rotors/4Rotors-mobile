package ru.vsu.zmaev.a4rotor.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.vsu.zmaev.a4rotor.ui.map.interactor.MapInteractorImpl
import ru.vsu.zmaev.a4rotor.ui.map.viewmodel.MapViewModel

class CustomViewModelFactory(
    private val repository: MapInteractorImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel Not Found")
    }
}