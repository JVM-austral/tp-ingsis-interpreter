package factory.version.first.linterfactory

import Linter
import factory.Factory
import linterconfig.ConfigurableLinter

class LinterFactoryV1WithJson(private val linterConfigPath: String?) : Factory<Linter> {

    override fun create(): Linter {
        var configPath = linterConfigPath
        if (configPath == null) {
            configPath = "src/main/resources/linter-rules-v-1.json"
        }
        val configurableLinter = ConfigurableLinter(configPath)

        println("hay? " + configurableLinter.getConfigurableLinter())

        return configurableLinter.getConfigurableLinter()
    }
}
