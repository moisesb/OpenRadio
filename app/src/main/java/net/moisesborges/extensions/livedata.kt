package net.moisesborges.extensions

import androidx.lifecycle.LiveData
import java.lang.IllegalStateException

fun <T> LiveData<T>.get(): T = value ?: throw IllegalStateException("Data cannot be null")