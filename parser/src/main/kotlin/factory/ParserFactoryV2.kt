package factory

import analyzer.FunctionAnalyzer
import analyzer.LetVariableDeclarationAnalyzer
import analyzer.LetVariableDeclarationWithNumberAssignmentAnalyzer
import analyzer.LetVariableDeclarationWithStringAssignmentAnalyzer
import analyzer.VariableDefinitionAnalyzer
import newanalyzers.BooleanDeclarationAnalyzer
import newanalyzers.BooleanDefinitionAnalyzer
import newanalyzers.IfAnalyzer
import newanalyzers.LetVariableDeclarationWithBooleanAnalyzer
import newanalyzers.LetVariableDeclarationWithEnvAssignment
import newanalyzers.LetVariableDeclarationWithInputAssignment
import newanalyzers.VariableDefinitionWithEnvAnalyzer
import newanalyzers.VariableDefinitionWithInputAnalyzer
import parser.Parser
import parser.ParserImplementation

class ParserFactoryV2 {
    private val rules = listOf(
        FunctionAnalyzer(), LetVariableDeclarationAnalyzer(listOf("number", "string"), listOf("let")),
        LetVariableDeclarationWithNumberAssignmentAnalyzer(listOf("number", "string"), listOf("let", "const")),
        LetVariableDeclarationWithStringAssignmentAnalyzer(listOf("number", "string"), listOf("let", "const")),
        VariableDefinitionAnalyzer(),
        BooleanDefinitionAnalyzer(),
        BooleanDeclarationAnalyzer(listOf("number", "string", "boolean"), listOf("let", "const")),
        LetVariableDeclarationWithBooleanAnalyzer(listOf("number", "string", "boolean"), listOf("let", "const")),
        LetVariableDeclarationWithEnvAssignment(listOf("number", "string", "boolean"), listOf("let", "const")),
        VariableDefinitionWithEnvAnalyzer(),
        IfAnalyzer(),
        LetVariableDeclarationWithInputAssignment(listOf("number", "string", "boolean"), listOf("let", "const")),
        VariableDefinitionWithInputAnalyzer(),
    )

    fun create(): Parser {
        return ParserImplementation(rules)
    }
}
