package runner

interface Runner {
    fun format(code: String, formatterConfigPath: String?): String

    fun run(code: String)

    fun lint(code: String, linterConfigPath: String?)
}
