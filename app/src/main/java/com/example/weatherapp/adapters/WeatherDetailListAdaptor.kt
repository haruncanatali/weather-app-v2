package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.RecyclerCityDetailWeatherListBinding
import com.example.weatherapp.dtos.Result

class WeatherDetailListAdaptor (val weatherList: List<Result>, val cityName: String, val isFavorite: Boolean) : RecyclerView.Adapter<WeatherDetailListAdaptor.WeatherDetailHolder>() {
    class WeatherDetailHolder (val binding : RecyclerCityDetailWeatherListBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDetailHolder {
        val rcycBinding = RecyclerCityDetailWeatherListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WeatherDetailHolder(rcycBinding)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    override fun onBindViewHolder(holder: WeatherDetailHolder, position: Int) {
        holder.binding.detailDayTxt.setText(weatherList.get(position).day)
        holder.binding.detailDateTxt.setText(weatherList.get(position).date)
        Glide.with(holder.itemView.context)
            .load(weatherList.get(position).icon)
            .into(holder.binding.detailIconImg)

        holder.binding.detailDegreeTxt.setText(weatherList.get(position).degree)
        holder.binding.detailHumidityTxt.setText(weatherList.get(position).humidity)
        holder.binding.detailDescriptionTxt.setText(weatherList.get(position).description)

        holder.binding.detailMaxDegreeTxt.setText(weatherList.get(position).max)
        holder.binding.detailMinDegreeTxt.setText(weatherList.get(position).min)
        holder.binding.detailNightDegreeTxt.setText(weatherList.get(position).night)
    }
}