package pl.lipov.laborki.presentation

import android.view.MotionEvent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.lipov.laborki.common.utils.GestureDetectorUtils
import pl.lipov.laborki.common.utils.SensorEventsUtils
import pl.lipov.laborki.data.LoginRepository
import pl.lipov.laborki.data.model.Event

class MainViewModel(
    private val gestureDetectorUtils: GestureDetectorUtils,
    private val sensorEventsUtils: SensorEventsUtils,
    private val loginRepository: LoginRepository
) : ViewModel() {

    val onAccelerometerNotDetected: MutableLiveData<Unit> =
        sensorEventsUtils.onAccelerometerNotDetected
    val onGestureEvent: MutableLiveData<Event> = gestureDetectorUtils.onEvent
    val onSensorEvent: MutableLiveData<Event> = sensorEventsUtils.onEvent

    fun initializeGestureDetector(activity: FragmentActivity){
        gestureDetectorUtils.initializeGestureDetector(activity)
    }

    fun initializeSensorEvents(activity: FragmentActivity){
        sensorEventsUtils.initializeSensorEvents(activity)
    }

    fun onTouchEvent(event:MotionEvent):Boolean {
        gestureDetectorUtils.onTouchEvent(event)
        return true
    }

    fun registerSensorManager(){
        sensorEventsUtils.registerSensorManager()
    }

    fun unregisterSensorManager(){
        sensorEventsUtils.unregisterSensorManager()
    }

    fun unregisterGestureDetector(){
        gestureDetectorUtils.unregisterGestureDetector()
    }
}
