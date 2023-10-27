package ru.vsu.zmaev.a4rotor.data.repository

import retrofit2.Response
import ru.vsu.zmaev.a4rotor.data.model.point.PointData
import ru.vsu.zmaev.a4rotor.data.network.PointRequestBody

interface MainRepository {
    suspend fun getPoint(pointRequestBody: PointRequestBody) : Response<PointData>
}