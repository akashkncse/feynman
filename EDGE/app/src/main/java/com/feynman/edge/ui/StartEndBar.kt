package com.feynman.edge.ui

import android.content.Context
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout

class StartEndBar(
    context: Context,
    private val onStartClicked: () -> Unit,
    private val onEndClicked: () -> Unit
) : LinearLayout(context) {

    private val startButton: Button
    private val endButton: Button

    init {
        orientation = HORIZONTAL
        gravity= Gravity.CENTER
        val density=context.resources.displayMetrics.density
        val paddingPx=(16*density).toInt()
        setPadding(paddingPx,paddingPx*3,paddingPx,paddingPx)
        startButton = Button(context).apply {
            text = "Start Recording"
            setOnClickListener { onStartClicked() }
        }

        endButton = Button(context).apply {
            text = "End Recording"
            setOnClickListener { onEndClicked() }
        }
        val buttonMargin=(12*density).toInt()
        addView(startButton, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(buttonMargin, 0, buttonMargin, 0) })

        addView(endButton, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(buttonMargin, 0, buttonMargin, 0) })
    }

    fun showStartButton(show: Boolean) {
        startButton.visibility = if (show) VISIBLE else GONE
        endButton.visibility = if (show) GONE else VISIBLE
    }
}