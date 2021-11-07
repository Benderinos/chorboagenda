/*
 * Copyright 2021 dalodev
 */
package es.littledavity.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.littledavity.database.ChorboagendaDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object TablesModule {

    @Provides
    @Singleton
    fun provideContactsDao(contactsDatabase: ChorboagendaDatabase) = contactsDatabase.contactDao

    @Provides
    @Singleton
    fun provideLikedContactsDao(contactsDatabase: ChorboagendaDatabase) = contactsDatabase.likedContactsDao
}
