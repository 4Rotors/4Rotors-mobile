package ru.vsu.zmaev.a4rotor.ui.map.interactor

import androidx.lifecycle.LiveData
import retrofit2.Response
import ru.vsu.zmaev.a4rotor.data.model.point.PointData
import ru.vsu.zmaev.a4rotor.data.network.PointRequestBody

interface MapInteractor {
    suspend fun getPoint(pointRequestBody: PointRequestBody): LiveData<Response<PointData>>
}