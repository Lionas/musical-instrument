/*
 * Copyright 2018 Naoki Seto(@Lionas)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.lionas.androidthings.musicalinstrument

import android.app.Activity
import android.content.Context
import android.databinding.DataBindingUtil
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.DynamicSensorCallback
import android.os.Bundle
import android.os.Handler
import jp.lionas.androidthings.musicalinstrument.databinding.ActivityHomeBinding
import jp.lionas.androidthings.musicalinstrument.presenter.MusicalInstrumentPresenter

/**
 * Analog to Digital Converter MusicalInstrumentPresenter Sample App
 * Musical instrument using Rainbow HAT
 * @author Naoki Seto(@Lionas)
 */
class HomeActivity : Activity(), SensorEventListener {

    private val callback = SensorCallback()
    private lateinit var sensorManager: SensorManager
    private lateinit var binding: ActivityHomeBinding

    private val handler = Handler()
    private val musicalInstrumentDriver = MusicalInstrumentPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerDynamicSensorCallback(callback)

        musicalInstrumentDriver.initDevices()
        binding.data = SensorData(musicalInstrumentDriver.getCurrentKeyString())
    }

    override fun onDestroy() {
        musicalInstrumentDriver.closeDevices()
        sensorManager.unregisterDynamicSensorCallback(callback)
        super.onDestroy()
    }

    private inner class SensorCallback : DynamicSensorCallback() {

        override fun onDynamicSensorConnected(sensor: Sensor) {
            musicalInstrumentDriver.onDynamicSensorConnected(this@HomeActivity, sensor, sensorManager, handler)
        }

        override fun onDynamicSensorDisconnected(sensor: Sensor?) {
            musicalInstrumentDriver.onDynamicSensorDisconnected(this@HomeActivity, sensorManager, handler)
            super.onDynamicSensorDisconnected(sensor)
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        binding.data = SensorData(musicalInstrumentDriver.onSensorChanged(event))
    }

}
