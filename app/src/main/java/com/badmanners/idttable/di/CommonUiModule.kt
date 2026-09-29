package com.badmanners.idttable.di

import android.content.Context
import com.badmanners.common_ui.ui.StringProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CommonUiModule {

    @Provides
    fun provideStringProvider(@ApplicationContext context: Context): StringProvider =
        StringProvider { resId -> context.getString(resId) }
}