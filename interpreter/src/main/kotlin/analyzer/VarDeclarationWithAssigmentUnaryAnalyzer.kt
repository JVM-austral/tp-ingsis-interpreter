package analyzer

import ConditionMessageHandler
import ast.Ast
import ast.BinaryOperation
import ast.ScapeAst
import ast.VarDeclaration
import executor.InterpreterExecutor
import executor.VarDeclarationWithAssigmentUnaryExecutor
import interpreter.VariableInfo

class VarDeclarationWithAssigmentUnaryAnalyzer(private val conditionHandler: ConditionMessageHandler) : InterpreterAnalyzer {

    override fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>, env: MutableMap<String, String>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return (ast is VarDeclaration) && (ast.expr !is ScapeAst) && (ast.getListOfChildren()[2] !is BinaryOperation)
    }

    override fun getExecutor(heap: MutableMap<String, VariableInfo>, env: MutableMap<String, String>): InterpreterExecutor {
        return VarDeclarationWithAssigmentUnaryExecutor(conditionHandler)
    }
}
