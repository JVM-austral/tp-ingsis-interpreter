package factory

import analyzer.FunctionAnalyzer
import analyzer.LetVariableDeclarationAnalyzer
import analyzer.LetVariableDeclarationWithNumberAssignmentAnalyzer
import analyzer.LetVariableDeclarationWithStringAssignmentAnalyzer
import analyzer.VariableDefinitionAnalyzer
import parser.Parser
import parser.ParserImplementation

class ParserFactoryV1 {
    private val rules = listOf(

        FunctionAnalyzer(),
        LetVariableDeclarationAnalyzer(listOf("number", "string"), listOf("let")),
        LetVariableDeclarationWithNumberAssignmentAnalyzer(listOf("number", "string"), listOf("let")),
        LetVariableDeclarationWithStringAssignmentAnalyzer(listOf("number", "string"), listOf("let")),
        VariableDefinitionAnalyzer(),

    )
    fun create(): Parser {
        return ParserImplementation(rules)
    }
}
