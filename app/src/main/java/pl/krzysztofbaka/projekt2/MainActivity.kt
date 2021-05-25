package pl.krzysztofbaka.projekt2

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.room.Room
import pl.krzysztofbaka.projekt2.activities.AddPhotoActivity
import pl.krzysztofbaka.projekt2.activities.GalleryActivity
import pl.krzysztofbaka.projekt2.activities.SettingsActivity
import pl.krzysztofbaka.projekt2.databinding.ActivityMainBinding
import pl.krzysztofbaka.projekt2.modelDB.AppDatabase
import pl.krzysztofbaka.projekt2.modelDB.Image
import pl.krzysztofbaka.projekt2.models.SettingModel
import pl.krzysztofbaka.projekt2.service.LocationTrackingService
import java.io.File
import kotlin.concurrent.thread

private const val REQUEST_CODE_PERMISSIONS_CAMERA = 10

class MainActivity : AppCompatActivity() {
    private val buttonShotPicture by lazy { findViewById<Button>(R.id.button_photo) }
    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    lateinit var intentAddActivity: Intent
    lateinit var intentGalleryActivity: Intent
    lateinit var intentSettingsActivity: Intent
    lateinit var filePathFromUri: File;
    lateinit var loc: LocationTrackingService;
    var allPerm = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            val arr = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this, arr, 100);
            //startLocationService()
        }

        registerChannel()
        Shared.settings = SettingModel(Color.WHITE,40f,1);
        startLocationService()
        LocationTrackingService.flag=0

        Shared.db = Room.databaseBuilder(this,AppDatabase::class.java,"imagedb").build()
        intentAddActivity = Intent(this, AddPhotoActivity::class.java)
        intentGalleryActivity = Intent(this, GalleryActivity::class.java)
        intentSettingsActivity = Intent(this, SettingsActivity::class.java)

        binding.buttonSettings.setOnClickListener {
            startActivity(intentSettingsActivity)
        }

        buttonShotPicture.setOnClickListener {
            startLocationService()
            val uri = generateUri()
            val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE).let {
                it.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            startActivityForResult(intentCamera, 1)
            intentAddActivity.putExtra("URI_EXTRA", uri)

        }
    }


    private fun generateUri(): Uri {
        filePathFromUri = filesDir.resolve(getRandomString(20)+".jpg").also {
            it.writeText("")
        }

        return FileProvider.getUriForFile(
            this,
            "pl.krzysztofbaka.projekt2.FileProvider",
                filePathFromUri
        )
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            intentAddActivity.putExtra("IMAGE_PATH",filePathFromUri.absolutePath)
            startActivity(intentAddActivity);
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    fun startGallery(v: View){
        startActivity(intentGalleryActivity)
    }

    override fun onResume() {
        super.onResume()
        thread {
            Shared.db?.image?.getAll()?.let {
                val list = it.mapNotNull {
                    Image(
                        it.id,
                        it.name,
                        it.path,
                        it.description,
                        it.loc,
                        it.date,
                        it.latitude,
                        it.longitude
                    )
                }
                Shared.list = list as MutableList<Image>
            }
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.allPerm = true
            }
            print(requestCode)
            startLocationService()
            if (!allPerm) {
                Toast.makeText(this, "Brak wszystkich uprawnień!", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun startLocationService() {
        loc = LocationTrackingService();
            val serviceIntent = Intent(this, LocationTrackingService::class.java)
                startService(serviceIntent)
    }


    private fun registerChannel() {
        getSystemService(NotificationManager::class.java).let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val nc = NotificationChannel(
                    "pl.krzysztofbaka.projekt2.Geofence",
                    "Geofences",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                it.createNotificationChannel(nc)
            }
        }
    }
}