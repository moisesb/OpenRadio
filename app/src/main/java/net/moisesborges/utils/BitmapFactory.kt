package net.moisesborges.utils

import android.graphics.Bitmap
import io.reactivex.Maybe
import java.io.InputStream
import java.net.URL
import android.graphics.BitmapFactory as AndroidBitmapFactory

class BitmapFactory {

    fun decode(url: String?): Maybe<Bitmap> {
        return Maybe.fromCallable {
            val imageUrl = URL(url)
            AndroidBitmapFactory.decodeStream(imageUrl.content as InputStream)
        }.onErrorComplete()
    }
}