package com.badmanners.idttable.feature.table.impl.di

import com.badmanners.idttable.feature.table.api.TableFeatureScreenProvider
import com.badmanners.idttable.feature.table.impl.provider.TableFeatureScreenProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TableFeatureModule {

    @Binds
    abstract fun bindTableScreenProvider(impl: TableFeatureScreenProviderImpl): TableFeatureScreenProvider
}
