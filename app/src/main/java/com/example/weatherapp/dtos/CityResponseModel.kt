package com.example.weatherapp.dtos

data class CityResponseModel(
    val city: String,
    val result: List<Result>,
    val success: Boolean
)