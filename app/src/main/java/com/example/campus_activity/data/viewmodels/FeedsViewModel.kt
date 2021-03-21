package com.example.campus_activity.data.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.campus_activity.data.model.FeedModel
import com.example.campus_activity.data.model.Result
import com.example.campus_activity.data.repository.FeedsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedsViewModel
@Inject
constructor(
    private val repository: FeedsRepository
) : ViewModel() {
    val allFeeds:LiveData<Result<List<FeedModel>>> = repository.allFeeds.asLiveData()

    fun insertFeed(feedModel: FeedModel){
        repository.insertFeed(feedModel)
    }

    fun uploadImage(path:String, imageUri:Uri) : LiveData<Result<Uri>>{
        return repository.uploadImage(path, imageUri).asLiveData()
    }

    fun getAllFeeds(){
        viewModelScope.launch {
            repository.getAllFeeds()
        }
    }
}