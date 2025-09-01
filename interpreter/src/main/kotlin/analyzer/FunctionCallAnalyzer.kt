package analyzer

import ast.Ast
import ast.FunctionCallAst
import evaluator.AstEvaluationEngine
import executor.FunctionCallExecutor
import executor.InterpreterExecutor
import interpreter.VariableInfo

class FunctionCallAnalyzer : InterpreterAnalyzer {
    override fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return ast is FunctionCallAst
    }
    override fun getExecutor(heap: MutableMap<String, VariableInfo>): InterpreterExecutor {
        return FunctionCallExecutor(AstEvaluationEngine())
    }
}
