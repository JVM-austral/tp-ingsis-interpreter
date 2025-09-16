package factory

import lexer.Lexer
import lexer.LexerImplementation
import lexer.newrules.BooleanAnalyzer
import lexer.newrules.BooleanOperatorsAnalyzer
import lexer.newrules.BooleanTypeAnalyzer
import lexer.newrules.ConstAnalyzer
import lexer.newrules.IfElseAnalyzer
import lexer.newrules.ReadEnvAnalyzer
import lexer.newrules.ReadInputAnalyzer
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

class LexerFactoryV1 {

    private val analyzers = listOf(
        BooleanOperatorsAnalyzer(),
        BooleanAnalyzer(),
        BooleanTypeAnalyzer(),
        ConstAnalyzer(),
        IfElseAnalyzer(),
        ReadInputAnalyzer(),
        KeywordAnalyzer(),
        NumberAnalyzer(),
        NumberTypeAnalyzer(),
        OperatorAnalyzer(),
        PunctuationAnalyzer(),
        StringAnalyzer(),
        StringTypeAnalyzer(),
        VariableAnalyzer(),
        WhitespaceAnalyzer(),
        MidStringAnalyzer(),
        MidNumberAnalyzer(),
        ReadEnvAnalyzer(),
        EnterAnalyzer(),
        PrintAnalyzer(),
    )
    fun create(): Lexer {
        return LexerImplementation(analyzers)
    }
}
