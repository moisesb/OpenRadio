package net.moisesborges.utils

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RxSchedulers {

    fun io(): Scheduler {
        return Schedulers.io()
    }

    fun mainThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}