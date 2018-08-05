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

package jp.lionas.androidthings.musicalinstrument.presenter.device

import android.graphics.Color
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat.*

/**
 * Rainbow HAT APA102 RGB LEDs class
 * @author Naoki Seto(@Lionas)
 */
class RainbowLeds {

    private var ledstrip = openLedStrip()
    var rainbow = IntArray(LEDSTRIP_LENGTH)

    init {
        setBrightness(3)
        clear()
    }

    fun close() {
        ledstrip.close()
    }

    fun setBrightness(value: Int) {
        ledstrip.brightness = value
    }

    fun set(index: Int) {
        if ((index < 0) or (index > LEDSTRIP_LENGTH)) { return }
        for (i in 0 until LEDSTRIP_LENGTH) {
            if (i == index) {
                rainbow[LEDSTRIP_LENGTH - 1 - i] =
                        Color.HSVToColor(255, floatArrayOf(i * 360f / LEDSTRIP_LENGTH, 1.0f, 1.0f))
            } else {
                rainbow[LEDSTRIP_LENGTH - 1 - i] = Color.HSVToColor(0, floatArrayOf(0f, 0f, 0f))
            }
        }
        ledstrip.write(rainbow)
    }

    fun clear() {
        for (i in 0 until LEDSTRIP_LENGTH) {
            rainbow[i] = Color.HSVToColor(0, floatArrayOf(0f, 0f, 0f))
            ledstrip.write(rainbow)
        }
    }
}