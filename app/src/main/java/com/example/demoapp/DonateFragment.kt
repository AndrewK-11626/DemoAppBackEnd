package com.example.demoapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.demoapp.databinding.FragmentDonateBinding


class DonateFragment : Fragment() {
    private var _binding: FragmentDonateBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val donateViewModel =
            ViewModelProvider(this).get(DonateViewModel::class.java)

        _binding = FragmentDonateBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.button2.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send/?phone=6285213710942&text&app_absent=0")))
        }
        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}