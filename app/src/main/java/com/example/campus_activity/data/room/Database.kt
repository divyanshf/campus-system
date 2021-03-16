package com.example.campus_activity.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.campus_activity.data.model.ChatModel

@Database(entities = [ChatModel::class], exportSchema = false, version = 1)
@TypeConverters(DateConverter::class)
abstract class Database:RoomDatabase() {
    abstract fun chatsDao():ChatsDao

    companion object{
        const val DATABASE_NAME = "database"
    }
}