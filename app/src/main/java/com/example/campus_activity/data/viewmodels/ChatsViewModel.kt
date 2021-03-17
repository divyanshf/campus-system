package com.example.campus_activity.data.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.campus_activity.data.model.ChatModel
import com.example.campus_activity.data.model.Result
import com.example.campus_activity.data.repository.ChatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@ActivityScoped
class ChatsViewModel
constructor(
    roomName: String
) : ViewModel() {

    private val repository = ChatsRepository(roomName)

    val allChats: LiveData<Result<List<ChatModel>>> = repository.allChats.asLiveData()

    init {
        viewModelScope.launch {
            repository.allChats.collect {
                if (it is Result.Success){
                    Log.i("viewmodel", it.result.toString())
                }
            }
        }
    }

    fun insertChatOnClick(message:String){
        repository.insertChatToDB(message)
    }
}