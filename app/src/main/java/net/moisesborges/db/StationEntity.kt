package net.moisesborges.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "station")
data class StationEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    var name: String,
    var countryCode: String,
    var imageUrl: String,
    var thumbnailUrl: String,
    @Ignore var genres: List<GenreEntity>,
    var streamUrl: String?,
    var streamContentType: String?,
    var streamBitrate: Int?,
    @ColumnInfo(name = "created_at")
    var createdAt: Date?
) {

    constructor() : this(-1, "", "", "", "", emptyList(), null, null, null, null)
}