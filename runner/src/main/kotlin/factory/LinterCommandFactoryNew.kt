package factory

import lexer.Lexer
import linter.Linter
import linterconfig.ConfigurableAnalyzerOptionsV1
import linterconfig.ConfigurableAnalyzerOptionsV2
import linterconfig.ConfigurableAnalyzersOptions
import linterfactory.LinterFactoryV1
import linterfactory.LinterFactoryV2
import parser.Parser

class LinterCommandFactoryNew(
    private val version: Version,
    private val config: ConfigurableAnalyzersOptions,
) {
    fun getLexer(): Lexer =
        when (version) {
            Version.V1 -> LexerFactoryV1().create()
            Version.V2 -> LexerFactoryV2().create()
        }

    fun getParser(): Parser =
        when (version) {
            Version.V1 -> ParserFactoryV1().create()
            Version.V2 -> ParserFactoryV2().create()
        }

    fun getLinter(): Linter =
        if (version.toString() == config.getVersion()) {
            when (version) {
                Version.V1 -> LinterFactoryV1(config as ConfigurableAnalyzerOptionsV1).create()
                Version.V2 -> LinterFactoryV2(config as ConfigurableAnalyzerOptionsV2).create()
            }
        } else {
            throw IllegalArgumentException("Version mismatch between LinterCommandFactoryNew and ConfigurableAnalyzersOptions")
        }
}
