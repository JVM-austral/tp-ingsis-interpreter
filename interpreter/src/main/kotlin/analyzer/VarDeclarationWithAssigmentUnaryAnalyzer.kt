package analyzer

import ConditionMessageHandler
import IsCompatibleTypeCondition
import ast.Ast
import ast.BinaryOperation
import ast.ScapeAst
import ast.VarDeclaration
import evaluator.AstEvaluator
import executor.InterpreterExecutor
import executor.VarDeclarationWithAssigmentUnaryExecutor
import interpreter.VariableInfo

class VarDeclarationWithAssigmentUnaryAnalyzer(private val engine: AstEvaluator, private val conditionHandler: ConditionMessageHandler, private val isCompatibleTypeCondition: IsCompatibleTypeCondition) : InterpreterAnalyzer {

    override fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>, env: MutableMap<String, Ast>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return (ast is VarDeclaration) && (ast.expr !is ScapeAst) && (ast.getListOfChildren()[2] !is BinaryOperation)
    }

    override fun getExecutor(heap: MutableMap<String, VariableInfo>, env: MutableMap<String, Ast>): InterpreterExecutor {
        return VarDeclarationWithAssigmentUnaryExecutor(engine, conditionHandler, isCompatibleTypeCondition)
    }
}
