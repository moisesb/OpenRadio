package net.moisesborges.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

abstract class BaseViewModeTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
}