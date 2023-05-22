package com.sepon.katexentertainment.ui.details

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.babu.smartlock.sessionManager.UserSessionManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sepon.katexentertainment.auth.LoginActivity
import com.sepon.katexentertainment.communicator.Communicator
import com.sepon.katexentertainment.databinding.FragmentDetailsBinding
import com.sepon.katexentertainment.ui.dashboard.data.model.StarModel
import com.sepon.katexentertainment.ui.dashboard.factory.ViewModelFactoryUtil
import com.sepon.katexentertainment.ui.search.data.model.ResultsItem
import kotlinx.android.synthetic.main.fragment_details.*
import java.lang.reflect.Type


class DetailsFragment : Fragment() {


    private val searchViewModel: DetailsViewModel by activityViewModels {
        ViewModelFactoryUtil.provideMovieDetailsFactory(this)
    }

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    var movieDetails : ResultsItem? = null
    lateinit var  communicator : Communicator

    lateinit var   userSession : UserSessionManager
    var progress : ProgressDialog? = null//= ProgressDialog(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater).apply {
            viewModel = searchViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        movieDetails = DetailsFragmentArgs.fromBundle(requireArguments()).movie
        communicator = activity as Communicator
        userSession = UserSessionManager(requireActivity())
        progress = ProgressDialog(requireActivity().applicationContext)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        movieDetails.let {

            Glide.with(requireActivity())
                .load(it!!.image!!)
                .into(movie_imageview_banner)
            title.text  = it.title
            description.text = it.description
        }

        back.setOnClickListener {
            communicator.showBottomNav()
            findNavController().popBackStack()
        }

        watchNowBtn.setOnClickListener {
            val i = Intent(requireActivity(), LoginActivity::class.java)//Intent(this, LoginActivity::class.java)
            startActivity(i)
            requireActivity().finish()
        }


        imageView7.setOnClickListener{
//            userSession.
//            userSession.favoritMovies =

            showProgress()
            val movei = StarModel()
            movei.title = movieDetails!!.title//it.title.toString()
            movei.fulltitle = movieDetails!!.description//it.description.toString()
            movei.image = movieDetails!!.image//!!.image!!

            val list = getCompaniesList()
            list.add(movei)

            setList(list)


            stopProgressbar()
            findNavController().popBackStack()
        }


    }

    private fun <T> setList(list: ArrayList<T>?) {
        val gson = Gson()
        val json = gson.toJson(list)
        userSession.favoritMovies = json
    }


    fun showProgress(){
        progress?.setMessage("Loading...");
        progress?.show();
    }

    fun stopProgressbar(){
        progress?.dismiss()
    }

    fun getCompaniesList(): ArrayList<StarModel> {
        if (userSession.favoritMovies != "") {
            val gson = Gson()
            val companyList: ArrayList<StarModel>
            val string: String? = userSession.favoritMovies//.getString(key, null)
            val type: Type = object : TypeToken<ArrayList<StarModel?>?>() {}.getType()
            companyList = gson.fromJson<ArrayList<StarModel>>(string, type)
            return companyList
        }
        return ArrayList<StarModel>()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}