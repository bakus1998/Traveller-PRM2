package pl.krzysztofbaka.projekt2.modelDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long =0,
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val path: String,
    @ColumnInfo
    val description: String,
    @ColumnInfo
    val loc: String,
    @ColumnInfo
    val date: String,
    @ColumnInfo
    val latitude: String,
    @ColumnInfo
    val longitude: String
)
