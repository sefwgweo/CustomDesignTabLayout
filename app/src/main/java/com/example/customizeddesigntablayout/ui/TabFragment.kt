package com.example.customizeddesigntablayout.ui

import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.customizeddesigntablayout.R
import com.example.customizeddesigntablayout.model.TabColorCalculator
import com.example.customizeddesigntablayout.model.TabDto
import com.example.customizeddesigntablayout.ui.adapter.TabPagerAdapter
import com.example.customizeddesigntablayout.databinding.FragmentTabBinding
import com.google.android.material.tabs.TabLayoutMediator

class TabFragment : Fragment(R.layout.fragment_tab) {
    companion object {
        fun newInstance() = TabFragment()
    }

    private lateinit var binding: FragmentTabBinding
    private lateinit var tabList: List<TabDto>
    private val viewModel: TabViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTabBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        initUIComponent()
    }

    private fun initUIComponent() {
        tabList = viewModel.fetchTab()
        val pagerAdapter = TabPagerAdapter(this@TabFragment, tabList)
        binding.pager.apply {
            adapter = pagerAdapter
            offscreenPageLimit = pagerAdapter.itemCount
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    // ???????????????????????????
                    setSellingTabBackgroundColor(positionOffset, position)
                }
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    // ?????????????????????????????????
                    setIdleTabColors(position)
                }
            })
        }
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = tabList[position].name
        }.attach()
    }

    private fun setSellingTabBackgroundColor(positionOffset: Float, currentPosition: Int) {
        val baseBgColor = TabColorCalculator.parseColor(tabList[currentPosition].bgColor)
        var nextTabBackgroundColor = baseBgColor
        if (positionOffset > 0f && currentPosition < tabList.size - 1) {
            nextTabBackgroundColor = TabColorCalculator.parseColor(tabList[currentPosition + 1].bgColor)
        }
        val bgColor = ColorUtils.blendARGB(nextTabBackgroundColor, baseBgColor, 1f - positionOffset)
        val tabColors = TabColorCalculator(bgColor).getTabColors()

        // ????????????????????????(1??????)
        requireActivity().window.apply {
            statusBarColor = bgColor
            // ?????????????????????????????????????????????????????????????????????
            WindowInsetsControllerCompat(this, decorView).isAppearanceLightStatusBars =
                tabColors.systemUiFlagLightStatusBar
        }
        // toolBar???(2??????)
        binding.toolbar.setBackgroundColor(bgColor)
        // ???????????????(3??????)
        val overlayColor = ContextCompat.getColor(requireContext(), TabColorCalculator(baseBgColor).getTabColors().overlay)
        val overlayColorAddAlpha = Color.argb(tabColors.alpha, Color.red(overlayColor), Color.green(overlayColor), Color.blue(overlayColor))
        val bgOverlayColor = ColorDrawable(bgColor).apply {
            colorFilter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                BlendModeColorFilter(overlayColorAddAlpha, BlendMode.SRC_OVER)
            } else {
                PorterDuffColorFilter(overlayColorAddAlpha, PorterDuff.Mode.SRC_OVER)
            }
        }
        binding.tabLayout.background = bgOverlayColor
    }

    private fun setIdleTabColors(position: Int) {
        val tabColors = TabColorCalculator(TabColorCalculator.parseColor(tabList[position].bgColor)).getTabColors()

        // ??????????????????????????????
        binding.tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(), tabColors.selectedText))

        // ?????????????????????
        binding.tabLayout.setTabTextColors(
            ContextCompat.getColor(requireContext(), tabColors.unSelectedText),
            ContextCompat.getColor(requireContext(), tabColors.selectedText)
        )

        // Menu????????????
        binding.menuButtonIcon.imageTintList = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf( android.R.attr.state_pressed)),
            intArrayOf(
                ContextCompat.getColor(requireContext(), tabColors.icon),
                ContextCompat.getColor(requireContext(), tabColors.icon)
            )
        )
    }
}
