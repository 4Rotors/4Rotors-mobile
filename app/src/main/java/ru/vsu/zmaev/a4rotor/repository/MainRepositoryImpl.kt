package ru.vsu.zmaev.a4rotor.repository

import retrofit2.Response
import ru.vsu.zmaev.a4rotor.model.PointData
import ru.vsu.zmaev.a4rotor.network.PointApi
import ru.vsu.zmaev.a4rotor.model.PointRequestBody

class MainRepositoryImpl(
    private val pointApi: PointApi
): MainRepository {
    override suspend fun getPoint(pointRequestBody: PointRequestBody): Response<PointData> {
        return pointApi.getPoint(pointRequestBody)
    }
}