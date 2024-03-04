package com.example.cityweather.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import com.example.cityweather.R
import com.example.cityweather.api.models.WeatherData
import com.example.cityweather.databinding.ActivityMainBinding
import com.tunetap.api.client.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


//94bd52a7942367aea2c53676b69f6f5b apiKey
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchWeatherData("jaipur")
//hgjhgjhgjh
        searchCity()

    }

    private fun searchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true//hgfhg
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               return true
            }

        })
    }

    private fun fetchWeatherData(cityName: String) {

        val call = ApiClient.getClient.getWeatherData(
            cityName,
            "94bd52a7942367aea2c53676b69f6f5b",
            "metric"
        )
        call.enqueue(object : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                val responseBody = response.body()
                if (response.isSuccessful) {

                    val sunRise = responseBody?.sys?.sunrise?.toLong()
                    val sunSet = responseBody?.sys?.sunset?.toLong()

                    "${responseBody?.main?.temp.toString()}°C".also { binding.tvTemperature.text = it }
                    "${responseBody?.main?.tempMin.toString()}°C".also { binding.tvMinTemp.text = it }
                    "${responseBody?.main?.tempMax.toString()}°C".also { binding.tvMaxTemp.text = it }

                    "${responseBody?.main?.humidity.toString()}%".also { binding.tvHumidityLevel.text = it }
                    ((responseBody?.wind?.speed.toString()) +"m/s").also { binding.tvWindLevel.text = it }
                    binding.tvSunriseLevel.text = "${sunRise?.let { time(it) }}"
                    binding.tvSeaLevel.text = responseBody?.main?.pressure.toString()
                    binding.tvSunsetLevel.text = "${sunSet?.let { time(it) }}"
                    binding.weather.text = responseBody?.weather?.firstOrNull()?.main?: "unknown"
                    binding.tvConditionType.text = responseBody?.weather?.firstOrNull()?.main?: "unknown"
                    binding.tvDayName.text = dayName(System.currentTimeMillis())
                    binding.tvDate.text = date()
                    binding.tvCity.text = cityName
                    val condition = responseBody?.weather?.firstOrNull()?.main?: "unknown"
                    weatherImgChange(condition)

                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {

            }

        })
    }

    private fun weatherImgChange(conditions:String) {
        when(conditions){
            "Clear Sky", "Sunny", "Clear"->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimation.setAnimation(R.raw.sun)

            }

            "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy"->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimation.setAnimation(R.raw.cloud)

            }

            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain"->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimation.setAnimation(R.raw.rain)

            }

            "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard"->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimation.setAnimation(R.raw.snow)

            }
            else->{
                    binding.root.setBackgroundResource(R.drawable.sunny_background)
                    binding.lottieAnimation.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimation.playAnimation()
    }


    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }

    private fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:MM", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }

    fun dayName(timestamp: Long): String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }

}