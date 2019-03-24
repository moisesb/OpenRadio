package net.moisesborges.base

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.schedulers.Schedulers
import net.moisesborges.utils.RxSchedulers

val testRxSchedulers: RxSchedulers = mock {
    on { io() } doReturn Schedulers.trampoline()
    on { mainThread() } doReturn Schedulers.trampoline()
}