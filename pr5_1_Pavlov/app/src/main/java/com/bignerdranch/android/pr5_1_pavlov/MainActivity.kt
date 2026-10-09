package com.bignerdranch.android.pr5_1_pavlov

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {


    private lateinit var queue: RequestQueue

    private lateinit var cityInput: EditText
    private lateinit var resultText: TextView


    private val key = "af3b8c9e575ca575f0c3337dc07b9c0c"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        queue = Volley.newRequestQueue(this)
        cityInput = findViewById(R.id.cityInput)
        resultText = findViewById(R.id.resultText)
        val button = findViewById<Button>(R.id.getWeatherButton)


        button.setOnClickListener {
            val city = cityInput.text.toString().trim()
            if (city.isEmpty()) {
                Toast.makeText(this, "Введите название города", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            getResult(city)
        }
    }


    private fun getResult(city: String) {

        if (city.isEmpty()) {
            Toast.makeText(this, "Город не может быть пустым", Toast.LENGTH_SHORT).show()
            return
        }


        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city" +
                "&appid=$key&units=metric&lang=ru"

        Log.d("MyLog", "URL: $url")


        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->

                Log.d("MyLog", "Response: $response")

                try {

                    val obj = JSONObject(response)

                    val cityName = obj.getString("name")
                    val temp = obj.getJSONObject("main").getDouble("temp")
                    val feelsLike = obj.getJSONObject("main").getDouble("feels_like")
                    val pressure = obj.getJSONObject("main").getInt("pressure")
                    val humidity = obj.getJSONObject("main").getInt("humidity")
                    val windSpeed = obj.getJSONObject("wind").getDouble("speed")
                    val description = obj.getJSONArray("weather")
                        .getJSONObject(0).getString("description")


                    Log.d("MyLog", "Город: $cityName")
                    Log.d("MyLog", "Температура: $temp°C")
                    Log.d("MyLog", "Ощущается как: $feelsLike°C")
                    Log.d("MyLog", "Описание: $description")
                    Log.d("MyLog", "Давление: $pressure гПа")
                    Log.d("MyLog", "Влажность: $humidity%")
                    Log.d("MyLog", "Ветер: $windSpeed м/с")


                    val text = """
                         Город: $cityName
                         Температура: $temp°C
                         Ощущается как: $feelsLike°C
                         Описание: $description
                         Давление: $pressure гПа
                         Влажность: $humidity%
                         Скорость ветра: $windSpeed м/с
                    """.trimIndent()

                    resultText.text = text

                } catch (e: Exception) {
                    Log.e("MyLog", "Ошибка парсинга JSON: ${e.message}")
                    Toast.makeText(this, "Ошибка парсинга данных", Toast.LENGTH_LONG).show()
                }
            },
            { error ->

                Log.e("MyLog", "Volley error: ${error.message}")
                Toast.makeText(this, "Ошибка: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )

        queue.add(stringRequest)
    }
}