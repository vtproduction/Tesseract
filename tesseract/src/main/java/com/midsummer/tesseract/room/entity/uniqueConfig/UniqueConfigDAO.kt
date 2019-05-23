package com.midsummer.tesseract.room.entity.uniqueConfig

import androidx.room.*
import com.midsummer.tesseract.room.entity.account.EntityAccount
import io.reactivex.Single

/**
 * Created by NienLe on 4/21/19,April,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */

@Dao
interface UniqueConfigDAO {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(config: EntityConfig) : Single<Long>

    @Query("SELECT * FROM EntityConfig WHERE unchangedId = 1 LIMIT 1")
    fun read() : Single<EntityConfig>

    @Update
    fun update(config: EntityConfig) : Single<Int>

    @Query("DELETE FROM EntityConfig")
    fun delete() : Single<Int>

}