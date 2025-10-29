package evaluator.input

import java.util.LinkedList
import java.util.Queue

class ProvideQueueOfInputs(
    private val values: List<String>,
) : InputProvider {
    private val valuesQueue: Queue<String> = LinkedList(values)

    override fun read(): String = valuesQueue.poll()
}
