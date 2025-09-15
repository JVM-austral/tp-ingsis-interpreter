package wrapper

import ast.Ast
import interpreter.ExecutionUnit
import interpreter.Interpreter

class InterpreterWrapper(
    private val parserWrapper: IteratorWrapper<Result<Ast>>,
    private val interpreter: Interpreter,
) : IteratorWrapper<ExecutionUnit> {
    private var executionBuffer: MutableList<ExecutionUnit>? = null

    private fun ensureInterpreted() {
        if (executionBuffer == null) {
            val astResults = mutableListOf<Result<Ast>>()
            while (parserWrapper.hasNext()) {
                astResults.add(parserWrapper.next())
            }
            executionBuffer = interpreter.interpret(astResults).toMutableList()
        }
    }

    override fun hasNext(): Boolean {
        ensureInterpreted()
        return executionBuffer?.isNotEmpty() == true
    }

    override fun next(): ExecutionUnit {
        ensureInterpreted()
        if (executionBuffer.isNullOrEmpty()) throw NoSuchElementException()
        return executionBuffer!!.removeFirst()
    }
}
