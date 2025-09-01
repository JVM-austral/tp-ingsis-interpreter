package interpreter

import analyzer.InterpreterAnalyzer
import ast.Ast
import executor.FailInterpreterExecutor
import executor.InterpreterExecutor

class InterpreterImplementation(private val listOfAnalyzers: List<InterpreterAnalyzer>, private var heap: MutableMap<String, VariableInfo>) : Interpreter {
    private val executionQueue: MutableList<Result<Ast>> = mutableListOf()

    override fun interpret(parsedStatement: List<Result<Ast>>): List<Result<Ast>> {
        for (statement in parsedStatement) {
            val result = verifyRules(statement).execute(statement, heap)
            executionQueue.add(result)
        }
        return executionQueue
    }

    private fun verifyRules(statement: Result<Ast>): InterpreterExecutor {
        for (analyzer in listOfAnalyzers) {
            if (analyzer.analyzeInterpretation(statement, heap)) {
                return analyzer.getExecutor(heap)
            }
        }
        return FailInterpreterExecutor()
    }
}
