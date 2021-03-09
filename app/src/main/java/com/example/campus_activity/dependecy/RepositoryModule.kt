package com.example.campus_activity.dependecy

import com.example.campus_activity.data.repository.FeedsRepository
import com.google.firebase.firestore.FirebaseFirestore
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
        return FeedsRepository((firebaseFirestore))
    }

}