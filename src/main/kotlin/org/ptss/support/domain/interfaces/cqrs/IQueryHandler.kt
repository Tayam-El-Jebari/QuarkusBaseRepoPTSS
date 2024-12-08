package org.ptss.support.domain.interfaces.cqrs

interface IQueryHandler<in TQuery, TResult> {
    suspend fun handleAsync(query: TQuery): TResult
}