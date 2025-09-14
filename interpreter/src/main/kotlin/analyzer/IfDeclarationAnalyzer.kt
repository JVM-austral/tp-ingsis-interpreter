package analyzer

import ast.Ast
import ast.IfDeclaration
import executor.InterpreterExecutor
import executor.IfDeclarationExecutor
import interpreter.VariableInfo
import evaluator.AstEvaluator
import mock.OutputHandler

class IfDeclarationAnalyzer(private val engine: AstEvaluator,private val outputHandler: OutputHandler) : InterpreterAnalyzer {
    override fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>,env:MutableMap<String,String>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return ast is IfDeclaration
    }

    override fun getExecutor(heap: MutableMap<String, VariableInfo>,env:MutableMap<String,String>): InterpreterExecutor {
        return IfDeclarationExecutor(engine, outputHandler)
    }
}
