package net.moisesborges.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import net.moisesborges.extensions.get
import org.amshove.kluent.mock
import org.junit.Before
import org.junit.Rule

abstract class BaseViewModeTest : LifecycleOwner {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val lifecycle = LifecycleRegistry(mock())

    @Before fun prepareTests() {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        setup()
    }

    abstract fun setup()

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

    fun <T> LiveData<T>.extractValue(): T {
        val observer: Observer<T> = mock()
        this.observe(this@BaseViewModeTest, observer)
        return this.get()
    }
}