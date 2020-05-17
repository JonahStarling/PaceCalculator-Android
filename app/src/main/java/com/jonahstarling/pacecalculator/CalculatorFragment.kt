package com.jonahstarling.pacecalculator

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.calculator_fields.*
import kotlinx.android.synthetic.main.distance_input.*
import kotlinx.android.synthetic.main.footer_buttons.*
import kotlinx.android.synthetic.main.fragment_calculator.*
import kotlinx.android.synthetic.main.pace_input.*
import kotlinx.android.synthetic.main.time_input.*

class CalculatorFragment: Fragment() {

    private val paceCalculator = PaceCalculator()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_calculator, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        distanceUnit.setOnClickListener { distanceUnitTapped() }
        paceUnit.setOnClickListener { paceUnitTapped() }
        calculateButton.setOnClickListener { calculateTapped() }
        resetButton.setOnClickListener { resetTapped() }

        view.viewTreeObserver.addOnGlobalLayoutListener(object:
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                animateViewsOnScreen(view)
            }
        })
    }

    private fun animateViewsOnScreen(view: View) {
        // Logo Header
        val logoStartX = (0 - logoHeader.width).toFloat()
        logoHeader.x = logoStartX

        // Calculator Fields
        val timeStartX = view.width.toFloat()
        timeInputLayout.x = timeStartX
        val distanceStartX = (0 - distanceInputLayout.width).toFloat()
        distanceInputLayout.x = distanceStartX
        val paceStartX = view.width.toFloat()
        paceInputLayout.x = paceStartX

        // Footer Buttons
        val calculateStartX = (0 - calculateButton.width).toFloat()
        calculateButton.x = calculateStartX
        val resetStartX = view.width.toFloat()
        resetButton.x = resetStartX

        val calculatorAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        calculatorAnimator.duration = 500L
        calculatorAnimator.interpolator = DecelerateInterpolator()
        calculatorAnimator.addUpdateListener {
            // Logo Header
            logoHeader.x = logoStartX + ((0 - logoStartX) * it.animatedValue as Float)

            // Calculator Fields
            timeInputLayout.x = timeStartX - (timeStartX * it.animatedValue as Float)
            distanceInputLayout.x = distanceStartX + ((0 - distanceStartX) * it.animatedValue as Float)
            paceInputLayout.x = paceStartX - (paceStartX * it.animatedValue as Float)

            // Footer Buttons
            calculateButton.x = calculateStartX + ((0 - calculateStartX) * it.animatedValue as Float)
            resetButton.x = resetStartX - ((resetStartX - (view.width - resetButton.width)) * it.animatedValue as Float)
        }
        calculatorAnimator.start()
    }

    private fun distanceUnitTapped() {
        paceCalculator.switchDistanceUnit()
        distanceUnit.text = paceCalculator.distanceUnit.name
    }

    private fun paceUnitTapped() {
        paceCalculator.switchPaceUnit()
        paceUnit.text = paceCalculator.paceUnit.name
    }

    private fun calculateTapped() {
        // TODO
    }

    private fun resetTapped() {
        paceCalculator.clear()

        timeHoursEditText.setText("")
        timeMinutesEditText.setText("")
        timeSecondsEditText.setText("")

        distanceEditText.setText("")

        paceMinutesEditText.setText("")
        paceSecondsEditText.setText("")
    }

    companion object {
        val TAG = CalculatorFragment::class.java.simpleName

        fun newInstance() = CalculatorFragment()
    }
}