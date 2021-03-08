package com.example.campus_activity.dependecy

import com.example.campus_activity.data.repository.ChatsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideChatsRepository(firebaseAuth: FirebaseAuth, firebaseDatabase: FirebaseDatabase) : ChatsRepository {
        return ChatsRepository(firebaseDatabase, firebaseAuth)
    }

}