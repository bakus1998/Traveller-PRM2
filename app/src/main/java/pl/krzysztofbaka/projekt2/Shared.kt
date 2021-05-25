package pl.krzysztofbaka.projekt2

import pl.krzysztofbaka.projekt2.modelDB.AppDatabase
import pl.krzysztofbaka.projekt2.modelDB.Image
import pl.krzysztofbaka.projekt2.models.SettingModel

object Shared {
    var db: AppDatabase? = null
    var settings: SettingModel? = null
    var list = mutableListOf<Image>()
}