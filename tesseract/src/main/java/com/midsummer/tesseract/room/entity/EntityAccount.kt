package com.midsummer.tesseract.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by NienLe on 4/21/19,April,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 *
 */
@Entity(tableName = "EntityAccount")
data class EntityAccount (

    /**
     * Address of account, unique, must be all lowercase
     */
    @PrimaryKey val address: String,

    /**
     * determine if this account is current account or not
     */
    var isSelected: Boolean,

    /**
     * Contain all encrypted data of wallet associated with the address,
     * include recovery phrase, private key, HDPath,...
     */
    val encryptedData: String,

    /**
     * when we insert the account
     */
    val createdAt: Long,

    /**
     * when we update the account (change the selected state, metadata...)
     */
    var updatedAt: Long,

    /**
     * The serializable of any object that contain extra data of EntityAccount
     */
    var metadata: String
)