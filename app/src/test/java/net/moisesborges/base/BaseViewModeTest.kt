/*
 * MIT License
 *
 * Copyright (c) 2019 Moisés Borges dos Anjos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.moisesborges.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import net.moisesborges.extensions.get
import org.amshove.kluent.mock
import org.junit.Before
import org.junit.Rule

abstract class BaseViewModeTest : LifecycleOwner {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val lifecycleOwner: LifecycleOwner = mock()

    private val lifecycle = LifecycleRegistry(lifecycleOwner)

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