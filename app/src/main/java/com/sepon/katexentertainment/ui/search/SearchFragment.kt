package com.sepon.katexentertainment.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.sepon.katexentertainment.communicator.Communicator
import com.sepon.katexentertainment.databinding.FragmentSearchBinding
import com.sepon.katexentertainment.ui.dashboard.factory.ViewModelFactoryUtil
import com.sepon.katexentertainment.ui.search.data.adapter.ClickListener
import com.sepon.katexentertainment.ui.search.data.adapter.MoviesSearchAdapter
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {


    private val searchViewModel: SearchViewModel by activityViewModels {
        ViewModelFactoryUtil.provideMovieSearchListFactory(this)
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    lateinit var  communicator : Communicator

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
                list.let {
                    search_movie_recyclerview.also { it1 ->
                        it1.layoutManager  = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                        it1.setHasFixedSize(true)
                        if (list != null){
                            it1.adapter = MoviesSearchAdapter(list, requireActivity(), ClickListener{
                                communicator.hideBottomNav()
                                findNavController().navigate(SearchFragmentDirections.actionSearchFragment2ToDetailsFragment(it))

                            })
                        }
                    }
                }
            })




        }
    }


    override fun onResume() {
        super.onResume()
        communicator.showBottomNav()
    }

    private fun init() {

        communicator = activity as Communicator
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
                searchViewModel.getMovies(tab.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        search_clear.setOnClickListener {
            findNavController().popBackStack()

        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length!! >= 2){
                    searchViewModel.getMovies(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}