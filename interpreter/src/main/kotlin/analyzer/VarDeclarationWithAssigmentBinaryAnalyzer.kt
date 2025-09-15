package analyzer

import IsCompatibleTypeCondition
import ast.Ast
import ast.BinaryOperation
import ast.VarDeclaration
import condition.ConstDefinitionCondition
import evaluator.AstEvaluator
import executor.InterpreterExecutor
import executor.VarDeclarationWithAssigmentBinaryExecutor
import interpreter.VariableInfo

class VarDeclarationWithAssigmentBinaryAnalyzer(private val engine: AstEvaluator, private val condition: IsCompatibleTypeCondition, private val constCondition: ConstDefinitionCondition) : InterpreterAnalyzer {
    override fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>, env: MutableMap<String, Ast>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return (ast is VarDeclaration) && (ast.getListOfChildren()[2] is BinaryOperation)
    }

    override fun getExecutor(heap: MutableMap<String, VariableInfo>, env: MutableMap<String, Ast>): InterpreterExecutor {
        return VarDeclarationWithAssigmentBinaryExecutor(
            engine,
            condition,
            constCondition,
        )
    }
}
