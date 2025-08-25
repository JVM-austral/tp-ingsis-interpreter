package lexertest

import com.google.gson.Gson
import lexer.LexerImplementation
import lexer.rules.KeywordAnalyzer
import lexer.rules.MidNumberAnalyzer
import lexer.rules.MidStringAnalyzer
import lexer.rules.NumberAnalyzer
import lexer.rules.NumberTypeAnalyzer
import lexer.rules.OperatorAnalyzer
import lexer.rules.PunctuationAnalyzer
import lexer.rules.StringAnalyzer
import lexer.rules.StringTypeAnalyzer
import lexer.rules.VariableAnalyzer
import lexer.rules.WhitespaceAnalyzer

object TestGenerator {
    private val analyzers = listOf(
        KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
        OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
        VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer(),
    )

    private val lexer = LexerImplementation(analyzers)
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
