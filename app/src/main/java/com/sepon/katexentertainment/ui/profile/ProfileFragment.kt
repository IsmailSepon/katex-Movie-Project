package com.sepon.katexentertainment.ui.profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.babu.smartlock.sessionManager.UserSessionManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.sepon.katexentertainment.auth.LoginActivity
import com.sepon.katexentertainment.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment(), CompoundButton.OnCheckedChangeListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    lateinit var   userSession : UserSessionManager
    private lateinit var database: DatabaseReference

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        //val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root


        userSession = UserSessionManager(requireContext())
        database = Firebase.database.reference
        binding.userName.text = "Email: "+userSession.userName

        if (userSession.userPic != ""){
            try {
                ProfilePic.setImageBitmap(convertStringToBitmap(userSession.userPic))
            }catch (e:Exception){

            }

        }

        getAddress()

        binding.switch1.setOnCheckedChangeListener(this)

        binding.logoutbutton.setOnClickListener{
            userSession.userName =""
            userSession.userPic =""
            userSession.userEmail =""
            Firebase.auth.signOut()
            val i = Intent(requireContext(), LoginActivity::class.java)
            startActivity(i)
        }



        return root
    }

    private fun getAddress() {

        val myRef = database.child("user").child("${Firebase.auth.currentUser?.uid}")
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.child("address")
                address.text = "Address: $value"
//                return value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun convertStringToBitmap(string: String?): Bitmap? {
        val byteArray1: ByteArray = Base64.decode(string, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(
            byteArray1, 0,
            byteArray1.size
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

        if (isChecked){
            //dark mood
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            //light mode

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}