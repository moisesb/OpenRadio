package net.moisesborges.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genre")
data class GenreEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var name: String
) {

    constructor(): this("", "")
}