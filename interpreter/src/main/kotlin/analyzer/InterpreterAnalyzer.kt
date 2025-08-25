package analyzer

import ast.Ast
import executor.InterpreterExecutor
import interpreter.VariableInfo


interface InterpreterAnalyzer {
    fun analyzeInterpretation(statement: Result<Ast>): Boolean;
    fun getExecutor(heap:MutableMap<String, VariableInfo>): InterpreterExecutor
}