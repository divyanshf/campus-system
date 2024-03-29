package com.example.campus_activity.data.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.campus_activity.data.model.Result
import com.example.campus_activity.data.model.RoomModel
import com.example.campus_activity.data.repository.RoomsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomsViewModel
@Inject
    constructor(
        private val repository: RoomsRepository
    ):ViewModel() {
    val allRooms:LiveData<Result<List<RoomModel>>> = repository.allRooms.asLiveData()

    fun uploadImage(path:String, uri: Uri):LiveData<Result<Uri>>{
        return repository.uploadImage(path, uri).asLiveData()
    }

    fun uploadClub(name:String, rollNmber:String, members:ArrayList<String>, uri:Uri?){
        return repository.uploadClub(name, rollNmber, members, uri)
    }

    fun getAllRooms(){
        viewModelScope.launch {
            repository.getAllRooms()
        }
    }
}