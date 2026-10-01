package com.badmanners.idttable.feature.input.impl.di

import com.badmanners.idttable.feature.input.api.InputFeatureScreenProvider
import com.badmanners.idttable.feature.input.impl.provider.InputFeatureScreenProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class InputFeatureModule {

    @Binds
    abstract fun bindInputScreenProvider(impl: InputFeatureScreenProviderImpl): InputFeatureScreenProvider
}
