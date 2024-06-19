package com.example.weatherapp.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.models.CityRoomDbModel

@Database(entities = [CityRoomDbModel::class], version = 1)
 abstract class CityDatabase : RoomDatabase() {
     abstract fun cityDao() : CityDao
}