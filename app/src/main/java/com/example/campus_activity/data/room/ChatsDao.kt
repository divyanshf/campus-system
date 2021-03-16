package com.example.campus_activity.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.campus_activity.data.model.ChatModel

@Dao
interface ChatsDao {
    @Insert
    suspend fun insert(chatModel: ChatModel)

    @Update
    suspend fun update(chatModel: ChatModel)

    @Delete
    suspend fun delete(chatModel: ChatModel)
}