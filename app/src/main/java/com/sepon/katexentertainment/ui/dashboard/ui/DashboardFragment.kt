package com.sepon.katexentertainment.ui.dashboard.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sepon.katexentertainment.databinding.FragmentDashboardBinding
import com.sepon.katexentertainment.ui.dashboard.factory.ViewModelFactoryUtil

class DashboardFragment : Fragment() {

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

            movieListData.observe(viewLifecycleOwner, Observer {

            })

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}