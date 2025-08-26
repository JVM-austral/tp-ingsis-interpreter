package analyzer

import ast.Ast
import ast.BinaryOperation
import ast.ScapeAst
import ast.VarDeclaration
import executor.InterpreterExecutor
import executor.VarDeclarationWithAssigmentUnaryExecutor
import interpreter.VariableInfo

class VarDeclarationWithAssigmentUnaryAnalyzer : InterpreterAnalyzer {

    override fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return (ast is VarDeclaration) && (ast.expr !is ScapeAst) && (ast.getListOfChildren()[2] !is BinaryOperation)
    }

    override fun getExecutor(heap: MutableMap<String, VariableInfo>): InterpreterExecutor {
        return VarDeclarationWithAssigmentUnaryExecutor(heap)
    }
}
