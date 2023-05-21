package com.sepon.katexentertainment.auth

import android.Manifest
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.babu.smartlock.sessionManager.UserSessionManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.sepon.katexentertainment.MainActivity
import com.sepon.katexentertainment.R
import kotlinx.android.synthetic.main.reg_login.*
import java.util.*


class RegistrationActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    lateinit var   userSession : UserSessionManager

    var location : FusedLocationProviderClient? = null
    var map : GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var locationRequest: LocationRequest? = null
    var progress : ProgressDialog? = null//= ProgressDialog(this)

    var  mAddress = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_login)
        supportActionBar!!.hide()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getSystemService(Context.LOCATION_SERVICE);
        progress = ProgressDialog(this)

        locationRequest = LocationRequest.create()
        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest!!.setInterval(5000)
        locationRequest!!.setFastestInterval(2000)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        userSession = UserSessionManager(this)
        location = LocationServices.getFusedLocationProviderClient(this)


        val createAcc : Button? = findViewById(R.id.createAccountBtn)
        val email : TextInputEditText? = findViewById(R.id.inputEmailEt)
        val password : TextInputEditText? = findViewById(R.id.inputPassword)
        val confirmPassword : TextInputEditText? = findViewById(R.id.inputConfirmPassword)


        getLocation.setOnClickListener{
            showProgress()
            openLocation(this)
        }
        getContact.setOnClickListener{
            getContactNumber(this)
        }
        camera.setOnClickListener{
            openCamera(this)
        }

        createAcc!!.setOnClickListener{

            val emailtxt : String = email!!.text.toString()
            val pass : String  = password!!.text.toString()
            val conPass : String  = confirmPassword!!.text.toString()
            if (emailtxt == ""){
                email.error = "Input Required"
            }else if (pass == "" || pass.length < 6){
                password.error = "Password Required"
            }else if (conPass == "" || conPass.length < 6){
                confirmPassword.error = "Password Required"
            }else if (pass != conPass){
                confirmPassword.error = "Password Not matched"
            }else{
                goForReg(emailtxt, pass)
            }

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {
                    getCurrentLocation()
                } else {
                    turnOnGPS()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                getCurrentLocation()
            }
        }else if (resultCode == 100){
            //camera
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView5.setImageBitmap(imageBitmap)


        }
    }

   fun showProgress(){
       progress?.setMessage("Loading...");
       progress?.show();
    }

    fun stopProgressbar(){
        progress?.dismiss()
    }

    private fun getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(this)
                        .requestLocationUpdates(locationRequest!!, object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                super.onLocationResult(locationResult)
                                LocationServices.getFusedLocationProviderClient(this@RegistrationActivity)
                                    .removeLocationUpdates(this)
                                if (locationResult.locations.size > 0) {
                                    val index = locationResult.locations.size - 1
                                    val latitude = locationResult.locations[index].latitude
                                    val longitude = locationResult.locations[index].longitude
//                                    AddressText.setText("Latitude: $latitude\nLongitude: $longitude")
//                                    Toast.makeText(this, "Latitude: $latitude\nLongitude: $longitude", Toast.LENGTH_SHORT).sh
                                    Toast.makeText(this@RegistrationActivity, "$latitude\nLongitude: $longitude", Toast.LENGTH_SHORT)
                                        .show()
                                    getAddress(latitude, longitude)
                                }
                            }
                        }, Looper.getMainLooper())
                } else {
                    turnOnGPS()
                }
            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }
    }

    private fun turnOnGPS() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)
        builder.setAlwaysShow(true)
        val result = LocationServices.getSettingsClient(
            applicationContext
        )
            .checkLocationSettings(builder.build())
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(
                    ApiException::class.java
                )
                Toast.makeText(this, "GPS is already tured on", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvableApiException = e as ResolvableApiException
                        resolvableApiException.startResolutionForResult(this, 2)
                    } catch (ex: SendIntentException) {
                        ex.printStackTrace()
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }
    }

    private fun isGPSEnabled(): Boolean {
        var locationManager: LocationManager? = null
        var isEnabled = false
        if (locationManager == null) {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        }
        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return isEnabled
    }


    private fun openCamera(registrationActivity: RegistrationActivity) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, 100)
    }

    private fun getContactNumber(registrationActivity: RegistrationActivity) {

    }

    private fun openLocation(registrationActivity: RegistrationActivity) {
        getCurrentLocation()
    }

    fun getAddress(lat: Double, lng: Double){
        val addresses: List<Address>?
        val geocoder: Geocoder = Geocoder(this, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            lat,
            lng,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        val address = addresses!![0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        val city = addresses[0].locality
        val state = addresses[0].adminArea
        val country = addresses[0].countryName
        val postalCode = addresses[0].postalCode
        var add = "$postalCode, $city, $state, $country"


        Log.v("Location", "Address$add")
        inputaddressEt.text = Editable.Factory.getInstance().newEditable(add)

        Toast.makeText(this, add, Toast.LENGTH_SHORT).show()
        stopProgressbar()

    }

    private fun goForReg(emailtxt: String, pass: String) {
        mAuth!!.createUserWithEmailAndPassword(emailtxt, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val account = mAuth!!.currentUser
                    userSession.userName = account?.email.toString()
                    userSession.userEmail = account?.email.toString()
                    //userSession.userPic = account?.photoUrl.toString()
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    finish()
                   // updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }





}