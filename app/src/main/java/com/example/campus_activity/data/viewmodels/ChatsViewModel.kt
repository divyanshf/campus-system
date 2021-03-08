package com.example.campus_activity.data.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.campus_activity.data.model.ChatModel
import com.example.campus_activity.data.model.Result
import com.example.campus_activity.data.repository.ChatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel
@Inject
constructor(
    val repository: ChatsRepository
    ) : ViewModel() {

    var allChats: LiveData<Result<List<ChatModel>>> = repository.allChats.asLiveData()

    fun insertChatOnClick(message:String){
        repository.insertChatToDB(message)
    }
}