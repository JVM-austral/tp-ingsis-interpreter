package factory.version.first

import analyzer.FunctionCallAnalyzer
import analyzer.TypeDeclarationAnalyzer
import analyzer.VarDeclarationWithAssigmentBinaryAnalyzer
import analyzer.VarDeclarationWithAssigmentUnaryAnalyzer
import analyzer.VarDeclarationWithAssignmentAnalyzer
import analyzer.VarDefinitionBinaryAnalyzer
import analyzer.VarDefinitionUnaryAnalyzer
import factory.Factory
import interpreter.Interpreter
import interpreter.InterpreterImplementation

class InterpreterFactoryV1 : Factory<Interpreter> {
    private val rules = listOf(
        FunctionCallAnalyzer(),
        TypeDeclarationAnalyzer(),
        VarDeclarationWithAssignmentAnalyzer(),
        VarDeclarationWithAssigmentUnaryAnalyzer(),
        VarDeclarationWithAssigmentBinaryAnalyzer(),
        VarDefinitionUnaryAnalyzer(),
        VarDefinitionBinaryAnalyzer(),
    )
    override fun create(): Interpreter {
        return InterpreterImplementation(rules, heap = mutableMapOf())
    }
}
