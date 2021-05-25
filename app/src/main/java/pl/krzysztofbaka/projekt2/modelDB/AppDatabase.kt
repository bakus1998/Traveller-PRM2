package pl.krzysztofbaka.projekt2.modelDB

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ImageDto::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase(){
    abstract val image: ImageDao

}