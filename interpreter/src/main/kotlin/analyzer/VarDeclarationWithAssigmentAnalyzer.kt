package analyzer

import ast.Ast
import ast.ScapeAst
import ast.VarDeclaration
import executor.InterpreterExecutor
import executor.VarDeclarationWithAssignmentExecutor

import interpreter.VariableInfo

class VarDeclarationWithAssigmentAnalyzer:InterpreterAnalyzer {
    override fun analyzeInterpretation(statement: Result<Ast>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return (ast is VarDeclaration) && (ast.expr !is ScapeAst)
    }

    override fun getExecutor(heap: MutableMap<String, VariableInfo>): InterpreterExecutor {
        return  VarDeclarationWithAssignmentExecutor()
    }
}