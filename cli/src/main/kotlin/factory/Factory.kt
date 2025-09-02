package factory

interface Factory<T> {
    fun create(): T
}
