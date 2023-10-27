package ru.vsu.zmaev.a4rotor.data.repository

import retrofit2.Response
import ru.vsu.zmaev.a4rotor.data.model.point.PointData
import ru.vsu.zmaev.a4rotor.data.network.PointApi
import ru.vsu.zmaev.a4rotor.data.network.PointRequestBody

class MainRepositoryImpl(
    private val pointApi: PointApi
): MainRepository {
    override suspend fun getPoint(pointRequestBody: PointRequestBody): Response<PointData> {
        return pointApi.getPoint(pointRequestBody)
    }
}