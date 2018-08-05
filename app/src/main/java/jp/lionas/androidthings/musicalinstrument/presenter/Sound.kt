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

import com.google.android.things.contrib.driver.pwmspeaker.Speaker
import jp.lionas.androidthings.musicalinstrument.model.SoundModel

/**
 * Sound class for Sample
 * @author Naoki Seto(@Lionas)
 */
class Sound(private val speaker: Speaker) {

    private var soundModel = SoundModel()

    fun play() {
        val mag = if (soundModel.isOctave) 2 else 1
        if (soundModel.isSemitone) {
            speaker.play(soundModel.getCurrentSemitones() * mag)
        } else {
            speaker.play(soundModel.getCurrentSound() * mag)
        }
    }

    fun playOnChanged(value: Int) {
        setCurrentKey(value)
        if (soundModel.prevStep != soundModel.currentStep) {
            speaker.stop()
            play()
            soundModel.prevStep = soundModel.currentStep
        }
    }

    fun setSemitone(isSemitone: Boolean) {
        soundModel.isSemitone = isSemitone
    }

    fun setOctave(isOctave: Boolean) {
        soundModel.isOctave = isOctave
    }

    fun setCurrentKey(value: Int): String {
        soundModel.currentStep = value / 170
        return getCurrentKeyString()
    }

    fun getCurrentKeyString(): String {
        return if (soundModel.isSemitone) {
            soundModel.getCurrentSemitonesStr()
        } else {
            soundModel.getCurrentSoundStr()
        }
    }

    fun getCurrentKeyIndex(): Int {
        return soundModel.currentStep
    }

    fun isCurrentOctave(): Boolean {
        return soundModel.isOctave
    }
}