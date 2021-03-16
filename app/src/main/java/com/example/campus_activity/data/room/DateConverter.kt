package com.example.campus_activity.data.room

import androidx.room.TypeConverter
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class DateConverter {
    @TypeConverter
    fun fromTimestamp(t:Timestamp):String{
        val type = object : TypeToken<Timestamp>() {}.type
        return Gson().toJson(t, type)
    }

    @TypeConverter
    fun toTimestamp(json:String):Timestamp{
        val type = object : TypeToken<Timestamp>() {}.type
        return Gson().fromJson(json, type)
    }
}