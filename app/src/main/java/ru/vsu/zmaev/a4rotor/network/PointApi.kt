package ru.vsu.zmaev.a4rotor.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.vsu.zmaev.a4rotor.model.PointData
import ru.vsu.zmaev.a4rotor.model.PointRequestBody

interface PointApi {

    @Headers("Content-type: application/json")
    @POST("/order_in_delivery/location")
    suspend fun getPoint(@Body pointRequestBody: PointRequestBody) : Response<PointData>

    companion object {
        fun getPointApi() : PointApi? {
            return MainApi.client?.create(PointApi::class.java)
        }
    }
}