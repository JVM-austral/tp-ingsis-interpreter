package interpreter

import analyzer.InterpreterAnalyzer
import ast.Ast
import executor.FailInterpreterExecutor
import executor.InterpreterExecutor

class InterpreterImplementation(
    private val listOfAnalyzers: List<InterpreterAnalyzer>,
    private var heap: MutableMap<String, VariableInfo>,
    private var env: MutableMap<String, Ast>,
) : Interpreter {

    private val executionQueue: MutableList<ExecutionUnit> = mutableListOf()

    override fun interpret(parsedStatement: List<Result<Ast>>): List<ExecutionUnit> {
        executionQueue.clear()

        for (statement in parsedStatement) {
            val executor: InterpreterExecutor
            val message: String?

            if (statement.isFailure) {
                val error = statement.exceptionOrNull()
                executor = FailInterpreterExecutor(error)
                message = error?.message ?: "unknown statement error"
            } else {
                executor = verifyRules(statement)
                message = null
            }
            executionQueue.add(
                ExecutionUnit(
                    executor = executor,
                    statement = statement,
                    message = message,
                ),
            )
        }

        return executionQueue
    }

    private fun verifyRules(statement: Result<Ast>): InterpreterExecutor {
        for (analyzer in listOfAnalyzers) {
            if (analyzer.analyzeInterpretation(statement, heap, env)) {
                return analyzer.getExecutor(heap, env)
            }
        }
        return FailInterpreterExecutor()
    }
}
