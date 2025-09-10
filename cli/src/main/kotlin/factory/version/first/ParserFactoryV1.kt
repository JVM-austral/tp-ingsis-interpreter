package factory.version.first

import analyzer.FunctionAnalyzer
import analyzer.LetVariableDeclarationAnalyzer
import analyzer.LetVariableDeclarationWithNumberAssignmentAnalyzer
import analyzer.LetVariableDeclarationWithStringAssignmentAnalyzer
import analyzer.VariableDefinitionAnalyzer
import factory.Factory
import parser.Parser
import parser.ParserImplementation

class ParserFactoryV1 : Factory<Parser> {
    private val rules = listOf(

        FunctionAnalyzer(),
        LetVariableDeclarationAnalyzer(listOf("number", "string"), listOf("let")),
        LetVariableDeclarationWithNumberAssignmentAnalyzer(listOf("number", "string"), listOf("let")),
        LetVariableDeclarationWithStringAssignmentAnalyzer(listOf("number", "string"), listOf("let")),
        VariableDefinitionAnalyzer(),

    )
    override fun create(): Parser {
        return ParserImplementation(rules)
    }
}
