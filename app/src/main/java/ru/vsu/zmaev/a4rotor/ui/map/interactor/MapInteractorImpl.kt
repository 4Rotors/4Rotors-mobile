package ru.vsu.zmaev.a4rotor.ui.map.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Response
import ru.vsu.zmaev.a4rotor.data.model.point.PointData
import ru.vsu.zmaev.a4rotor.data.network.PointRequestBody
import ru.vsu.zmaev.a4rotor.data.repository.MainRepository

class MapInteractorImpl(
    private val repository: MainRepository
) : MapInteractor {

    override suspend fun getPoint(pointRequestBody: PointRequestBody): LiveData<Response<PointData>> {
        return MutableLiveData(repository.getPoint(pointRequestBody))
    }

}