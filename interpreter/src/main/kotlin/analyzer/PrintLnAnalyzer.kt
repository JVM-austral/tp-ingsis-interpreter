package analyzer

import ast.Ast
import ast.FunctionCallAst
import evaluator.AstEvaluator
import executor.InterpreterExecutor
import executor.PrintLnExecutor
import interpreter.VariableInfo
import mock.MockOutputHandler
import mock.OutputHandler

class PrintLnAnalyzer(private val outputHandler: OutputHandler = MockOutputHandler(),private val engine: AstEvaluator) : InterpreterAnalyzer {
    override fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>,env:MutableMap<String,String>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return ast is FunctionCallAst && ast.getValue() == "println"
    }
    override fun getExecutor(heap: MutableMap<String, VariableInfo>,env:MutableMap<String,String>): InterpreterExecutor {
        return PrintLnExecutor(engine)
    }
}
