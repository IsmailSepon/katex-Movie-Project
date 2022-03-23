package com.sepon.katexentertainment.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.sepon.katexentertainment.databinding.FragmentSearchBinding
import com.sepon.katexentertainment.ui.search.data.adapter.MoviesSearchAdapter
import com.sepon.katexentertainment.ui.dashboard.factory.ViewModelFactoryUtil
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {


    private val searchViewModel: SearchViewModel by activityViewModels {
        ViewModelFactoryUtil.provideMovieSearchListFactory(this)
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater).apply {
            viewModel = searchViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        searchViewModel.apply {

            movieSearchListData.observe(viewLifecycleOwner, Observer { list ->
                list.let { it1 ->
                    search_movie_recyclerview.also {
                        it.layoutManager  = LinearLayoutManager(requireActivity().applicationContext, LinearLayoutManager.VERTICAL, false)
                        it.setHasFixedSize(true)
                        if (list != null){
                            it.adapter = MoviesSearchAdapter(list, requireActivity().applicationContext )
                        }
                    }
                }
            })




        }
    }

    private fun init() {
        tabs.addTab(tabs.newTab().setText("All"))
        tabs.addTab(tabs.newTab().setText("Action"))
        tabs.addTab(tabs.newTab().setText("Comedy"))
        tabs.addTab(tabs.newTab().setText("Drama"))
        tabs.addTab(tabs.newTab().setText("Romance"))
        tabs.addTab(tabs.newTab().setText("Animation"))
        tabs.addTab(tabs.newTab().setText("Crime"))
        tabs.addTab(tabs.newTab().setText("Horror"))
        tabs.addTab(tabs.newTab().setText("Western"))
        tabs.addTab(tabs.newTab().setText("Science Fiction"))

        tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Toast.makeText(requireContext(), "${tab.text}", Toast.LENGTH_SHORT).show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}