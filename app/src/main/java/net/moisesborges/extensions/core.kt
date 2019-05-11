package net.moisesborges.extensions

inline fun <T, R> withUnwrapped(receiver: T?, block: T.() -> R): R {
    if (receiver != null) {
        return with(receiver, block)
    } else {
        throw NullPointerException("receiver cannot be null")
    }
}