package com.notilocations

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.notilocations.database.Location
import com.notilocations.database.NotiLocationsRepository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NotiLocationsRepository.getInstance((application)).insertLocation(
                Location(45, "test once", 0.0, 0.0))

        val temp = NotiLocationsRepository.getInstance(application).getLocation(45)
        val test2 = NotiLocationsRepository.getInstance(application).getAllLocations()
        temp.observe(this, Observer<Location> { test3 ->
            if (test3 != null) {
                Log.i("MyLogTest", test3.name + test3.lat + test3.lng)
//                NotiLocationsRepository.getInstance(application).deleteLocation(test3)
            }
        })

        NotiLocationsRepository.getInstance((application)).updateLocation(Location(45, "test something different", 1.1, 1.1))


        test2.observe(this, Observer<List<Location>> { what ->
            if (what != null) {
                for (l in what) {
                    Log.i("MyLogTest", l.name + l.lat + l.lng)
                }
            }
        })
    }
}