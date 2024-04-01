package com.example.rithviknakirikanti_boggleapp
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class ScoreResetFragment : Fragment(), SensorEventListener {

    private lateinit var scoreTextView: TextView
    private var listener: OnNewGameRequestedListener? = null
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private var lastUpdate: Long = 0
    private var last_x: Float = 0.0f
    private var last_y: Float = 0.0f
    private var last_z: Float = 0.0f
    private val shakeThreshold: Int = 600

    interface OnNewGameRequestedListener {
        fun onNewGameRequested()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_score_reset_fragment, container, false)

        scoreTextView = view.findViewById(R.id.scoreTextView)

        view.findViewById<Button>(R.id.newGameButton).setOnClickListener {
            listener?.onNewGameRequested()
        }

        //Initialize the sensor manager
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

        return view
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        val curTime = System.currentTimeMillis()
        if ((curTime - lastUpdate) > 100) {
            val diffTime = (curTime - lastUpdate)
            lastUpdate = curTime

            val x = event?.values?.get(0) ?: 0f
            val y = event?.values?.get(1) ?: 0f
            val z = event?.values?.get(2) ?: 0f

            val speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000

            if (speed > shakeThreshold) {
                listener?.onNewGameRequested()
            }


            last_x = x
            last_y = y
            last_z = z
        }
    }

    fun updateScore(score: Int) {
        scoreTextView.text = "Score: $score"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNewGameRequestedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnNewGameRequestedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}


