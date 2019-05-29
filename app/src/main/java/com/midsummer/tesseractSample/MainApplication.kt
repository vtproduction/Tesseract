package com.midsummer.tesseractSample

import android.app.Application
import com.midsummer.tesseract.core.Tesseract
import java.lang.ref.WeakReference

/**
 * Created by cityme on 29,May,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
class MainApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        Tesseract.openTheWormHole(WeakReference(this))
    }
}