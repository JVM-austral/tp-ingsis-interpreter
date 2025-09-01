package analyzer

import ast.Ast
import ast.VarDefinition
import executor.InterpreterExecutor
import interpreter.VariableInfo

class VarDefinitionUnaryAnalyzer : InterpreterAnalyzer {

    override fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return ast is VarDefinition && ast.getListOfChildren()[1].getChildLimit() == 0
    }

    override fun getExecutor(heap: MutableMap<String, VariableInfo>): InterpreterExecutor {
        return executor.VarDefinitionUnaryExecutor()
    }
}
