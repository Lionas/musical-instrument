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

package jp.lionas.androidthings.musicalinstrument.view.device

import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import com.google.android.things.pio.Gpio

/**
 * Rainbow HAT LED for touch button class
 * @author Naoki Seto(@Lionas)
 */
class TouchButtonLeds {

    enum class Color  {
        Red,
        Green,
        Blue
    }

    private var ledRed: Gpio = RainbowHat.openLedRed()
    private var ledGreen: Gpio = RainbowHat.openLedGreen()
    private var ledBlue: Gpio = RainbowHat.openLedBlue()

    fun get(color: Color):Boolean {

        return when(color) {
            Color.Red -> ledRed.value
            Color.Green -> ledGreen.value
            Color.Blue -> ledBlue.value
        }

    }

    fun set(color: Color, value: Boolean) {
        when(color) {
            Color.Red -> ledRed.value = value
            Color.Blue -> ledBlue.value = value
            Color.Green -> ledGreen.value = value
        }
    }

    fun close() {
        ledRed.close()
        ledBlue.close()
        ledGreen.close()
    }

}