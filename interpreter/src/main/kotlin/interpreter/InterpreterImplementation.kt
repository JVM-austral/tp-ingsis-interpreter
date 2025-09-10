package interpreter

import analyzer.InterpreterAnalyzer
import ast.Ast
import executor.FailInterpreterExecutor
import executor.InterpreterExecutor

class InterpreterImplementation(
    private val listOfAnalyzers: List<InterpreterAnalyzer>,
    private var heap: MutableMap<String, VariableInfo>,
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

    override fun runAll(): List<ExecutionUnit> {
        val results = mutableListOf<ExecutionUnit>()
        for (unit in executionQueue) {
            if (unit.message != null) {
                results.add(unit)
            } else {
                val execution = unit.executor.execute(unit.statement, heap)
                if (execution.isFailure) {
                    results.add(
                        ExecutionUnit(
                            executor = unit.executor,
                            statement = execution,
                            message = execution.exceptionOrNull()?.message,
                        ),
                    )
                }
            }
        }
        executionQueue.clear()
        return results
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
