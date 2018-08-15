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

import jp.lionas.androidthings.sensor.mcp3008.MCP3008Driver

/**
 * ADC Driver class
 * @author Naoki Seto(@Lionas)
 */
class AdcDriver {

    private var driver: MCP3008Driver = MCP3008Driver()

    fun register() {
        driver.register()
    }

    fun unregister() {
        driver.unregister()
    }

    fun setLowPowerMode(enable: Boolean) {
        driver.setLowPowerMode(enable)
    }

}