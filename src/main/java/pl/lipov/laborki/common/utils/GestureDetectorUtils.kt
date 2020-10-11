package pl.lipov.laborki.common.utils

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import pl.lipov.laborki.data.model.Event

class GestureDetectorUtils : GestureDetector.SimpleOnGestureListener() {

    val onEvent = MutableLiveData<Event>()
    private var gestureDetector: GestureDetector? = null

    override fun onDoubleTap(event: MotionEvent): Boolean {
        onEvent.postValue(Event.DOUBLE_TAP)
        super.onDoubleTap(event)
        return true
    }

    override fun onLongPress(event: MotionEvent) {
        onEvent.postValue(Event.LONG_PRESS)
        super.onLongPress(event)
    }

    fun initializeGestureDetector(activity: FragmentActivity) {
        gestureDetector = GestureDetector(activity, this)
    }

    fun unregisterGestureDetector() {
        gestureDetector = null
    }

    fun onTouchEvent(event: MotionEvent){
        gestureDetector?.onTouchEvent(event)
    }
}