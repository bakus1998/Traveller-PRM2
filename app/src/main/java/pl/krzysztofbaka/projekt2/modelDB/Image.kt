package pl.krzysztofbaka.projekt2.modelDB

data class Image(
    val id: Long,
    val name: String,
    val path: String,
    val description: String,
    val loc: String,
    val date: String,
    val latitude: String,
    val longitude: String
)