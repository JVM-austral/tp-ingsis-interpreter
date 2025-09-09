package analyzer

import ast.Ast
import ast.FunctionCallAst
import evaluator.AstEvaluationEngine
import executor.InterpreterExecutor
import executor.PrintLnExecutor
import interpreter.VariableInfo
import mock.MockOutputHandler
import mock.OutputHandler

class PrintLnAnalyzer(private val outputHandler: OutputHandler = MockOutputHandler()) : InterpreterAnalyzer {
    override fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return ast is FunctionCallAst
    }
    override fun getExecutor(heap: MutableMap<String, VariableInfo>): InterpreterExecutor {
        return PrintLnExecutor(AstEvaluationEngine(outputHandler))
    }
}
