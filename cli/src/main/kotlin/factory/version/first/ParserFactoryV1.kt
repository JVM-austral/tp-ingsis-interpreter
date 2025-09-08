package factory.version.first

import analyzer.BinaryNumberOperatorAnalyzer
import analyzer.FunctionAnalyzer
import analyzer.LetVariableDeclarationAnalyzer
import analyzer.LetVariableDeclarationWithNumberAssignmentAnalyzer
import analyzer.LetVariableDeclarationWithStringAssignmentAnalyzer
import analyzer.StringConcatenationAnalyzer
import analyzer.VariableDefinitionAnalyzer
import factory.Factory
import parser.Parser
import parser.ParserImplementation

class ParserFactoryV1 : Factory<Parser> {
    private val rules = listOf(
        BinaryNumberOperatorAnalyzer(),
        FunctionAnalyzer(),
        LetVariableDeclarationAnalyzer(),
        LetVariableDeclarationWithNumberAssignmentAnalyzer(),
        LetVariableDeclarationWithStringAssignmentAnalyzer(),
        StringConcatenationAnalyzer(),
        VariableDefinitionAnalyzer(),

    )
    override fun create(): Parser {
        return ParserImplementation(rules)
    }
}
