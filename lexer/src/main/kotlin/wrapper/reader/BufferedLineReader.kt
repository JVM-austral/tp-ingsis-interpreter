package wrapper.reader

import java.io.BufferedReader

class BufferedLineReader(private val source: BufferedReader) : LineReader {
    override fun readLine(): String? = source.readLine()
}
