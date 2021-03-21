package com.example.campus_activity.data.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.campus_activity.data.model.ChatModel
import com.example.campus_activity.data.model.Result
import com.example.campus_activity.data.repository.ChatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel
@Inject
constructor(
    val repository: ChatsRepository
) : ViewModel() {

    val allChats: LiveData<Result<List<ChatModel>>> = repository.allChats.asLiveData()

    fun initialize(roomId:String){
        repository.initialize(roomId)
    }

    fun insertChatOnClick(message:String){
        repository.insertChatToDB(message)
    }

    fun deleteChat(chatModel: ChatModel){
        repository.deleteChat(chatModel)
    }

    fun addMember(roomId:String, roomName:String, email:String): LiveData<Result<Boolean>> {
        return repository.addMember(roomId, roomName, email).asLiveData()
    }
}