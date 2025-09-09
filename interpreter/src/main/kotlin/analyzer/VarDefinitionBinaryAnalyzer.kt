package analyzer

import IsCompatibleTypeCondition
import PriorityDeclarationCondition
import VarDefinitionBinaryStructureCondition
import ast.Ast
import ast.BinaryOperation
import ast.VarDefinition
import evaluator.AstEvaluationEngine
import executor.InterpreterExecutor
import interpreter.VariableInfo

class VarDefinitionBinaryAnalyzer : InterpreterAnalyzer {
    override fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return ast is VarDefinition && ast.getListOfChildren()[1] is BinaryOperation
    }

    override fun getExecutor(heap: MutableMap<String, VariableInfo>): InterpreterExecutor {
        return executor.VarDefinitionBinaryExecutor(
            AstEvaluationEngine(),
            IsCompatibleTypeCondition(
                mapOfCondition = mapOf(
                    "number" to Number::class,
                    "string" to String::class,
                    "boolean" to Boolean::class,
                ),
            ),
            VarDefinitionBinaryStructureCondition(),
            PriorityDeclarationCondition(),
        )
    }
}
