package jp.lionas.androidthings.musicalinstrument.presenter.device

import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat

/**
 * Rainbow HAT Button class
 * @author Naoki Seto(@Lionas)
 */
class Buttons {

    private var buttonA: Button = RainbowHat.openButtonA()
    private var buttonB: Button = RainbowHat.openButtonB()
    private var buttonC: Button =  RainbowHat.openButtonC()

    private var isPressedA: Boolean = false
    private var isPressedB: Boolean = false
    private var isPressedC: Boolean = false

    fun closeButtons() {
        buttonA.close()
        buttonB.close()
        buttonC.close()
    }

    fun setOnButtonAEventListener(listener: (button: Button, pressed: Boolean) -> Unit) {
        buttonA.setOnButtonEventListener(listener)
    }

    fun setOnButtonBEventListener(listener: (button: Button, pressed: Boolean) -> Unit) {
        buttonB.setOnButtonEventListener(listener)
    }

    fun setOnButtonCEventListener(listener: (button: Button, pressed: Boolean) -> Unit) {
        buttonC.setOnButtonEventListener(listener)
    }

    fun isPressedA(): Boolean {
        return isPressedA
    }

    fun isPressedB(): Boolean {
        return isPressedB
    }

    fun isPressedC(): Boolean {
        return isPressedC
    }

    fun setPressedA(value: Boolean) {
        isPressedA = value
    }

    fun setPressedB(value: Boolean) {
        isPressedB = value
    }

    fun setPressedC(value: Boolean) {
        isPressedC = value
    }
}