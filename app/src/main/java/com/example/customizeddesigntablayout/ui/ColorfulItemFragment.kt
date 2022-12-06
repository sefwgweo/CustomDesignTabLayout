package com.example.customizeddesigntablayout.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.customizeddesigntablayout.R
import com.example.customizeddesigntablayout.databinding.FragmentColorfulBinding

class ColorfulItemFragment : Fragment(R.layout.fragment_colorful) {
    companion object {
        fun newInstance() = ColorfulItemFragment()
        const val KEY_CONTENTS = "Contents"
    }

    private lateinit var binding: FragmentColorfulBinding

    private val contentsStr by lazy {
        arguments?.getString(KEY_CONTENTS)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentColorfulBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        initUIComponent()
    }

    private fun initUIComponent() {
        binding.textColorful.text = contentsStr
    }
}