package factory

import lexer.Lexer
import lexer.LexerImplementation
import lexer.rules.EnterAnalyzer
import lexer.rules.KeywordAnalyzer
import lexer.rules.MidNumberAnalyzer
import lexer.rules.MidStringAnalyzer
import lexer.rules.NumberAnalyzer
import lexer.rules.NumberTypeAnalyzer
import lexer.rules.OperatorAnalyzer
import lexer.rules.PrintAnalyzer
import lexer.rules.PunctuationAnalyzer
import lexer.rules.StringAnalyzer
import lexer.rules.StringTypeAnalyzer
import lexer.rules.VariableAnalyzer
import lexer.rules.WhitespaceAnalyzer


class LexerFactory : Factory<Lexer> {

    private val rules = listOf(
        NumberAnalyzer(), EnterAnalyzer(), KeywordAnalyzer(), MidNumberAnalyzer(),
        OperatorAnalyzer(), NumberTypeAnalyzer(),
        PrintAnalyzer(), PunctuationAnalyzer(), StringTypeAnalyzer(),
        StringAnalyzer(), VariableAnalyzer(), WhitespaceAnalyzer(),
        MidStringAnalyzer(),

    )
    override fun create(): Lexer {
        return LexerImplementation(rules)
    }
}
