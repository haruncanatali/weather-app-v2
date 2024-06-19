package com.example.weatherapp.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.weatherapp.adapters.WeatherDetailListAdaptor
import com.example.weatherapp.databinding.FragmentDetailBinding
import com.example.weatherapp.dtos.CityResponseModel
import com.example.weatherapp.models.CityRoomDbModel
import com.example.weatherapp.roomdb.CityDao
import com.example.weatherapp.roomdb.CityDatabase
import com.example.weatherapp.utils.RetrofitInstance
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.Console
import java.io.IOException

class DetailFragment : Fragment() {

    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var db : CityDatabase
    private lateinit var cityDao : CityDao
    private val mDisposable =  CompositeDisposable()

    private var isFavoriteCity = false
    private var _cityName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room
            .databaseBuilder(requireContext(),CityDatabase::class.java,"Cities")
            .build()
        cityDao = db.cityDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val cityName = DetailFragmentArgs.fromBundle(it).city

            if(!cityName.isNullOrEmpty()){
                fillTheInformantions(cityName,view)
            }
            else{
                Toast.makeText(requireContext(),"Veritabanı hatası!",
                    Toast.LENGTH_LONG).show()
                val action = DetailFragmentDirections.actionDetailFragmentToListFragment()
                Navigation.findNavController(view).navigate(action)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mDisposable.clear()
    }

    fun fillTheInformantions(cityName:String, view:View){

        binding.detailMainPageBtn.setOnClickListener {
            val action = DetailFragmentDirections.actionDetailFragmentToListFragment()
            Navigation.findNavController(view).navigate(action)
        }

        binding.detailFavoritePageBtn.setOnClickListener {
            val action = DetailFragmentDirections.actionDetailFragmentToListFragment(favlist = true)
            Navigation.findNavController(view).navigate(action)
        }

        _cityName = cityName

        GlobalScope.launch (Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getWeather(cityName)
            } catch (e:HttpException){
                Log.e("API_CALL",e.localizedMessage)
                Toast.makeText(requireContext(),"http error ${e.localizedMessage}",Toast.LENGTH_LONG).show()
                return@launch
            } catch (e1: IOException){
                Log.e("API_CALL",e1.localizedMessage)
                Toast.makeText(requireContext(),"app error ${e1.localizedMessage}",Toast.LENGTH_LONG).show()
                return@launch
            } catch (e2: Exception){
                Log.e("API_CALL",e2.localizedMessage)
                Toast.makeText(requireContext(),"app error ${e2.localizedMessage}",Toast.LENGTH_LONG).show()
                return@launch
            }

            if(response.isSuccessful && response.body() != null){
                withContext(Dispatchers.Main){
                    binding.apply {
                        val api_response : CityResponseModel = response.body()!!
                        recycMainDetailList.layoutManager = LinearLayoutManager(requireContext())
                        recycMainDetailList.adapter = WeatherDetailListAdaptor(api_response.result, api_response.city, isFavoriteCity)
                        detailCityNameTxt.setText(api_response.city.replaceFirstChar { it.uppercase() })
                        detailProgressBar.visibility = View.GONE
                    }
                }
            }
        }

        try {
            mDisposable.add(
                cityDao.getByName(cityName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleFavStatusFromDb)
            )
        }catch (ex:Exception){
            Log.e("vt hatasi",ex.localizedMessage)
        }
    }

    private fun handleFavStatus(){
        return
    }

    private fun handleFavStatusFromDb(cityDbModel:List<CityRoomDbModel>){
        if(cityDbModel.isEmpty()){
            binding.detailFavoriteStatusBtn.setText("Favoriye al")
            binding.detailFavoriteStatusBtn.setOnClickListener {
                mDisposable.add(
                    cityDao.insert(_cityName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleFavStatus)
                )
            }
        }
        else{
            binding.detailFavoriteStatusBtn.setText("Favoriden çık")
            binding.detailFavoriteStatusBtn.setOnClickListener{
                mDisposable.add(
                    cityDao.delete(_cityName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleFavStatus)
                )
            }
        }
    }
}