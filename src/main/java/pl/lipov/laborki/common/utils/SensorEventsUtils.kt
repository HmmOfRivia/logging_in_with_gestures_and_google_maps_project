package pl.lipov.laborki.common.utils

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Context.DEVICE_POLICY_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import pl.lipov.laborki.data.model.Event
import pl.lipov.laborki.presentation.MainActivity

class SensorEventsUtils(
    private var sensorManager: SensorManager,
    private var accelerometer: Sensor?
) : SensorEventListener {

    companion object {
        private const val TAG = "sensor_events_utils"
    }


    val onEvent = MutableLiveData<Event>()
    val onAccelerometerNotDetected = MutableLiveData<Unit>()
    val gravity = doubleArrayOf(0.0,0.0,0.0)
    val linear_acceleration = doubleArrayOf(0.0,0.0,0.0)
    var blocker:Boolean = false

    override fun onSensorChanged(
        sensorEvent: SensorEvent
    ) {
        val alpha: Float = 0.8f
        gravity[0] = alpha * gravity[0] + (1 - alpha) * sensorEvent.values[0]
        gravity[1] = alpha * gravity[1] + (1 - alpha) * sensorEvent.values[1]
        gravity[2] = alpha * gravity[2] + (1 - alpha) * sensorEvent.values[2]

        linear_acceleration[0] = sensorEvent.values[0] - gravity[0]
        linear_acceleration[1] = sensorEvent.values[1] - gravity[1]
        linear_acceleration[2] = sensorEvent.values[2] - gravity[2]
        if(blocker==false){
            if(linear_acceleration[0]>5 || linear_acceleration[1]>5){
                onEvent.postValue(Event.ACCELERATION_CHANGE)
                blocker = true
                Handler().postDelayed({
                    blocker = false
                }, 1500)
            }
        }
    }

    fun initializeSensorEvents(context: Context){
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if(accelerometer == null){
            onAccelerometerNotDetected.postValue(Unit)
        }
    }

    fun registerSensorManager(){
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unregisterSensorManager(){
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(
        sensor: Sensor,
        accuracy: Int
    ) {
        Log.d(TAG, "${sensor.name} accuracy changed to $accuracy.")
    }

}
