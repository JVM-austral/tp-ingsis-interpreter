package lexertest

import com.google.gson.Gson
import lexer.LexerImplementationV1
import lexer.rules.*


object TestGenerator {
    private val analyzers = listOf(
        KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
        OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
        VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer()
    )

    private val lexer = LexerImplementationV1(analyzers)
    private val gson = Gson()

    private fun generateJson(input: String): String {
        val result = lexer.tokenize(input)
        return gson.toJson(result)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val input = if (args.isNotEmpty()) args.joinToString(" ") else ""
        println(generateJson(input))
    }
}