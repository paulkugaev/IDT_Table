package com.badmanners.idttable.di

import com.badmanners.idttable.domain.validator.TableSizeValidator
import com.badmanners.idttable.domain.validator.TableSizeValidatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindTableSizeValidator(impl: TableSizeValidatorImpl): TableSizeValidator
}