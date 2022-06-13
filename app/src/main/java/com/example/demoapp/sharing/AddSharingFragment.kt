package com.example.demoapp.sharing

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.demoapp.R

class AddSharingFragment : Fragment() {

    companion object {
        fun newInstance() = AddSharingFragment()
    }

    private lateinit var viewModel: AddSharingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_sharing, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddSharingViewModel::class.java)
        // TODO: Use the ViewModel
    }

}