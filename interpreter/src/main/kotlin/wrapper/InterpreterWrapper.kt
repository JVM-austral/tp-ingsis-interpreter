package wrapper

import ast.Ast
import interpreter.ExecutionUnit
import interpreter.Interpreter

class InterpreterWrapper(
    private val parserWrapper: IteratorWrapper<Result<Ast>>,
    private val interpreter: Interpreter,
    private val batchSize: Int = 512,
) : IteratorWrapper<ExecutionUnit> {

    private var execIterator: Iterator<ExecutionUnit>? = null
    private var inputDepleted = false

    private fun fillExecIterator() {
        if (execIterator?.hasNext() == true) return

        while (!inputDepleted) {
            val astBatch = ArrayList<Result<Ast>>(batchSize)
            var count = 0
            while (count < batchSize && parserWrapper.hasNext()) {
                astBatch.add(parserWrapper.next())
                count++
            }
            if (count == 0) {
                inputDepleted = true
                execIterator = null
                return
            }
            val exec = interpreter.interpret(astBatch)
            val it = exec.iterator()
            if (it.hasNext()) {
                execIterator = it
                return
            }
        }
        execIterator = null
    }

    override fun hasNext(): Boolean {
        fillExecIterator()
        return execIterator?.hasNext() == true
    }

    override fun next(): ExecutionUnit {
        fillExecIterator()
        val it = execIterator ?: throw NoSuchElementException()
        return it.next()
    }
}
