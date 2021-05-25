package pl.krzysztofbaka.projekt2.modelDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {
    @Insert
    fun save(image: ImageDto): Long

    @Query("SELECT * FROM images;")
    fun getAll(): List<ImageDto>

    @Query("DELETE FROM images WHERE id= :idtoDel")
    fun delete(idtoDel:String)
}