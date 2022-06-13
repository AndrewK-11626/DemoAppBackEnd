package com.example.demoapp.autentikasi.splashscreen

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.demoapp.R
import com.example.demoapp.databinding.FragmentSplashBinding
import com.example.demoapp.utils.injector

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding

    private val viewModel: SplashViewModel by viewModels {
        injector().splashFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun observeViewModel() {
        viewModel.isLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            val destination = if (isLoggedIn) R.id.nav_splash_to_home
            else R.id.nav_splash_to_auth

            findNavController().navigate(destination)
        }
    }
}