package com.teamfusion.spyglassplus.event

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
    companion object {
        fun <T> pass(): TypedEventResult<T> {
            return TypedEventResult()
        }
    }
}
