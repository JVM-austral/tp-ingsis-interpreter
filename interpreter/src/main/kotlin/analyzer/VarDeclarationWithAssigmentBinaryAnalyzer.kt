package analyzer

import ast.Ast
import ast.BinaryOperation
import ast.VarDeclaration
import evaluator.AstEvaluationEngine
import executor.InterpreterExecutor
import executor.VarDeclarationWithAssigmentBinaryExecutor
import interpreter.VariableInfo

class VarDeclarationWithAssigmentBinaryAnalyzer : InterpreterAnalyzer {
    override fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return (ast is VarDeclaration) && (ast.getListOfChildren()[2] is BinaryOperation)
    }

    override fun getExecutor(heap: MutableMap<String, VariableInfo>): InterpreterExecutor {
        return VarDeclarationWithAssigmentBinaryExecutor(AstEvaluationEngine())
    }
}
