package pl.krzysztofbaka.projekt2.service

import android.Manifest
import android.R
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import pl.krzysztofbaka.projekt2.MainActivity
import pl.krzysztofbaka.projekt2.Shared
import java.util.*
import kotlin.concurrent.thread


class LocationTrackingService : Service() {

    lateinit var clientLocation: FusedLocationProviderClient

    companion object {
        var lat: String? = null
        var lon: String? = null
        var myLocation: Location? = null
        var adress: String? = null
        var country: String? = null
        var city: String? = null
        var flag: Int = 0
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        clientLocation = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        getLocation()
        //return START_NOT_STICKY
        return super.onStartCommand(intent, flags, startId);
    }


    private fun getLocation() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000L
        locationRequest.fastestInterval = 2000L


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //Toast.makeText(this, "Nie nadano uprawnień GPS!", Toast.LENGTH_LONG).show()
            stopSelf()
            return
        }

        var locCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    myLocation = location
                    val geocoder = Geocoder(this@LocationTrackingService, Locale.getDefault())
                    val addresses =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    lat = location.latitude.toString()
                    lon = location.longitude.toString()
                    city = addresses[0].locality
                    adress = addresses[0].getAddressLine(0)
                    country = addresses[0].countryName
                }

                thread {
                    Shared.db?.image?.getAll()
                }

                for (img in Shared.list) {
                    if (img.longitude != "" && img.latitude != "") {
                        var locToCheck = Location(LocationManager.GPS_PROVIDER);
                        locToCheck.latitude = img.latitude.toDouble()
                        locToCheck.longitude = img.longitude.toDouble()
                        print(locToCheck.latitude)
                        print(locToCheck.longitude)
                        print(myLocation)

                        val dist = locToCheck.distanceTo(myLocation)
                        print(dist)
                        val intent = Intent(this@LocationTrackingService, MainActivity::class.java)

                        var range = Shared.settings?.range ?: Int
                        if (dist / 1000 <= range.toString().toFloat()) {
                            print("TEST")
                            if(flag==0) {
                                val notification = NotificationCompat.Builder(
                                    this@LocationTrackingService,
                                    "pl.krzysztofbaka.projekt2.Geofence"
                                )
                                    .setSmallIcon(R.drawable.sym_def_app_icon)
                                    .setContentTitle("Jesteś w pobliżu miejsca w którym robiłeś zdjęcie!")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setContentIntent(
                                        PendingIntent.getActivity(
                                            this@LocationTrackingService,
                                            0,
                                            intent,
                                            0
                                        )
                                    )
                                    .build()
                                this@LocationTrackingService.getSystemService(NotificationManager::class.java)
                                    ?.notify(1, notification)
                                flag++;
                            }
                        }
                    }
                }
            }
        }
        clientLocation.requestLocationUpdates(locationRequest, locCallback, Looper.myLooper())
    }


}