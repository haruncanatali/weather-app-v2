package com.example.weatherapp.roomdb

import androidx.room.Dao
import androidx.room.Query
import com.example.weatherapp.models.CityRoomDbModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface CityDao {

    @Query("SELECT * FROM CityRoomDbModel WHERE CityName = :cityName")
    fun getByName(cityName: String) : Flowable<List<CityRoomDbModel>>

    @Query("SELECT * FROM CityRoomDbModel")
    fun getList() : Flowable<List<CityRoomDbModel>>

    @Query("INSERT INTO CityRoomDbModel(CityName) VALUES(:cityName)")
    fun insert(cityName: String) : Completable

    @Query("DELETE FROM CityRoomDbModel WHERE CityName = :cityName")
    fun delete(cityName: String) : Completable

}