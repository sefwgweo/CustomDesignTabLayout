package com.example.customizeddesigntablayout.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.customizeddesigntablayout.model.TabDto
import com.example.customizeddesigntablayout.ui.ColorfulItemFragment
import com.example.customizeddesigntablayout.ui.ColorfulItemFragment.Companion.KEY_CONTENTS
import java.lang.ref.WeakReference

class TabPagerAdapter(
    fragment: Fragment,
    private val tabList: List<TabDto>,
) : FragmentStateAdapter(fragment) {
    private val fragments: MutableList<WeakReference<Fragment>> = mutableListOf()

    override fun getItemCount(): Int = tabList.size

    override fun createFragment(position: Int): Fragment {
        val fragment = ColorfulItemFragment.newInstance().apply {
            arguments = Bundle().apply {
                putString(KEY_CONTENTS, tabList[position].contentsStr)
            }
        }
        fragments.add(WeakReference(fragment))
        return fragment
    }
}