package com.badmanners.idttable.domain.validator

interface TableSizeValidator {
    fun validate(rows: Int?, columns: Int?): TableSizeValidationState
}