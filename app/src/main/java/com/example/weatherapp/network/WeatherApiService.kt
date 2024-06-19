package com.example.weatherapp.network

import com.example.weatherapp.dtos.CityResponseModel
import com.example.weatherapp.utils.Utils
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherApiService {
    @GET("getWeather")
    suspend fun getWeather(
        @Query("data.city") city:String,
        @Query("data.lang") lang:String = Utils.LANGUAGE,
        @Header("authorization") auth:String = Utils.API_KEY
    ) : Response<CityResponseModel>
}