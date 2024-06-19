package com.example.weatherapp.models

import java.util.Date

class Weather (var date: Date, var day: String, var icon: String,
               var description: String, var degree: Double, var min: Double,
               var max: Double, var night: Double, var humidity: Double  ) {
}