package com.sepon.katexentertainment.ui.dashboard.ui

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
import com.sepon.katexentertainment.databinding.FragmentDashboardBinding
import com.sepon.katexentertainment.ui.dashboard.data.adapter.MoviesAdapter
import com.sepon.katexentertainment.ui.dashboard.data.adapter.StarMoviesAdapter
import com.sepon.katexentertainment.ui.dashboard.data.model.ItemsItem
import com.sepon.katexentertainment.ui.dashboard.data.model.StarModel
import com.sepon.katexentertainment.ui.dashboard.factory.ViewModelFactoryUtil
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : Fragment(), MoviesAdapter.RecyclerViewClickListener {

    private val dashboardViewModel: DashboardViewModel by activityViewModels {
        ViewModelFactoryUtil.provideMovieListFactory(this)
    }
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater).apply {
            viewModel = dashboardViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        dashboardViewModel.apply {

            movieListData.observe(viewLifecycleOwner, Observer { list ->
                list.let { it1 ->
                    movie_recyclerview.also {
                        it.layoutManager  =StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL) //LinearLayoutManager(requireActivity().applicationContext, staggeredGridLayoutManager, false)
                        it.setHasFixedSize(true)
                        it.adapter = MoviesAdapter(it1, requireActivity().applicationContext )
                    }
                }
            })

            starMovieListData.observe(requireActivity(), Observer { list ->
                list.let {
                    star_movie_recyclerview.also {
                        it.layoutManager  = LinearLayoutManager(requireActivity().applicationContext, LinearLayoutManager.VERTICAL, false)
                        it.setHasFixedSize(true)
                        it.adapter = StarMoviesAdapter(list as ArrayList<StarModel?>?, requireActivity().applicationContext )
                    }
                }



            })

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRecyclerViewItemClick(view: View, movie: ItemsItem) {
        TODO("Not yet implemented")
    }
}