package pl.krzysztofbaka.projekt2.activities

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.krzysztofbaka.projekt2.databinding.ActivityAddPhotoBinding
import android.net.Uri
import android.provider.MediaStore
import pl.krzysztofbaka.projekt2.Shared
import pl.krzysztofbaka.projekt2.modelDB.ImageDto
import pl.krzysztofbaka.projekt2.service.LocationTrackingService
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


class AddPhotoActivity : AppCompatActivity() {
    val binding by lazy { ActivityAddPhotoBinding.inflate(layoutInflater) }
    lateinit var imagePath : String
    lateinit var date : String
    lateinit var dateToSave : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val paintVals = Paint().apply {
            color = (Shared.settings?.color ?: Int) as Int
            textSize = (Shared.settings?.size ?: Float) as Float
            style = Paint.Style.FILL
        }

        imagePath = intent.getStringExtra("IMAGE_PATH").toString()

        var uri = intent.getParcelableExtra<Uri>("URI_EXTRA")

        val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val newImageBitmap = Bitmap.createBitmap(imageBitmap.width, imageBitmap.height, imageBitmap.config)
        val canvas = Canvas(newImageBitmap)

        canvas.drawPaint(paintVals)
        canvas.drawBitmap(imageBitmap,0F,0F,null)

        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        dateToSave = SimpleDateFormat("yyyyMMddHHmmss").format(Date())

        canvas.drawText(date, 10F, canvas.height.toFloat()-30F, paintVals)
        val pos = canvas.height.toFloat()-30F-10-(Shared.settings?.size ?: Float) as Float;
        canvas.drawText(LocationTrackingService.country.toString(),10F,pos,paintVals)
        canvas.drawText(LocationTrackingService.city.toString(),10F,pos-10-(Shared.settings?.size ?: Float) as Float,paintVals)

        binding.imageView.setImageBitmap(newImageBitmap)

        binding.buttonAdd.setOnClickListener {
            val finalPath = "Picture_"+dateToSave+".jpg";

            val createdFile = File.createTempFile(finalPath, "", filesDir).apply {
                val out = FileOutputStream(this)
                newImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
            }

            val toDatabase = ImageDto(
                name = binding.editTextName.text.toString(),
                path= createdFile.absolutePath,
                description = binding.editTextDescription.text.toString(),
                loc= LocationTrackingService.adress.toString(), date = date,
                longitude = LocationTrackingService.lon.toString(), latitude = LocationTrackingService.lat.toString()
            )
            thread {
                Shared.db?.image?.save(toDatabase)
            }
            finish()
        }

        binding.buttonCancel.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
    }
}