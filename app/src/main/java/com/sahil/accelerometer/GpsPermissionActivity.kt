package com.sahil.accelerometer

import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes

class GpsPermissionActivity : AppCompatActivity() {
    private val requestCheck = 55
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(1)
        window.setFlags(1024,1024)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gps_permission)
        displayLocationSettingsRequest()
    }

    private fun displayLocationSettingsRequest(){
        val mLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10*1000).setFastestInterval(1*1000)
        val settings = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)
        settings.setAlwaysShow(true)
        val results = LocationServices.getSettingsClient(this).checkLocationSettings(settings.build())
        results.addOnCompleteListener {
            try {
                 it.getResult(ApiException::class.java)
            }catch (ex: ApiException){
                when(ex.statusCode){
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED->{
                        try{
                            val res = ex as ResolvableApiException
                            res.startResolutionForResult(this,requestCheck)

                        }catch (e: IntentSender.SendIntentException){
                            Toast.makeText(this,"PendingIntent unable to execute request.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE->{
                        Toast.makeText(this,"Something wrong with your GPS", Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            requestCheck->{
                if(resultCode== RESULT_OK){
                    // got GPS Location

                }else{
                    // user Denied

                }
            }

        }
    }



}