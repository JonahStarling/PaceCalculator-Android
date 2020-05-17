package com.jonahstarling.pacecalculator

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
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
        if (paceCalculator.distancePresent()) {
            setDistanceResult()
        }
    }

    private fun paceUnitTapped() {
        paceCalculator.switchPaceUnit()
        paceUnit.text = paceCalculator.paceUnit.name
        if (paceCalculator.pacePresent() && paceCalculator.calculatePace()) {
            setPaceResult()
        }
    }

    private fun calculateTapped() {
        getTimeInput()
        getDistanceInput()
        getPaceInput()

        when (paceCalculator.calculateMissing()) {
            PaceCalculator.ValueCalculated.TIME -> setTimeResult()
            PaceCalculator.ValueCalculated.DISTANCE -> setDistanceResult()
            PaceCalculator.ValueCalculated.PACE -> setPaceResult()
            PaceCalculator.ValueCalculated.ERROR -> {
                Toast.makeText(context, "Fill in only 2 of the 3", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getTimeInput() {
        var timeSeconds = 0f
        if (!timeHoursEditText.text.isNullOrEmpty()) {
            timeSeconds += timeHoursEditText.text.toString().toFloat() * secondsInHour
        }
        if (!timeMinutesEditText.text.isNullOrEmpty()) {
            timeSeconds += timeMinutesEditText.text.toString().toFloat() * secondsInMinute
        }
        if (!timeSecondsEditText.text.isNullOrEmpty()) {
            timeSeconds += timeSecondsEditText.text.toString().toFloat()
        }
        if (timeSeconds > 0f) {
            paceCalculator.timeSeconds = timeSeconds
        } else {
            paceCalculator.timeSeconds = null
        }
    }

    private fun setTimeResult() {
        paceCalculator.timeSeconds?.let { timeSeconds ->
            var secondsLeft = timeSeconds

            var hours = (secondsLeft / secondsInHour).toInt().toFloat()
            secondsLeft -= hours * secondsInHour

            var minutes = (secondsLeft / secondsInMinute).toInt().toFloat()
            secondsLeft -= minutes * secondsInMinute

            var secondsString = FormatHelper.seconds(secondsLeft)
            if (secondsString == "60.00") {
                secondsString = "00"
                minutes += 1f
            }
            var minutesString = FormatHelper.minutes(minutes)
            if (minutesString == "60.00") {
                minutesString = "00"
                hours += 1f
            }

            timeHoursEditText.setText(FormatHelper.hours(hours))
            timeMinutesEditText.setText(minutesString)
            timeSecondsEditText.setText(secondsString)
        } ?: clearTimeFields()
    }

    private fun getDistanceInput() {
        var distanceInMiles: Float? = null
        if (paceCalculator.distanceUnit.name == Distances.oneMile.name) {
            distanceInMiles = distanceEditText.text.toString().toFloatOrNull()
        } else if (paceCalculator.distanceUnit.name == Distances.oneKilometer.name) {
            val distanceInput = distanceEditText.text.toString().toFloatOrNull()
            distanceInput?.let {
                distanceInMiles = it * Distances.oneKilometer.lengthInMiles
            }
        }
        paceCalculator.distanceInMiles = distanceInMiles
    }

    private fun setDistanceResult() {
        paceCalculator.distanceInMiles?.let { distanceInMiles ->
            if (paceCalculator.distanceUnit.name == Distances.oneMile.name) {
                distanceEditText.setText(FormatHelper.distance(distanceInMiles))
            } else if (paceCalculator.distanceUnit.name == Distances.oneKilometer.name) {
                distanceEditText.setText(FormatHelper.distance(distanceInMiles / paceCalculator.distanceUnit.lengthInMiles))
            }
        } ?: clearDistanceFields()
    }

    private fun getPaceInput() {
        var paceSecondsPerMile: Float? = null
        var paceSecondsFromInput = 0f
        if (!paceMinutesEditText.text.isNullOrEmpty()) {
            paceSecondsFromInput += paceMinutesEditText.text.toString().toFloat() * secondsInMinute
        }
        if (!paceSecondsEditText.text.isNullOrEmpty()) {
            paceSecondsFromInput += paceSecondsEditText.text.toString().toFloat()
        }
        if (paceSecondsFromInput > 0f) {
            if (paceCalculator.paceUnit.name == Distances.oneMile.name) {
                paceSecondsPerMile = paceSecondsFromInput
            } else if (paceCalculator.paceUnit.name == Distances.oneKilometer.name) {
                paceSecondsPerMile = paceSecondsFromInput / paceCalculator.paceUnit.lengthInMiles
            }
        }
        paceCalculator.paceSecondsPerMile = paceSecondsPerMile
    }

    private fun setPaceResult() {
        paceCalculator.paceSecondsPerMile?.let {
            var secondsLeft = it
            if (paceCalculator.paceUnit.name == Distances.oneKilometer.name) {
                secondsLeft = it * paceCalculator.paceUnit.lengthInMiles
            }
            var minutes = (secondsLeft / secondsInMinute).toInt().toFloat()
            secondsLeft -= minutes * secondsInMinute

            var secondsString = FormatHelper.seconds(secondsLeft)
            if (secondsString == "60.00") {
                secondsString = "00"
                minutes += 1f
            }

            paceSecondsEditText.setText(secondsString)
            paceMinutesEditText.setText(FormatHelper.paceMinutes(minutes))
        } ?: clearPaceFields()
    }

    private fun clearTimeFields() {
        timeHoursEditText.setText("")
        timeMinutesEditText.setText("")
        timeSecondsEditText.setText("")
    }

    private fun clearDistanceFields() {
        distanceEditText.setText("")
    }

    private fun clearPaceFields() {
        paceMinutesEditText.setText("")
        paceSecondsEditText.setText("")
    }

    private fun resetTapped() {
        paceCalculator.clear()

        clearTimeFields()
        clearDistanceFields()
        clearPaceFields()
    }

    companion object {
        val TAG = CalculatorFragment::class.java.simpleName

        private const val secondsInMinute = 60
        private const val secondsInHour = 3600

        fun newInstance() = CalculatorFragment()
    }
}