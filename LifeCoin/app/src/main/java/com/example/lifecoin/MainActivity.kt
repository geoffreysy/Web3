package com.example.lifecoin

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lifecoin.databinding.ActivityMainBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.vishnusivadas.advanced_httpurlconnection.FetchData

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: ActivityMainBinding
    private var sensorManager : SensorManager? = null
    private var running = false
    private lateinit var viewModel: MainActivityViewModel
    var totalSteps = 0f
    private var previousTotalSteps = 0f
    private lateinit var sharedPreferences0: SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var spEditor : SharedPreferences.Editor
    private var lastPaidSteps = previousTotalSteps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager == null) {
            Log.i("MYTAG", "SENSOR MANAGER IS NULL")
        } else {
            Log.i("MYTAG", "SENSOR MANAGER IS NOT NULL")

        }

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        sharedPreferences0 = getSharedPreferences("lifecoin", Context.MODE_PRIVATE)
        val mobile = sharedPreferences0.getString("mobile", "xxx")

        sharedPreferences = getSharedPreferences(mobile.toString(), Context.MODE_PRIVATE)
        Log.i("MYTAG-SP", mobile.toString())
        spEditor = sharedPreferences.edit()
        viewModel.lifecoin.value = sharedPreferences.getFloat("lifecoin", 0f)
        viewModel.lastPaidSteps.value = sharedPreferences.getFloat("lastPaidSteps", previousTotalSteps)
        viewModel.scans.value = sharedPreferences.getInt("scans", 0)
        viewModel.steps.value = sharedPreferences.getInt("steps", 0)
        Log.i("MYTAG-Lifecoin", viewModel.lifecoin.value.toString())
        Log.i("MYTAG-Lastpaidsteps", viewModel.lastPaidSteps.value.toString())
        Log.i("MYTAG-Scans", viewModel.scans.value.toString())
        Log.i("MYTAG-Steps", viewModel.steps.value.toString())
//        Log.i("MYTAG-Mobile", viewModel.mobile.value.toString())

        lastPaidSteps = viewModel.lastPaidSteps.value!!
        Log.i("MYTAG", "Lifecoin retrieved: ${viewModel.lifecoin.value}")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment (HomeFragment())
//                R.id.steps -> replaceFragment (StepsFragment())
                R.id.redeem -> replaceFragment (RedeemFragment())
                R.id.signin -> {
                    viewModel.scanCode.value = true
                    replaceFragment (HomeFragment())
                }
//                R.id.signin -> scanCode()

                R.id.withdraw -> replaceFragment (WithdrawFragment())
                else -> {true}
            }
        }

        replaceFragment (HomeFragment())
    }

    override fun onResume() {
        super.onResume()
        running = true
        Log.i("MYTAG", "Running: $running")
        val stepSensor:Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
            Log.i("MYTAG", "Have Sensor")
        }
    }

    private fun replaceFragment (fragment: Fragment): Boolean {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
        return true
    }

    override fun onSensorChanged(event: SensorEvent?) {
        Log.i("MYTAG", "OnSensorChanged")
        if (running) {
            totalSteps = event!!.values[0]
            Log.i("MYTAG-TOTAL-STEPS", totalSteps.toString())
            Log.i("MYTAG-PREVIOUS-STEPS", previousTotalSteps.toString())
            if (previousTotalSteps.toFloat()==0f) {
                previousTotalSteps = totalSteps
                lastPaidSteps = totalSteps
            }
            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
            Log.i("MYTAG-CURRENT-STEPS", currentSteps.toString())
//            viewModel.steps.value = currentSteps

            // adding L$
            val payingSteps = totalSteps - lastPaidSteps
            Log.i("MYTAG-PAYING-STEPS", payingSteps.toString())
            Log.i("MYTAG-LASTPAID-STEPS-BF", lastPaidSteps.toString())
            lastPaidSteps = totalSteps
            Log.i("MYTAG-LASTPAID-STEPS-AF", lastPaidSteps.toString())
            viewModel.steps.value = viewModel.steps.value?.plus(payingSteps.toInt())
            viewModel.lifecoin.value = viewModel.lifecoin.value?.plus(payingSteps / 1)
            saveLifeCoin()
            Log.i("MYTAG", "Inside running $currentSteps")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun saveLifeCoin() {
        Log.i("MYTAG", "Saving lifecoin")
        spEditor.putFloat("lifecoin", viewModel.lifecoin.value!!)
        spEditor.putFloat("lastPaidSteps", lastPaidSteps)
        spEditor.putInt("scans", viewModel.scans.value!!)
        spEditor.putInt("steps", viewModel.steps.value!!)
        spEditor.commit()
    }

}