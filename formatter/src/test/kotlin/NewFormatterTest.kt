import analyzers.CanNotStartLineWithSpaceAnalyzer
import analyzers.NewLineAfterSemiColonAnalyzer
import analyzers.NewLinesBeforePrintlnAnalyzer
import analyzers.OnlyOneSpaceAnalyzer
import analyzers.SpaceAfterColonAnalyzer
import analyzers.SpaceAfterEqualsAnalyzer
import analyzers.SpaceAfterOperatorAnalyzer
import analyzers.SpaceBeforeColonAnalyzer
import analyzers.SpaceBeforeEqualsAnalyzer
import analyzers.SpaceBeforeOperatorAnalyzer
import lexer.LexerImplementation
import lexer.newrules.BooleanAnalyzer
import lexer.newrules.BooleanOperatorsAnalyzer
import lexer.newrules.BooleanTypeAnalyzer
import lexer.newrules.ConstAnalyzer
import lexer.newrules.IfElseAnalyzer
import lexer.newrules.ReadInputAnalyzer
import lexer.newrules.TabAnalyzer
import lexer.rules.EnterAnalyzer
import lexer.rules.KeywordAnalyzer
import lexer.rules.MidNumberAnalyzer
import lexer.rules.MidStringAnalyzer
import lexer.rules.NumberAnalyzer
import lexer.rules.NumberTypeAnalyzer
import lexer.rules.OperatorAnalyzer
import lexer.rules.PunctuationAnalyzer
import lexer.rules.StringAnalyzer
import lexer.rules.StringTypeAnalyzer
import lexer.rules.TokenAnalyzer
import lexer.rules.VariableAnalyzer
import lexer.rules.WhitespaceAnalyzer
import newanalyzers.IfOpenBlockInTheSameLineAnalyzer
import newanalyzers.IndentationAnalyzer
import newanalyzers.NewLineAfterIfStatementAnalyzer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class NewFormatterTest {
    private lateinit var formatter: Formatter
    private lateinit var lexer: lexer.Lexer

    @BeforeEach
    fun setup() {
        formatter = FormatterImpl(
            listOf(
                CanNotStartLineWithSpaceAnalyzer(), IfOpenBlockInTheSameLineAnalyzer(), NewLinesBeforePrintlnAnalyzer(1), SpaceAfterColonAnalyzer(),
                SpaceAfterEqualsAnalyzer(), SpaceAfterOperatorAnalyzer(), NewLineAfterIfStatementAnalyzer(),
                SpaceBeforeEqualsAnalyzer(), SpaceBeforeOperatorAnalyzer(),
                SpaceBeforeColonAnalyzer(), NewLineAfterSemiColonAnalyzer(), OnlyOneSpaceAnalyzer(),
                SpaceAfterEqualsAnalyzer(), IndentationAnalyzer(),

            ),
            1,
        )
        lexer = LexerImplementation(
            listOf<TokenAnalyzer>(
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
                TabAnalyzer(),
                EnterAnalyzer(),
            ),
        )
    }

    @Test
    fun `debe poner la llave de apertura en la misma línea que el if`() {
        val tokens = lexer.tokenize("if (a>5)\n{println(a);}")
        val result = formatter.format(tokens)
        assertEquals(result, "if (a > 5){\n\tprintln(a);\n}")
    }

    @Test
    fun `debe aplicar indentado configurado dentro del if`() {
        val tokens = lexer.tokenize("if(a>5){println(a);}")
        val result = formatter.format(tokens)
        assertEquals(result, "if(a > 5){\n\tprintln(a);\n}")
    }

    @Test
    fun `debe manejar múltiples sentencias dentro del if con indentado`() {
        val tokens = lexer.tokenize("if(x<10){let y:number=5;println(y);}")
        val result = formatter.format(tokens)
        assertEquals(result, "if(x < 10){\n\tlet y : number = 5;\n\tprintln(y);\n}")
    }

    @Test
    fun `debe mantener el indentado correcto en if anidados`() {
        val tokens = lexer.tokenize("if(a>0){if(b>0){println(b);}}")
        val result = formatter.format(tokens)
        assertEquals(
            result,
            "if(a > 0){\n\tif(b > 0){\n\t\tprintln(b);\n\t}\n}",
        )
    }
}
