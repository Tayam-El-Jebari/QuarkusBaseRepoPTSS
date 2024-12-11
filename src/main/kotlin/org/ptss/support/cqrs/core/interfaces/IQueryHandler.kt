package org.ptss.support.cqrs.core.interfaces

interface IQueryHandler<in TQuery, TResult> {
    suspend fun handleAsync(query: TQuery): TResult
}
