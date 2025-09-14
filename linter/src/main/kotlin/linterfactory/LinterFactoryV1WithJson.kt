package linterfactory

import Linter
import linterconfig.ConfigurableLinter

class LinterFactoryV1WithJson(private val linterConfigPath: String?) {

    fun create(): Linter {
        var configPath = linterConfigPath
        if (configPath == null) {
            configPath = "src/main/resources/linter-rules-v-1.json"
        }
        val configurableLinter = ConfigurableLinter(configPath)

        return configurableLinter.getConfigurableLinter()
    }
}
