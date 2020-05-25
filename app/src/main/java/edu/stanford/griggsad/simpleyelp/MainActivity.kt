package edu.stanford.griggsad.simpleyelp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "MainActivity"
private const val BASE_URL = "https://api.yelp.com/v3/"
private const val API_KEY = "mukLm2JCFHvcrBfSvMZTFG6dzJ--CUJY9OAdVqgyoJmxjjo7zx8zA1Zo43BG8roxBHWacBC5avssE-w6GMndp0Iw-EtsEK15WrZ2OQQuXmFe9AYA9Ut0PLF9ALDJXnYx"

//private val INITIAL_SEARCH = "Avocado Toast"
private var searchTerm = "Avocado Toast"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //searchTerm = INITIAL_SEARCH
        searchRestaurants()

        btGo.setOnClickListener{
            searchTerm = etSearch.text.toString()
            Log.i(TAG,"Button clicked, search term: $searchTerm")
            searchRestaurants()
        }

    }

    private fun searchRestaurants() {
        val restaurants = mutableListOf<YelpRestaurant>()
        val adapter = RestaurantsAdapter(this, restaurants)
        rvRestaurants.adapter = adapter
        rvRestaurants.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val yelpService = retrofit.create(YelpService::class.java)
        yelpService.searchRestaurants("Bearer $API_KEY", searchTerm, "Palo Alto").enqueue(object : Callback<YelpSearchResult> {
            override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
                Log.i(TAG, "onResponse $response")
                val body = response.body()
                if (body == null) {
                    Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                    return
                }
                restaurants.addAll(body.restaurants)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                Log.i(TAG, "onFailure $t")
            }
        })
    }
}
