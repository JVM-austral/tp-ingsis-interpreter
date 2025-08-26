package interpreter

import analyzer.InterpreterAnalyzer
import ast.Ast
import executor.FailInterpreterExecutor
import executor.InterpreterExecutor

class InterpreterImplementation(private val listOfAnalyzers: List<InterpreterAnalyzer>, private var heap: MutableMap<String, VariableInfo>) : Interpreter {
    private val executionQueue: MutableList<Result<MutableMap<String, VariableInfo>>> = mutableListOf()

    override fun interpret(parsedStatement: List<Result<Ast>>): List<Result<MutableMap<String, VariableInfo>>> {
        for (statement in parsedStatement) {
            val result = verifyRules(statement).execute(statement, heap)
            result.onSuccess { resultHeap ->
                heap = resultHeap
            }
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
