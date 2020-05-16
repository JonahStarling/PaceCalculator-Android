package com.jonahstarling.pacecalculator

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_launch.*


class LaunchFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_launch, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.viewTreeObserver.addOnGlobalLayoutListener(object:
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                animateLaunchScreen(view)
            }
        })
    }

    private fun animateLaunchScreen(view: View) {
        val location = IntArray(2)
        jonahText.getLocationOnScreen(location)
        val originalJonahX: Int = location[0]
        paceText.getLocationOnScreen(location)
        val originalPaceX: Int = location[0]
        calculatorText.getLocationOnScreen(location)
        val originalCalculatorX: Int = location[0]

        val logoAnimator = ValueAnimator.ofFloat(1.0f, 0.0f)
        logoAnimator.duration = 500L
        logoAnimator.startDelay = 500L
        logoAnimator.interpolator = AccelerateInterpolator()
        logoAnimator.addUpdateListener {
            jonahText.alpha = it.animatedValue as Float
            paceText.alpha = it.animatedValue as Float
            calculatorText.alpha = it.animatedValue as Float

            jonahText.x = originalJonahX - ((originalJonahX - (0 - jonahText.width/2)) * it.animatedFraction)
            paceText.x = originalPaceX + (((view.width + paceText.width/2) - originalPaceX) * it.animatedFraction)
            calculatorText.x = originalCalculatorX - ((originalCalculatorX - (0 - calculatorText.width/2)) * it.animatedFraction)
        }
        logoAnimator.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                navigateToCalculatorFragment()
            }
        })
        logoAnimator.start()
    }

    private fun navigateToCalculatorFragment() {
        (activity as MainActivity).replaceFragment(CalculatorFragment.newInstance(), CalculatorFragment.TAG)
    }

    companion object {
        val TAG = LaunchFragment::class.java.simpleName

        fun newInstance() = LaunchFragment()
    }
}