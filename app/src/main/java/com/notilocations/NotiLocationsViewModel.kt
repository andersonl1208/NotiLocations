package com.notilocations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.notilocations.database.NotiLocationsRepository

class NotiLocationsViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = NotiLocationsRepository.getInstance(app)

}