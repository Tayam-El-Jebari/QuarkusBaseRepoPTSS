package org.ptss.support.domain.interfaces.cqrs

interface ICommandHandler<in TCommand, TResult> {
    suspend fun handleAsync(command: TCommand): TResult
}