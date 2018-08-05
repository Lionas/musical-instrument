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

package jp.lionas.androidthings.musicalinstrument.presenter

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import jp.lionas.androidthings.sensor.mcp3008.MCP3008Driver
import android.os.Handler
import jp.lionas.androidthings.musicalinstrument.presenter.device.*
import jp.lionas.androidthings.musicalinstrument.presenter.device.TouchButtonLeds.*

/**
 * Musical Instrument Presenter class
 * @author Naoki Seto(@Lionas)
 */
class MusicalInstrumentPresenter(val sensorManager: SensorManager,
                                 val listener: SensorEventListener) {

    companion object {
        const val DELAY_MIL_SECS = 60000L // 1min
    }

    private val callback = SensorCallback()
    private val handler = Handler()

    private lateinit var adcDriver: AdcDriver
    private lateinit var touchButtonLeds: TouchButtonLeds
    private lateinit var speaker: Speaker
    private lateinit var buttons: Buttons
    private lateinit var segment: Segment
    private lateinit var rainbowLeds: RainbowLeds

    private inner class SensorCallback : SensorManager.DynamicSensorCallback() {
        override fun onDynamicSensorConnected(sensor: Sensor) {
            onDynamicSensorConnected(listener, sensor, sensorManager, handler)
        }

        override fun onDynamicSensorDisconnected(sensor: Sensor?) {
            onDynamicSensorDisconnected(listener, sensorManager, handler)
            super.onDynamicSensorDisconnected(sensor)
        }
    }

    fun openDevices() {
        sensorManager.registerDynamicSensorCallback(callback)
        adcDriver = AdcDriver()
        adcDriver.register()
        touchButtonLeds = TouchButtonLeds()
        speaker = Speaker()
        buttons = Buttons()
        segment = Segment()
        segment.display(speaker.getCurrentKeyString())
        setOnTouchListenerForButtons()
        rainbowLeds = RainbowLeds()
    }

    private fun setOnTouchListenerForButtons() {
        buttons.setOnButtonAEventListener { _, pressed ->
            touchButtonLeds.set(Color.Red, pressed)
            if (!buttons.isPressedA() and pressed) {
                buttons.setPressedA(true)
                speaker.updateSemitone(true)
                speaker.play()
            }
            else if (buttons.isPressedA() and !pressed) {
                buttons.setPressedA(false)
                speaker.stop()
                rainbowLeds.clear()
            }
        }

        buttons.setOnButtonBEventListener { _, pressed ->
            touchButtonLeds.set(Color.Green, pressed)
            if (!buttons.isPressedB() and pressed) {
                buttons.setPressedB(true)
                speaker.updateSemitone(false)
                speaker.play()
            }
            else if (buttons.isPressedB() and !pressed) {
                buttons.setPressedB(false)
                speaker.stop()
                rainbowLeds.clear()
            }
        }

        buttons.setOnButtonCEventListener { _, pressed ->
            if (!buttons.isPressedC() and pressed) {
                touchButtonLeds.set(Color.Blue, true)
                buttons.setPressedC(true)
                speaker.updateOctave(true)
            }
            else if (buttons.isPressedC() and pressed) {
                touchButtonLeds.set(Color.Blue, false)
                buttons.setPressedC(false)
                speaker.updateOctave(false)
                rainbowLeds.clear()
            }
        }
    }

    fun closeDevices() {
        speaker.stop()
        speaker.close()
        segment.clear()
        buttons.closeButtons()
        rainbowLeds.close()
        adcDriver.unregister()
        sensorManager.unregisterDynamicSensorCallback(callback)
    }

    private val diveIntoLowPowerMode: Runnable = Runnable {
        adcDriver.setLowPowerMode(true)
    }

    fun getCurrentKeyString(): String {
        return speaker.getCurrentKeyString()
    }

    fun onSensorChanged(event: SensorEvent?): String {
        event?.let {
            val sensorValue = it.values[0].toInt()
            if (buttons.isPressedA() || buttons.isPressedB()) {
                rainbowLeds.set(speaker.getCurrentKeyIndex())
                speaker.playOnChanged(sensorValue)
            }
            val currentKeyStr = speaker.updateKey(sensorValue)
            val currentOctave = if (speaker.isCurrentOctave()) { "+1" } else { "+0" }
            segment.display("%-2s%2s".format(currentKeyStr, currentOctave))
            return currentKeyStr
        }
        return ""
    }

    private fun onDynamicSensorConnected(listener: SensorEventListener,
                                         sensor: Sensor,
                                         sensorManager: SensorManager,
                                         handler: Handler) {

        if (sensor.type == Sensor.TYPE_DEVICE_PRIVATE_BASE &&
                sensor.name!!.contentEquals(MCP3008Driver.DRIVER_NAME)) {

            sensorManager.registerListener(
                    listener,
                    sensor,
                    SensorManager.SENSOR_DELAY_NORMAL
            )

            // the mode is shifted to the low power mode
            handler.postDelayed(diveIntoLowPowerMode, DELAY_MIL_SECS)
        }
    }

    private fun onDynamicSensorDisconnected(listener: SensorEventListener,
                                    sensorManager: SensorManager,
                                    handler: Handler) {
        handler.removeCallbacks(diveIntoLowPowerMode)
        sensorManager.unregisterListener(listener)
    }

}
