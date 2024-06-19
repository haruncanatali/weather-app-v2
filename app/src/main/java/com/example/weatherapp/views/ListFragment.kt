package com.example.weatherapp.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.room.Room
import com.example.weatherapp.R
import com.example.weatherapp.adapters.CityListAdaptor
import com.example.weatherapp.databinding.FragmentListBinding
import com.example.weatherapp.models.City
import com.example.weatherapp.models.CityRoomDbModel
import com.example.weatherapp.roomdb.CityDao
import com.example.weatherapp.roomdb.CityDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ListFragment : Fragment() {
    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!

    val cities = listOf(
        "adana", "adiyaman", "afyonkarahisar", "ağrı", "amasya", "ankara", "antalya", "artvin",
        "aydın", "balıkesir", "bilecik", "bingöl", "bitlis", "bolu", "burdur", "bursa", "çanakkale",
        "çankırı", "çorum", "denizli", "diyarbakır", "edirne", "elazığ", "erzincan", "erzurum",
        "eskişehir", "gaziantep", "giresun", "gümüşhane", "hakkari", "hatay", "ısparta", "mersin",
        "istanbul", "izmir", "kars", "kastamonu", "kayseri", "kırklareli", "kırşehir", "kocaeli",
        "konya", "kütahya", "malatya", "manisa", "kahramanmaraş", "mardin", "muğla", "muş", "nevşehir",
        "niğde", "ordu", "rize", "sakarya", "samsun", "siirt", "sinop", "sivas", "tekirdağ",
        "tokat", "trabzon", "tunceli", "şanlıurfa", "uşak", "van", "yozgat", "zonguldak", "aksaray",
        "bayburt", "karaman", "kırıkkale", "batman", "şırnak", "bartın", "ardahan", "ığdır", "yalova",
        "karabük", "kilis", "osmaniye", "düzce"
    )
    var cityList : ArrayList<City> = arrayListOf()
    var isFavList = false

    private lateinit var db : CityDatabase
    private lateinit var cityDao : CityDao
    private val mDisposable =  CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        for (city in cities) {
            cityList.add(City(city, false))
        }

        db = Room
            .databaseBuilder(requireContext(),CityDatabase::class.java,"Cities")
            .build()
        cityDao = db.cityDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            isFavList = ListFragmentArgs.fromBundle(it).favlist
        }

        binding.listRcycMain.layoutManager = LinearLayoutManager(requireContext())

        if(!isFavList){
            binding.listRcycMain.adapter = CityListAdaptor(cityList)
        }
        else{
            mDisposable.add(
                cityDao.getList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleFavList)
            )
        }
    }

    private fun handleFavList(cities: List<CityRoomDbModel>){
        var favCities : ArrayList<City> = arrayListOf()
        for(city in cities){
            favCities.add(City(city.CityName,true))
        }
        binding.listRcycMain.adapter = CityListAdaptor(favCities)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mDisposable.clear()
    }

}