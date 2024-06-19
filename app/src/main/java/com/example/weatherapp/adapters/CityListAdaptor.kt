package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.RecyclerCityListRowBinding
import com.example.weatherapp.models.City
import com.example.weatherapp.views.ListFragmentDirections

class CityListAdaptor (val cityList : List<City>) : RecyclerView.Adapter<CityListAdaptor.CityListHolder>() {
    class CityListHolder (val binding: RecyclerCityListRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListHolder {
        val rcycRowBinding = RecyclerCityListRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CityListHolder(rcycRowBinding)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    override fun onBindViewHolder(holder: CityListHolder, position: Int) {
        holder.binding.rcyClCityName.setText(cityList.get(position).cityName.replaceFirstChar { it.uppercase() })
        holder.binding.rcyClCityName.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToDetailFragment2(cityList.get(position).cityName)
            Navigation.findNavController(it).navigate(action)
        }
    }
}