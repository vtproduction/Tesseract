package com.midsummer.tesseract.room.entity.uniqueConfig

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by NienLe on 2019-04-28,April,2019
 * Midsummer.
 * Ping me at nienbkict@gmail.com
 * Happy coding ^_^
 */
@Entity(tableName = "EntityConfig")
data class EntityConfig(

    /**
     * The id of object is always 1, to make sure there is no other object
     * stored on the DB
     */
    @PrimaryKey val unchangedId: Int = 1,

    /**
     * Contain all encrypted data of config object
     */
    val encryptedData: String,

    /**
     * when we insert new config
     */
    val createdAt: Long,

    /**
     * when we update the config (change the metadata...)
     */
    var updatedAt: Long,

    /**
     * The serializable of any object that contain extra data of EntityConfig
     */
    var metadata: String = ""
)