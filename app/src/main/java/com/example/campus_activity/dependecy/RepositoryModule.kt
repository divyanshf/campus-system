package com.example.campus_activity.dependecy

import com.example.campus_activity.data.repository.ChatsRepository
import com.example.campus_activity.data.repository.FeedsRepository
import com.example.campus_activity.data.repository.RoomsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
    fun provideFeedsRepository(firebaseFirestore: FirebaseFirestore):FeedsRepository{
        return FeedsRepository(firebaseFirestore)
    }

    @Singleton
    @Provides
    fun provideRoomsRepository(firebaseFirestore: FirebaseFirestore, firebaseStorage: FirebaseStorage):RoomsRepository{
        return RoomsRepository(firebaseFirestore, firebaseStorage)
    }

    @Singleton
    @Provides
    fun provideChatsRepository(firebaseFirestore: FirebaseFirestore, firebaseAuth:FirebaseAuth):ChatsRepository{
        return ChatsRepository(firebaseFirestore, firebaseAuth)
    }

}