package net.moisesborges.db

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "genre")
data class GenreEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var name: String
) {

    @Ignore
    constructor(): this("", "")
}