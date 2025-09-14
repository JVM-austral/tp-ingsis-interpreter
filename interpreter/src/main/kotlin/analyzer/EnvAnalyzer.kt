package analyzer

import ast.Ast
import ast.FunctionCallAst
import executor.EnvExecutor
import executor.InterpreterExecutor
import interpreter.VariableInfo

class EnvAnalyzer :InterpreterAnalyzer {
    override fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>,env:MutableMap<String,String>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return ast is FunctionCallAst && ast.getValue()=="readEnv"
    }

    override fun getExecutor(heap: MutableMap<String, VariableInfo>,env:MutableMap<String,String>): InterpreterExecutor {
        return EnvExecutor()
    }
}
