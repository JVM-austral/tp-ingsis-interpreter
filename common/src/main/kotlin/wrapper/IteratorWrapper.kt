package wrapper

interface IteratorWrapper<T> {
    fun hasNext(): Boolean
    fun next(): T
}
