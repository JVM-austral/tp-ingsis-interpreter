package analyzer

import ConditionMessageHandler
import VariableAlreadyDeclaredCondition
import ast.Ast
import ast.ScapeAst
import ast.VarDeclaration
import executor.InterpreterExecutor
import executor.TypeDeclarationExecutor
import interpreter.VariableInfo

class TypeDeclarationAnalyzer : InterpreterAnalyzer {
    override fun analyzeInterpretation(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,
        env: MutableMap<String, Ast>,
    ): Boolean {
        val ast = statement.getOrNull() ?: return false
        return (ast is VarDeclaration && ast.expr is ScapeAst)
    }

    override fun getExecutor(
        heap: MutableMap<String, VariableInfo>,
        env: MutableMap<String, Ast>,
    ): InterpreterExecutor = TypeDeclarationExecutor(ConditionMessageHandler(listOfConditions = listOf(VariableAlreadyDeclaredCondition())))
}
