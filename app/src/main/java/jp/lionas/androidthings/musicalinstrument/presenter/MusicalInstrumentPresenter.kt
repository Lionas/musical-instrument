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
import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay
import com.google.android.things.contrib.driver.ht16k33.Ht16k33
import com.google.android.things.contrib.driver.pwmspeaker.Speaker
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import com.google.android.things.pio.Gpio
import jp.lionas.androidthings.musicalinstrument.Sound
import jp.lionas.androidthings.sensor.mcp3008.MCP3008Driver
import android.os.Handler

class MusicalInstrumentPresenter {

    companion object {
        const val DELAY_MIL_SECS = 60000L // 1min
    }

    private lateinit var driver: MCP3008Driver
    private lateinit var speaker: Speaker
    private lateinit var segment: AlphanumericDisplay
    private lateinit var sound: Sound

    private var isPressedA: Boolean = false
    private var isPressedB: Boolean = false
    private var isPressedC: Boolean = false

    private lateinit var buttonA: Button
    private lateinit var buttonB: Button
    private lateinit var buttonC: Button

    private lateinit var ledRed: Gpio
    private lateinit var ledGreen: Gpio
    private lateinit var ledBlue: Gpio

    fun initDevices() {
        initDriver()
        initLed()
        initSpeaker()
        initButtons()
        initSegment()
    }

    fun closeDevices() {
        speaker.stop()
        segment.clear()
        driver.unregister()
    }

    private fun initDriver() {
        driver = MCP3008Driver(useSpi = true)
        driver.register()
    }

    private fun initLed() {
        ledRed = RainbowHat.openLedRed()
        ledGreen = RainbowHat.openLedGreen()
        ledBlue = RainbowHat.openLedBlue()
    }

    private fun initSpeaker() {
        speaker = RainbowHat.openPiezo()
        sound = Sound(speaker)
    }

    private fun initButtons() {
        buttonA = RainbowHat.openButtonA()
        buttonA.setOnButtonEventListener { _, pressed ->
            ledRed.value = pressed
            if (!isPressedA and pressed) {
                isPressedA = true
                sound.updateSemitone(true)
                sound.play()
            }
            else if (isPressedA and !pressed) {
                isPressedA = false
                speaker.stop()
            }
        }

        buttonB = RainbowHat.openButtonB()
        buttonB.setOnButtonEventListener { _, pressed ->
            ledGreen.value = pressed
            if (!isPressedB and pressed) {
                isPressedB = true
                sound.updateSemitone(false)
                sound.play()
            }
            else if (isPressedB and !pressed) {
                isPressedB = false
                speaker.stop()
            }
        }

        buttonC = RainbowHat.openButtonC()
        buttonC.setOnButtonEventListener { _, pressed ->
            if (!isPressedC and pressed) {
                ledBlue.value = true
                isPressedC = true
                sound.updateOctave(true)
            }
            else if (isPressedC and pressed) {
                ledBlue.value = false
                isPressedC = false
                sound.updateOctave(false)
            }
        }
    }

    private fun initSegment() {
        segment = RainbowHat.openDisplay()
        segment.setBrightness(Ht16k33.HT16K33_BRIGHTNESS_MAX)
        segment.display(sound.getCurrentKeyString())
        segment.setEnabled(true)
    }

    private val diveIntoLowPowerMode: Runnable = Runnable {
        driver.setLowPowerMode(true)
    }

    fun getCurrentKeyString(): String {
        return sound.getCurrentKeyString()
    }

    fun onDynamicSensorConnected(listener: SensorEventListener,
                                 sensor: Sensor, sensorManager:
                                 SensorManager, handler: Handler) {

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

    fun onDynamicSensorDisconnected(listener: SensorEventListener,
                                    sensorManager: SensorManager,
                                    handler: Handler) {
        handler.removeCallbacks(diveIntoLowPowerMode)
        sensorManager.unregisterListener(listener)
    }

    fun onSensorChanged(event: SensorEvent?): String {
        event?.let {
            val sensorValue = it.values[0].toInt()
            if (isPressedA || isPressedB) {
                sound.playOnChanged(sensorValue)
            }
            val currentKeyStr = sound.updateKey(sensorValue)
            segment.display(currentKeyStr)
            return currentKeyStr
        }

        return ""
    }
}