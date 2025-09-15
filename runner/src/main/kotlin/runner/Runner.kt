package runner

import errorhandler.MockErrorHandler
import java.io.InputStream

interface Runner {
    fun format(code: String, formatterConfigPath: String?): String

    fun run(code: InputStream)

    fun lint(code: String, linterConfigPath: String?)

    fun getErrorHandler(): MockErrorHandler
}
