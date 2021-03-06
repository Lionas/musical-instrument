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

import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay
import com.google.android.things.contrib.driver.ht16k33.Ht16k33
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat

/**
 * Rainbow HAT AlphanumericDisplay class
 * @author Naoki Seto(@Lionas)
 */
class Segment {

    private var segment: AlphanumericDisplay = RainbowHat.openDisplay()

    init {
        segment.setBrightness(Ht16k33.HT16K33_BRIGHTNESS_MAX)
        segment.setEnabled(true)
        segment.clear()
    }

    fun display(message: String) {
        segment.display(message)
    }

    fun clear() {
        segment.clear()
    }

    fun close() {
        segment.close()
    }
}