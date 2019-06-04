package com.midsummer.tesseract.common.dagger

import com.midsummer.tesseract.core.CoreFunctions
import com.midsummer.tesseract.core.Tesseract
import dagger.Component
import dagger.Module
import javax.inject.Singleton

/**
 * Created by cityme on 03,June,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {


    fun inject(tesseract: Tesseract?)
}