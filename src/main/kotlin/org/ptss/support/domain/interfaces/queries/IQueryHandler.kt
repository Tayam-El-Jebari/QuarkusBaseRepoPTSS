package org.ptss.support.domain.interfaces.queries

interface IQueryHandler<in TQuery, TResult> {
    suspend fun handleAsync(query: TQuery): TResult
}
