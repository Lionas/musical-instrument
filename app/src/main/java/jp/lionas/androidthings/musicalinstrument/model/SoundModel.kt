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

package jp.lionas.androidthings.musicalinstrument.model

/**
 * Sound Data Model
 * @author Naoki Seto(@Lionas)
 */
class SoundModel {

    companion object {
        const val C = 262
        const val CS = 278
        const val D = 294
        const val DS = 312
        const val E = 330
        const val F = 349
        const val FS = 370
        const val G = 392
        const val GS = 416
        const val A = 440
        const val AS = 467
        const val B = 494
    }

    private val sounds = listOf(C, D, E, F, G, A, B)
    private val soundsStr = listOf("C", "D", "E", "F", "G", "A", "B")

    private val soundSemitones = listOf(CS, DS, E, FS, GS, AS, B)
    private val soundSemitonesStr = listOf("C#", "D#", "E", "F#", "G#", "A#", "B")

    var prevStep: Int = 0
    var currentStep: Int = 0
    var isSemitone: Boolean = false
    var isOctave: Boolean = false

    fun getCurrentSound(): Double {
        return sounds[currentStep].toDouble()
    }

    fun getCurrentSemitones(): Double {
        return soundSemitones[currentStep].toDouble()
    }

    fun getCurrentSoundStr(): String {
        return soundsStr[currentStep]
    }

    fun getCurrentSemitonesStr(): String {
        return soundSemitonesStr[currentStep]
    }
}
