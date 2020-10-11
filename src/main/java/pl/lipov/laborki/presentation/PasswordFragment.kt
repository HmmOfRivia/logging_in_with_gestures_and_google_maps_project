package pl.lipov.laborki.presentation

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_password.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.lipov.laborki.R
import pl.lipov.laborki.presentation.Map.MapsActivity


class PasswordFragment : Fragment() {

    private val viewModel: MainViewModel by viewModel()
    private var enteredPin = ArrayList<String>()
    private var passPin = listOf<String>()
    private var starAnimator: ValueAnimator? = null
    private var steps = 0
    private var unlockTries = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getStringArray("pin")?.let {
            passPin = it.toList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_password, container, false)

        inflate.setOnTouchListener { _, event ->
            viewModel.onTouchEvent(event)
            true
        }
        return inflate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let { viewModel.initializeGestureDetector(it) }
        activity?.let { viewModel.initializeSensorEvents(it) }
        val stars = arrayListOf(ic_star11, ic_star22, ic_star33, ic_star44)
        viewModel.run {
            onAccelerometerNotDetected.observe(::getLifecycle) {
                //info_text.text = getString(R.string.no_accelerometer_detected)
            }
            onGestureEvent.observe(::getLifecycle) {
                enteredPin.add(it.name)
                steps+=1
                steps = checkPIN(steps, stars)
            }
            onSensorEvent.observe(::getLifecycle) {
                enteredPin.add(it.name)
                steps+=1
                steps = checkPIN(steps, stars)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(userPin: Array<String>?) = PasswordFragment().apply {
            arguments = Bundle().apply {
                putStringArray("pin", userPin)
            }
        }
    }

    fun checkPIN(steps: Int, stars: ArrayList<View>): Int {
        starAnimator = ic_star22.getBackgroundAnimator(1500,R.color.army, R.color.pink, steps-1, stars)
        starAnimator?.start()
        var steps2 = steps
        if (steps == 4) {
            unlockTries+=1
            if (enteredPin == passPin) {
                steps2 = 0
                enteredPin.removeAll(enteredPin)
                lockImage.setImageResource(R.drawable.ic_lock_open_black_24dp)
                blockInterface(false)
                Handler().postDelayed({ViewRouter().fragmentChange(MapsActivity(), activity, R.id.fragment_box)},2000)
            } else {
                steps2 = 0
                enteredPin.removeAll(enteredPin)

                blockInterface(true)
                if(unlockTries!=3){
                    Handler().postDelayed({
                        for (star in stars) {
                            showHide(star)
                            activity?.let { viewModel.initializeGestureDetector(it) }
                            viewModel.registerSensorManager()
                            lockImage.setColorFilter(lockImage.context.resources.getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP)
                        }
                    },2000)
                }
            }
        }
        return steps2
    }

    private fun blockInterface(redLock: Boolean){
        viewModel.unregisterSensorManager()
        viewModel.unregisterGestureDetector()
        if(redLock){
            lockImage.setColorFilter(lockImage.context.resources.getColor(R.color.error), PorterDuff.Mode.SRC_ATOP)
        }
    }

    override fun onResume() {
        viewModel.registerSensorManager()
        super.onResume()
    }

    override fun onPause() {
        viewModel.unregisterSensorManager()
        super.onPause()
    }

    fun showHide(view:View) {
        view.visibility = if (view.visibility == View.VISIBLE){
            View.INVISIBLE
        } else{
            View.VISIBLE
        }
    }

    private fun View.getBackgroundAnimator(
        duration: Long = 1500,
        @ColorRes firstColorResId: Int = R.color.army,
        @ColorRes secondColorResId: Int = R.color.error,
        steps: Int = 0,
        stars: ArrayList<View>
    ): ValueAnimator {
        val starsB = arrayListOf<Drawable>(ic_star11.background, ic_star22.background, ic_star33.background, ic_star44.background)
        showHide(stars[steps])
        return ValueAnimator.ofArgb(
            ContextCompat.getColor(context, firstColorResId),
            ContextCompat.getColor(context, secondColorResId)
        ).apply {
            addUpdateListener {
                DrawableCompat.setTint(starsB.get(steps), it.animatedValue as Int)
            }
            this.duration = duration
        }
    }
}
