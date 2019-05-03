package com.midsummer.tesseract.room.entity.account

import androidx.room.*
import io.reactivex.Single

/**
 * Created by NienLe on 4/21/19,April,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */

@Dao
interface AccountDAO {

    @Query("SELECT * FROM EntityAccount")
    fun getAllAccounts() : Single<List<EntityAccount>>

    @Query("SELECT * FROM EntityAccount WHERE address = :address LIMIT 1")
    fun getAccountByAddress(address: String) : Single<EntityAccount>

    @Query("SELECT * FROM EntityAccount WHERE isSelected = :isSelected LIMIT 1")
    fun getActiveAccount(isSelected: Boolean = true) : Single<EntityAccount>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccount(account: EntityAccount) : Single<Long>

    @Update
    fun updateAccount(vararg account: EntityAccount) : Single<Int>

    @Delete
    fun deleteAccount(vararg account: EntityAccount) : Single<Int>

    @Query("DELETE FROM EntityAccount")
    fun kaboom() : Single<Int>

}