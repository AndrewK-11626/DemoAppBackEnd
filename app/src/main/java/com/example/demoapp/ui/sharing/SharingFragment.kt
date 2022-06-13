package com.example.demoapp.ui.sharing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.demoapp.R
import com.example.demoapp.databinding.FragmentSharingBinding


class SharingFragment : Fragment() {
    private var _binding: FragmentSharingBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionListener()
    }

    private fun actionListener(){
        val view = binding ?: return
        view.fabAdd.setOnClickListener { findNavController().navigate(R.id.nav_list_to_add_sharing) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSharingBinding.inflate(inflater, container, false)

        return binding?.root

    }

    companion object {
    }
}