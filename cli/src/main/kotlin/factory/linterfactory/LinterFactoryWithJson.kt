package factory.linterfactory

import Linter
import factory.Factory
import linterconfig.ConfigurableLinter

class LinterFactoryWithJson(private val linterConfigPath: String?) : Factory<Linter> {

    override fun create(): Linter {
        var configPath = linterConfigPath
        if (configPath == null) {
            configPath = "linter-rules.json"
        }
        val configurableLinter = ConfigurableLinter(configPath)

        return configurableLinter.getConfigurableLinter()
    }
}
