package com.example.campus_activity.dependecy

import android.content.Context
import androidx.room.Room
import com.example.campus_activity.data.room.ChatsDao
import com.example.campus_activity.data.room.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context):Database{
        return Room.databaseBuilder(
            context,
            Database::class.java,
            Database.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesChatsDao(db : Database):ChatsDao{
        return db.chatsDao()
    }

}