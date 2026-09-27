package com.badmanners.idttable.tea

/**
 * Wraps a failure thrown inside a [CommandsFlowHandler]. [kotlinx.coroutines.CancellationException]
 * is deliberately not wrapped and propagates as is.
 */
class CommandsFlowHandlerException(cause: Throwable) :
    RuntimeException("Commands flow handler failed", cause)
