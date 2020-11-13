package com.notilocations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI


class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding =
            DataBindingUtil.inflate<com.notilocations.databinding.FragmentSettingsBinding>(
                inflater,
                R.layout.fragment_settings,
                container,
                false
            )
        return binding.root

    }
}