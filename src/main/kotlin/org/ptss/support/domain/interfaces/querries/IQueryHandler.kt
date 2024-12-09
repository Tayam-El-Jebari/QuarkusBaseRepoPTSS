package org.ptss.support.domain.interfaces.querries

interface IQueryHandler<in TQuery, TResult> {
    suspend fun handleAsync(query: TQuery): TResult
}