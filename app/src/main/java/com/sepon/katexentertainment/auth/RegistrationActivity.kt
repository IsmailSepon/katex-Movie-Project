package com.sepon.katexentertainment.auth

import android.Manifest
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.babu.smartlock.sessionManager.UserSessionManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sepon.katexentertainment.MainActivity
import com.sepon.katexentertainment.R
import kotlinx.android.synthetic.main.reg_login.*
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class RegistrationActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    lateinit var   userSession : UserSessionManager

    var location : FusedLocationProviderClient? = null
    var map : GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var locationRequest: LocationRequest? = null
    var progress : ProgressDialog? = null//= ProgressDialog(this)

    val CAMERA_PERM_CODE = 101
    val CONTACT_PERM_CODE = 103
    val SELECT_PHONE_NUMBER = 102

    private lateinit var database: DatabaseReference
    var firebaseDatabase: FirebaseDatabase? = null

    var  mAddress = ""
    var  mNumber = ""
//    var  mAddress = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_login)
        supportActionBar!!.hide()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getSystemService(Context.LOCATION_SERVICE);
        progress = ProgressDialog(this)

        database = Firebase.database.reference


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
            //getContactNumber(this)
            askContactPermissions()
        }
        camera.setOnClickListener{
//            openCamera(this)
            askCameraPermissions()
        }

        createAcc!!.setOnClickListener{
            val emailtxt : String = email!!.text.toString()
            val pass : String  = password!!.text.toString()
            val conPass : String  = confirmPassword!!.text.toString()
            val number  = inputphoneEt.text


            val pattern: Pattern = Pattern.compile("((0|44|\\+44|\\+44\\s*\\(0\\)|\\+44\\s*0)\\s*)?7(\\s*[0-9]){9}")
            val matcher: Matcher = pattern.matcher(number)

            if (inputNameEt.text.isNullOrEmpty()){
                inputNameEt.error = "name Required!"
            }else if (!matcher.matches()){
                inputphoneEt.error = "Invalid UK Number"
            }else if(inputaddressEt.text.isNullOrEmpty()) {
                inputaddressEt.error = "Address Required!"
            } else if (emailtxt == ""){
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
        }else if(requestCode == CAMERA_PERM_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera(this);
            }else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == CONTACT_PERM_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getContactNumber(this);
            }else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Toast.makeText(this, "result $resultCode", Toast.LENGTH_SHORT).show()
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                getCurrentLocation()
            }
        }else if (requestCode == 100 && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView5.setImageBitmap(imageBitmap)
            userSession.userPic = imageBitmap.toString()
        }else if (requestCode == SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
            if (data != null) {
                contactPicked1(data)
            }
        }
    }


    private fun contactPicked1(data: Intent) {
        var cursor: Cursor? = null
        try {
            var phoneNo: String? = null
            var name: String? = null
            val uri = data.data
            cursor = contentResolver.query(uri!!, null, null, null, null)
            cursor!!.moveToFirst()
            val phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
//            name = cursor.getString(nameIndex)
            phoneNo = cursor.getString(phoneIndex)
            inputphoneEt.text = Editable.Factory.getInstance().newEditable(phoneNo)
        } catch (e: Exception) {
            e.printStackTrace()
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

    private fun askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERM_CODE)
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERM_CODE)
        } else {
            openCamera(this)
        }
    }
    private fun askContactPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), CONTACT_PERM_CODE)
        } else {
            getContactNumber(this)
        }
    }
    private fun openCamera(registrationActivity: RegistrationActivity) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, 100)
    }

    private fun getContactNumber(registrationActivity: RegistrationActivity) {

        val i = Intent(Intent.ACTION_PICK)
        i.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        startActivityForResult(i, SELECT_PHONE_NUMBER)
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
        showProgress()
        mAuth!!.createUserWithEmailAndPassword(emailtxt, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val account = mAuth!!.currentUser
                    userSession.userName = account?.email.toString()
                    userSession.userEmail = account?.email.toString()
                    val uid = mAuth!!.currentUser?.uid

                    var user = User()
                    user.name = inputNameEt.text.toString()
                    user.address = inputaddressEt.text.toString()
                    user.mail = account?.email
                    user.password = inputConfirmPassword.text.toString()

                    saveInformation(uid, user);
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
                    stopProgressbar()
                    //updateUI(null)
                }
            }
    }

    private fun saveInformation(uid: String?, user: User) {
        val ref = database.child("user").child("$uid")//.child(ob.name.toString())
        ref.setValue(user)
    }


}