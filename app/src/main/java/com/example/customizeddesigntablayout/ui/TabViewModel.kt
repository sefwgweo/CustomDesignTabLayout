package com.example.customizeddesigntablayout.ui

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.customizeddesigntablayout.model.TabDto

class TabViewModel : ViewModel() {
    private val tabSize = ObservableField(0)
    private val FixedModeTabSizeLimit = 3

    fun fetchTab(): List<TabDto> {
        val list =  listOf(
            TabDto(
                "Red",
                "#FF0000",
                "タブ１"
            ),
            TabDto(
                "White",
                "#FFFFFF",
                "タブ２"
            ),
            TabDto(
                "Black",
                "#FF000000",
                "タブ３"
            ),
        )
        tabSize.set(list.size)
        return list
    }

    fun needScrollable(): Boolean {
        val count = tabSize.get() ?: 0
        return count > FixedModeTabSizeLimit
    }
}