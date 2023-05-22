package com.sepon.katexentertainment.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sepon.katexentertainment.databinding.FragmentNotificationsBinding
import com.sepon.katexentertainment.ui.dashboard.data.adapter.MoviesAdapter
import com.sepon.katexentertainment.ui.dashboard.data.adapter.StarMoviesAdapter
import com.sepon.katexentertainment.ui.dashboard.data.model.StarModel
import com.sepon.katexentertainment.ui.dashboard.factory.ViewModelFactoryUtil
import com.sepon.katexentertainment.ui.dashboard.ui.DashboardViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*

class NotificationsFragment : Fragment() {
    private val dashboardViewModel: DashboardViewModel by activityViewModels {
        ViewModelFactoryUtil.provideMovieListFactory(this)
    }
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        dashboardViewModel.apply {

            starMovieListData.observe(requireActivity(), Observer { list ->
                list.let {

                    binding.starMovieRecyclerview.also {
                        it.layoutManager  = LinearLayoutManager(requireActivity().applicationContext, LinearLayoutManager.VERTICAL, false)
                        it.setHasFixedSize(true)
                        it.adapter = StarMoviesAdapter(list as ArrayList<StarModel?>?, requireActivity().applicationContext )
                    }
                }



            })

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}