package com.elanyudho.core.abstraction

interface BaseMapper<Raw, Domain> {
    fun mapToDomain(raw: Raw): Domain
    fun mapToRaw(domain: Domain): Raw
}