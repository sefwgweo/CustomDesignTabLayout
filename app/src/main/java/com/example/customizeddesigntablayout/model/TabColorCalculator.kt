package com.example.customizeddesigntablayout.model

import android.graphics.Color
import androidx.annotation.ColorRes
import com.example.customizeddesigntablayout.R

class TabColorCalculator(private val backgroundColor: Int) {
    companion object {
        fun parseColor(colorString: String?): Int {
            runCatching {
                Color.parseColor(colorString)
            }.fold(
                onSuccess = { return it },
                onFailure = { throw it }
            )
        }
    }

    // 輝度とbackgroundColorを元にタブで使う色を求める(選択中テキスト色、非選択テキスト色、アイコン色、背景色 )
    fun getTabColors(): TabColors {
        return when {
            getLightness() in 0.0..0.29 -> {
                TabColors(
                    alpha = 33,
                    overlay = R.color.white,
                    icon = R.color.white,
                    selectedText = R.color.white,
                    unSelectedText = R.color.Grey_2,
                    systemUiFlagLightStatusBar = false
                )
            }
            getLightness() < 0.6 -> {
                if (blackTextIsBetter()) {
                    TabColors(
                        alpha = 89,
                        overlay = R.color.black,
                        icon = R.color.Black_2,
                        selectedText = R.color.white,
                        unSelectedText = R.color.Grey_2,
                        systemUiFlagLightStatusBar = true
                    )
                } else {
                    TabColors(
                        alpha = 89,
                        overlay = R.color.black,
                        icon = R.color.white,
                        selectedText = R.color.white,
                        unSelectedText = R.color.Grey_2,
                        systemUiFlagLightStatusBar = false
                    )
                }
            }
            getLightness() < 0.8 -> {
                TabColors(
                    alpha = 89,
                    overlay = R.color.black,
                    icon = R.color.Black_2,
                    selectedText = R.color.white,
                    unSelectedText = R.color.Grey_2,
                    systemUiFlagLightStatusBar = true
                )
            }
            getLightness() < 0.9 -> {
                TabColors(
                    alpha = 89,
                    overlay = R.color.white,
                    icon = R.color.Black_2,
                    selectedText = R.color.Black_2,
                    unSelectedText = R.color.Black_3,
                    systemUiFlagLightStatusBar = true
                )
            }
            else -> {
                TabColors(
                    alpha = 12,
                    overlay = R.color.black,
                    icon = R.color.Black_2,
                    selectedText = R.color.Black_2,
                    unSelectedText = R.color.Black_3,
                    systemUiFlagLightStatusBar = true
                )
            }
        }
    }

    /**
     * colorを元に輝度を求める
     *
     * @return 輝度 Double
     */
    private fun getLightness(): Double {
        val colors = mutableListOf(
            Color.red(backgroundColor),
            Color.green(backgroundColor),
            Color.blue(backgroundColor)
        )
        val max = colors.maxOrNull() ?: 0
        val min = colors.minOrNull() ?: 0
        return (max + min).toDouble() / (2 * 255).toDouble()
    }

    /**
     * 背景色によって、白文字列 or 黒文字列が良いかを判断する.<br></br>
     * 以下のW3Cのロジックを利用。
     * https://www.w3.org/WAI/ER/WD-AERT/#color-contrast
     *
     * @return 黒を指定した方が良い場合にtrue
     */
    private fun blackTextIsBetter(): Boolean {
        val red = Color.red(backgroundColor)
        val green = Color.green(backgroundColor)
        val blue = Color.blue(backgroundColor)
        val delta = (red * 299 + green * 587 + blue * 114) / 1000
        return delta >= 125
    }

    data class TabColors(
        /**
         * 重ねる画像のアルファ値(0〜255)
         */
        val alpha: Int,
        /**
         * 重ねる色
         */
        @ColorRes val overlay: Int,
        /**
         * アイコン色
         */
        @ColorRes val icon: Int,
        /**
         * タブ選択時テキスト色
         */
        @ColorRes val selectedText: Int,
        /**
         * タブ非選択時テキスト色
         */
        @ColorRes val unSelectedText: Int,
        /**
         * ステータスバーのアイコンや文字の色をLight用にするかどうかの判定(iconの色が黒ならtrue)
         */
        val systemUiFlagLightStatusBar: Boolean
    )
}