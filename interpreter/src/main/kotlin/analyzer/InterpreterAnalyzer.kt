package analyzer

import ast.Ast
import executor.InterpreterExecutor
import interpreter.VariableInfo

interface InterpreterAnalyzer {
    fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>,env:MutableMap<String,String>): Boolean
    fun getExecutor(heap: MutableMap<String, VariableInfo>,env:MutableMap<String,String>): InterpreterExecutor
}
