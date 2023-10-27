package ru.vsu.zmaev.a4rotor.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import ru.vsu.zmaev.a4rotor.data.model.point.PointData

interface PointApi {

    @Headers("Content type: application/json")
    @GET("getLocation")
    suspend fun getPoint(@Body pointRequestBody: PointRequestBody) : Response<PointData>

    companion object {
        fun getPointApi() : PointApi? {
            return MainApi.client?.create(PointApi::class.java)
        }
    }
}