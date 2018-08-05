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

import com.google.android.things.contrib.driver.pwmspeaker.Speaker
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat

/**
 * Rainbow HAT Speaker class
 * @author Naoki Seto(@Lionas)
 */
class Speaker {

    private var speaker: Speaker = RainbowHat.openPiezo()
    private var sound: Sound

    init {
        sound = Sound(speaker)
    }

    fun close() {
        speaker.close()
    }

    fun play() {
        sound.play()
    }

    fun playOnChanged(value: Int) {
        sound.playOnChanged(value)
    }

    fun updateSemitone(isSemitone: Boolean) {
        sound.updateSemitone(isSemitone)
    }

    fun updateOctave(isOctave: Boolean) {
        sound.updateOctave(isOctave)
    }

    fun updateKey(value: Int): String {
        return sound.updateKey(value)
    }

    fun getCurrentKeyString(): String {
        return sound.getCurrentKeyString()
    }

    fun getCurrentKeyIndex(): Int {
        return sound.getCurrentKeyIndex()
    }

    fun isCurrentOctave(): Boolean {
        return sound.isCurrentOctave()
    }

    fun stop() {
        speaker.stop()
    }
}