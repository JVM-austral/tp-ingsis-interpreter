package factory

import analyzer.FunctionAnalyzer
import analyzer.LetVariableDeclarationAnalyzer
import analyzer.LetVariableDeclarationWithNumberAssignmentAnalyzer
import analyzer.LetVariableDeclarationWithStringAssignmentAnalyzer
import analyzer.VariableDefinitionAnalyzer
import parser.Parser
import parser.ParserImplementation

class ParserFactory : Factory<Parser> {
    private val rules = listOf(
        FunctionAnalyzer(),
        LetVariableDeclarationAnalyzer(),
        LetVariableDeclarationWithNumberAssignmentAnalyzer(),
        LetVariableDeclarationWithStringAssignmentAnalyzer(),
        VariableDefinitionAnalyzer(),

    )
    override fun create(): Parser {
        return ParserImplementation(rules)
    }
}
