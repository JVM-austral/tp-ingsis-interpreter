package factory

import lexer.Lexer
import lexer.LexerImplementation
import lexer.newrules.BooleanAnalyzer
import lexer.newrules.BooleanTypeAnalyzer
import lexer.newrules.ConstAnalyzer
import lexer.newrules.IfElseAnalyzer
import lexer.newrules.ReadEnvAnalyzer
import lexer.newrules.ReadInputAnalyzer
import lexer.newrules.TabAnalyzer
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

class LexerFactoryV2 {
    private val rules = listOf(
        ReadInputAnalyzer(), ReadEnvAnalyzer(), IfElseAnalyzer(), BooleanAnalyzer(), BooleanTypeAnalyzer(), NumberAnalyzer(), EnterAnalyzer(), ConstAnalyzer(), KeywordAnalyzer(), MidNumberAnalyzer(),
        OperatorAnalyzer(), NumberTypeAnalyzer(),
        PrintAnalyzer(), PunctuationAnalyzer(), StringTypeAnalyzer(),
        StringAnalyzer(), VariableAnalyzer(), WhitespaceAnalyzer(),
        MidStringAnalyzer(), TabAnalyzer(),

    )
    fun create(): Lexer {
        return LexerImplementation(rules)
    }
}
