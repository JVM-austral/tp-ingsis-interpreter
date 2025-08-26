package analyzer

import ast.Assigment
import ast.Ast
import ast.BinaryOperation
import executor.InterpreterExecutor
import interpreter.VariableInfo

class VarDefinitionBinaryAnalyzer : InterpreterAnalyzer {
    override fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return ast is Assigment && ast.getListOfChildren()[1] is BinaryOperation
    }

    override fun getExecutor(heap: MutableMap<String, VariableInfo>): InterpreterExecutor {
        return executor.VarDefinitionBinaryExecutor()
    }
}
