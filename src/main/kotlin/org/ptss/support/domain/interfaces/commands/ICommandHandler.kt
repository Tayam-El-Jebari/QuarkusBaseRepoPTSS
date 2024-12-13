package org.ptss.support.domain.interfaces.commands

interface ICommandHandler<in TCommand, TResult> {
    suspend fun handleAsync(command: TCommand): TResult
}
