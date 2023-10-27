package ru.vsu.zmaev.a4rotor.repository

import retrofit2.Response
import ru.vsu.zmaev.a4rotor.model.PointData
import ru.vsu.zmaev.a4rotor.model.PointRequestBody

interface MainRepository {
    suspend fun getPoint(pointRequestBody: PointRequestBody) : Response<PointData>
}