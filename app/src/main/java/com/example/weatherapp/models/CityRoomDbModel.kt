package com.example.weatherapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityRoomDbModel (
    @ColumnInfo(name = "CityName")
    var CityName : String
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0 // default
}