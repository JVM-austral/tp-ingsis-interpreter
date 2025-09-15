package interpreter

import ast.Ast

class ExecutionEngine(
    private val heap: MutableMap<String, VariableInfo>,
    private val env: MutableMap<String, Ast>,
) {
    fun runAll(units: List<ExecutionUnit>): List<ExecutionUnit> {
        val results = mutableListOf<ExecutionUnit>()
        for (unit in units) {
            if (unit.message != null) {
                results.add(unit)
            } else {
                val execution = unit.executor.execute(unit.statement, heap, env)
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
        return results
    }
}
