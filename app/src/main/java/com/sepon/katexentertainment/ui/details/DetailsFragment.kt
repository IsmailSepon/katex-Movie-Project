package com.sepon.katexentertainment.ui.details

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextSwitcher
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.sepon.katexentertainment.communicator.Communicator
import com.sepon.katexentertainment.databinding.FragmentDetailsBinding
import com.sepon.katexentertainment.databinding.FragmentSearchBinding
import com.sepon.katexentertainment.ui.search.data.adapter.MoviesSearchAdapter
import com.sepon.katexentertainment.ui.dashboard.factory.ViewModelFactoryUtil
import com.sepon.katexentertainment.ui.search.SearchFragmentDirections
import com.sepon.katexentertainment.ui.search.data.adapter.ClickListener
import com.sepon.katexentertainment.ui.search.data.model.ResultsItem
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_search.*

class DetailsFragment : Fragment() {


    private val searchViewModel: DetailsViewModel by activityViewModels {
        ViewModelFactoryUtil.provideMovieDetailsFactory(this)
    }

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    var movieDetails : ResultsItem? = null
    lateinit var  communicator : Communicator

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


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}