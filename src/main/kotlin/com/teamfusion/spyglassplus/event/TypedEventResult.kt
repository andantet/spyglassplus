package com.teamfusion.spyglassplus.event

import java.util.function.Consumer

/**
 * An event result with a value.
 */
class TypedEventResult<T>(
    /**
     * The underlying event result.
     */
    val result: EventResult = EventResult.PASS,

    /**
     * The value of the event result.
     */
    val value: T? = null
) {
    fun ifCancelled(action: Consumer<T>) {
        if (result == EventResult.CANCEL) {
            action.accept(value ?: throw NullPointerException("Typed result value cannot be null here"))
        }
    }

    companion object {
        fun <T> pass(): TypedEventResult<T> {
            return TypedEventResult()
        }

        fun <T> cancel(value: T): TypedEventResult<T> {
            return TypedEventResult(EventResult.CANCEL, value)
        }
    }
}
