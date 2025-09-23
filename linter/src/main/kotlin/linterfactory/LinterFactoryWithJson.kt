package linterfactory

import linter.Linter
import linterconfig.ConfigurableLinter

class LinterFactoryWithJson(
    private val linterConfigPath: String?,
    private val v2: Boolean = true,
) {
    fun create(): Linter {
        var configPath = linterConfigPath
        if (configPath == null) {
            configPath = "src/main/resources/linter-rules-v-2.json"
        }
        val configurableLinter = ConfigurableLinter(configPath, v2)

        return configurableLinter.getConfigurableLinter()
    }
}
