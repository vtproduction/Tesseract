package com.midsummer.tesseract.room.entity.account

import android.content.Context
import android.text.SpannableStringBuilder
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.midsummer.tesseract.common.Config

/**
 * Created by NienLe on 4/21/19,April,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */

@Database(entities = [EntityAccount::class], version = Config.Databse.VERSION, exportSchema = false)
abstract class DatabaseAccount : RoomDatabase(){

    abstract fun accountDAO(): AccountDAO

    companion object {
        private var INSTANCE: DatabaseAccount? = null

        fun getInstance(context: Context): DatabaseAccount? {
            if (INSTANCE == null) {
                synchronized(DatabaseAccount::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        DatabaseAccount::class.java, Config.Databse.NAME)
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