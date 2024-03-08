package com.teamfusion.spyglassplus.event

/**
 * The result of an event callback.
 */
enum class EventResult {
    /**
     * Cancel the source function.
     */
    CANCEL,

    /**
     * Continue and pass to the next callback.
     */
    PASS
}
