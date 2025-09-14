package analyzer

import ConditionMessageHandler
import ast.Ast
import ast.VarDefinition
import executor.InterpreterExecutor
import interpreter.VariableInfo

class VarDefinitionUnaryAnalyzer(private val conditionMessageHandler: ConditionMessageHandler) : InterpreterAnalyzer {

    override fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>, env:  MutableMap<String, Ast>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return ast is VarDefinition && ast.getListOfChildren()[1].getChildLimit() == 0
    }

    override fun getExecutor(heap: MutableMap<String, VariableInfo>, env:  MutableMap<String, Ast>): InterpreterExecutor {
        return executor.VarDefinitionUnaryExecutor(
            conditionMessageHandler,
        )
    }
}
