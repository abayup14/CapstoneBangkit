package com.haire.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.haire.ListNotifikasiUserQuery
import com.haire.ViewModelFactory
import com.haire.databinding.FragmentNotificationBinding
import com.haire.util.showLoading
import com.haire.util.showText

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private var adapter: NotifikasiAdapter? = null
    private val viewModel by viewModels<NotificationViewModel> { ViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            viewModel.getNotification(user.id)
            viewModel.listNotification.observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    binding.noNotif.visibility = View.VISIBLE
                } else {
                    binding.noNotif.visibility = View.GONE
                    setData(it!!)
                }
            }
        }

        viewModel.toastMsg.observe(viewLifecycleOwner) {
            showText( requireActivity(), it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it, binding.progressBar)
        }
        return binding.root
    }

    private fun setData(listNotifikasi: List<ListNotifikasiUserQuery.Notifikasi?>) {
        adapter = NotifikasiAdapter(listNotifikasi)
        binding.rvNotification.layoutManager = LinearLayoutManager(context)
        binding.rvNotification.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}