package executors

interface LinterExecutor {

    fun execute(code: String): Error

}