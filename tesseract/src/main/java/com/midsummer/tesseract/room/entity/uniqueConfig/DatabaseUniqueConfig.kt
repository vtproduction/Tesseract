package com.midsummer.tesseract.room.entity.uniqueConfig

import android.content.Context
import android.text.SpannableStringBuilder
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.midsummer.tesseract.common.Config
import com.midsummer.tesseract.room.entity.account.AccountDAO

/**
 * Created by NienLe on 4/21/19,April,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */

@Database(entities = [EntityConfig::class],
    version = Config.Database.VERSION, exportSchema = false)
abstract class DatabaseUniqueConfig : RoomDatabase(){

    abstract fun uniqueConfigDAO(): UniqueConfigDAO

    companion object {
        private var INSTANCE: DatabaseUniqueConfig? = null

        fun getInstance(context: Context): DatabaseUniqueConfig? {
            if (INSTANCE == null) {
                synchronized(DatabaseUniqueConfig::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        DatabaseUniqueConfig::class.java, Config.Database.NAME)
                        .openHelperFactory(SafeHelperFactory
                            .fromUser(SpannableStringBuilder("TEST")))
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}