package analyzer

import ast.Ast
import ast.IfDeclaration
import evaluator.AstEvaluator
import evaluator.input.InputProvider
import evaluator.input.LiteralConverter
import executor.IfDeclarationExecutor
import executor.InterpreterExecutor
import interpreter.VariableInfo
import mock.OutputHandler

class IfDeclarationAnalyzer(private val engine: AstEvaluator, private val outputHandler: OutputHandler, private val inputProvider: InputProvider,
                            private val converter: LiteralConverter
) : InterpreterAnalyzer {
    override fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>, env:  MutableMap<String, Ast>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return ast is IfDeclaration
    }

    override fun getExecutor(heap: MutableMap<String, VariableInfo>, env:  MutableMap<String, Ast>): InterpreterExecutor {
        return IfDeclarationExecutor(engine, outputHandler, inputProvider, converter)
    }
}
