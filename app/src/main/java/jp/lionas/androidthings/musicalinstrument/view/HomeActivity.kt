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

package jp.lionas.androidthings.musicalinstrument.view

import android.app.Activity
import android.content.Context
import android.databinding.DataBindingUtil
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import jp.lionas.androidthings.musicalinstrument.R
import jp.lionas.androidthings.musicalinstrument.databinding.ActivityHomeBinding
import jp.lionas.androidthings.musicalinstrument.model.PitchDataModel
import jp.lionas.androidthings.musicalinstrument.presenter.MusicalInstrumentPresenter

/**
 * Musical instrument for Android Things with Rainbow HAT
 * @author Naoki Seto(@Lionas)
 */
class HomeActivity : Activity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var binding: ActivityHomeBinding
    private lateinit var presenter: MusicalInstrumentPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        presenter = MusicalInstrumentPresenter(sensorManager, this@HomeActivity)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        presenter.openDevices()
        binding.data = PitchDataModel(presenter.getCurrentKeyString())
    }

    override fun onDestroy() {
        presenter.closeDevices()
        super.onDestroy()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        binding.data = PitchDataModel(presenter.onSensorChanged(event))
    }

}
