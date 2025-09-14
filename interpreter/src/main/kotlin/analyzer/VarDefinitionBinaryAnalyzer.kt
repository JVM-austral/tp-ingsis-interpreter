package analyzer

import IsCompatibleTypeCondition
import PriorityDeclarationCondition
import VarDefinitionBinaryStructureCondition
import ast.Ast
import ast.BinaryOperation
import ast.VarDefinition
import condition.ConstDefinitionCondition
import evaluator.AstEvaluator
import executor.InterpreterExecutor
import interpreter.VariableInfo

class VarDefinitionBinaryAnalyzer(private val engine: AstEvaluator,
                                  private val isCompatibleTypeCondition: IsCompatibleTypeCondition,
                                  private val structureCondition: VarDefinitionBinaryStructureCondition,
                                  private val declarationCondition: PriorityDeclarationCondition,
                                  private val constCondition: ConstDefinitionCondition
    ) : InterpreterAnalyzer {
    override fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>,env:MutableMap<String,String>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return ast is VarDefinition && ast.getListOfChildren()[1] is BinaryOperation
    }

    override fun getExecutor(heap: MutableMap<String, VariableInfo>,env:MutableMap<String,String>): InterpreterExecutor {
        return executor.VarDefinitionBinaryExecutor(
            engine,
            isCompatibleTypeCondition,
            structureCondition,
            declarationCondition,
            constCondition
        )
    }
}
