package com.badmanners.idttable.di

import com.badmanners.idttable.data.data_source.RandomStringDataSource
import com.badmanners.idttable.data.mapper.TableDataDtoMapper
import com.badmanners.idttable.data.mapper.TableDataDtoMapperImpl
import com.badmanners.idttable.data.repository.TableRepositoryImpl
import com.badmanners.idttable.domain.repository.TableRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindTableRepository(impl: TableRepositoryImpl): TableRepository

    @Binds
    abstract fun bindTableDataDtoMapper(impl: TableDataDtoMapperImpl): TableDataDtoMapper

    companion object {

        @Provides
        fun provideRandomStringDataSource(): RandomStringDataSource = RandomStringDataSource()
    }
}