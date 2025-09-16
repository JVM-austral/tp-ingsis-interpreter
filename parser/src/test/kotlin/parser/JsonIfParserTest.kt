package dsl.parser

import analyzer.BinaryNumberOperatorAnalyzer
import analyzer.FunctionAnalyzer
import analyzer.LetVariableDeclarationAnalyzer
import analyzer.LetVariableDeclarationWithNumberAssignmentAnalyzer
import analyzer.LetVariableDeclarationWithStringAssignmentAnalyzer
import analyzer.StringConcatenationAnalyzer
import analyzer.VariableDefinitionAnalyzer
import dsl.AstJsonDsl.toJson
import newanalyzers.BooleanDeclarationAnalyzer
import newanalyzers.BooleanDefinitionAnalyzer
import newanalyzers.IfAnalyzer
import newanalyzers.LetVariableDeclarationWithBooleanAnalyzer
import newanalyzers.LetVariableDeclarationWithEnvAssignment
import newanalyzers.LetVariableDeclarationWithInputAssignment
import newanalyzers.VariableDefinitionWithEnvAnalyzer
import newanalyzers.VariableDefinitionWithInputAnalyzer
import org.junit.jupiter.api.BeforeEach
import parser.ParserImplementation
import token.Token
import token.TokenType
import kotlin.test.Test

class JsonIfParserTest {
    private lateinit var parser: ParserImplementation
    private lateinit var letAnalyzer: LetVariableDeclarationAnalyzer
    private lateinit var letWithStringAssignmentAnalyzer: LetVariableDeclarationWithStringAssignmentAnalyzer
    private lateinit var letWithNumberAssignmentAnalyzer: LetVariableDeclarationWithNumberAssignmentAnalyzer
    private lateinit var variableDefinitionAnalyzer: VariableDefinitionAnalyzer
    private lateinit var binaryNumberAnalyzer: BinaryNumberOperatorAnalyzer
    private lateinit var stringConcatenationAnalyzer: StringConcatenationAnalyzer
    private lateinit var booleanDeclarationAnalyzer: BooleanDeclarationAnalyzer
    private lateinit var booleanDefinitionAnalyzer: BooleanDefinitionAnalyzer
    private lateinit var letWithBooleanAnalyzer: LetVariableDeclarationWithBooleanAnalyzer

    private lateinit var letEnvAnalyzer: LetVariableDeclarationWithEnvAssignment
    private lateinit var varDefEnvAnalyzer: VariableDefinitionWithEnvAnalyzer

    private lateinit var letInputAnalyzer: LetVariableDeclarationWithInputAssignment
    private lateinit var varDefInputAnalyzer: VariableDefinitionWithInputAnalyzer

    @BeforeEach
    fun setUp() {
        letAnalyzer = LetVariableDeclarationAnalyzer(listOf("number", "string", "boolean"), listOf("let", "const"))
        letWithStringAssignmentAnalyzer = LetVariableDeclarationWithStringAssignmentAnalyzer(listOf("number", "string", "boolean"), listOf("let", "const"))
        letWithNumberAssignmentAnalyzer = LetVariableDeclarationWithNumberAssignmentAnalyzer(listOf("number", "string", "boolean"), listOf("let", "const"))
        variableDefinitionAnalyzer = VariableDefinitionAnalyzer()
        binaryNumberAnalyzer = BinaryNumberOperatorAnalyzer()
        stringConcatenationAnalyzer = StringConcatenationAnalyzer()
        booleanDeclarationAnalyzer = BooleanDeclarationAnalyzer(listOf("number", "string", "boolean"), listOf("let", "const"))
        booleanDefinitionAnalyzer = BooleanDefinitionAnalyzer()
        letWithBooleanAnalyzer = LetVariableDeclarationWithBooleanAnalyzer(listOf("number", "string", "boolean"), listOf("let", "const"))

        letEnvAnalyzer = LetVariableDeclarationWithEnvAssignment(listOf("number", "string", "boolean"), listOf("let", "const"))
        varDefEnvAnalyzer = VariableDefinitionWithEnvAnalyzer()

        letInputAnalyzer = LetVariableDeclarationWithInputAssignment(listOf("number", "string", "boolean"), listOf("let", "const"))
        varDefInputAnalyzer = VariableDefinitionWithInputAnalyzer()

        parser =
            ParserImplementation(
                listOf(
                    letAnalyzer, letWithNumberAssignmentAnalyzer,
                    letWithStringAssignmentAnalyzer, variableDefinitionAnalyzer, FunctionAnalyzer(),
                    booleanDeclarationAnalyzer, booleanDefinitionAnalyzer, letWithBooleanAnalyzer,
                    IfAnalyzer(), letEnvAnalyzer, varDefEnvAnalyzer,
                    letInputAnalyzer, varDefInputAnalyzer,
                ),

            )
    }

    @Test
    fun `test simple if without else to JSON`() {
        val tokens = listOf(
            Token("if", TokenType.CONDITIONAL, 1, 1),
            Token("(", TokenType.PUNCTUATION, 1, 2),
            Token("true", TokenType.BOOLEAN_LITERAL, 1, 3),
            Token(")", TokenType.PUNCTUATION, 1, 4),
            Token("{", TokenType.PUNCTUATION, 1, 5),
            Token("println", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token("\"ok\"", TokenType.STRING_LITERAL, 1, 8),
            Token(")", TokenType.PUNCTUATION, 1, 9),
            Token(";", TokenType.PUNCTUATION, 1, 10),
            Token("}", TokenType.PUNCTUATION, 1, 11),
        )

        val result = parser.parse(tokens.map { Result.success(it) })
        val actualJson = result.toJson()

        val expectedJson = """
        [
          {
            "type": "IfDeclaration",
            "value": "if",
            "children": [
              {
                "type": "BooleanLiteral",
                "value": "true"
              }
            ],
            "onSuccess": [
              {
                "type": "FunctionCallAst",
                "value": "println",
                "children": [
                  {
                    "type": "StringLiteral",
                    "value": "ok"
                  }
                ]
              }
            ],
            "onFailure": []
          }
        ]
        """.trimIndent()

        println("\n🔍 IF WITHOUT ELSE TEST")
        printJsonComparison(expectedJson, actualJson)
        assertJsonSimilar(expectedJson, actualJson)
    }

    @Test
    fun `test if with else to JSON`() {
        val tokens = listOf(
            Token("if", TokenType.CONDITIONAL, 1, 1),
            Token("(", TokenType.PUNCTUATION, 1, 2),
            Token("false", TokenType.BOOLEAN_LITERAL, 1, 3),
            Token(")", TokenType.PUNCTUATION, 1, 4),
            Token("{", TokenType.PUNCTUATION, 1, 5),
            Token("println", TokenType.IDENTIFIER, 1, 6),
            Token("(", TokenType.PUNCTUATION, 1, 7),
            Token("\"then branch\"", TokenType.STRING_LITERAL, 1, 8),
            Token(")", TokenType.PUNCTUATION, 1, 9),
            Token(";", TokenType.PUNCTUATION, 1, 10),
            Token("}", TokenType.PUNCTUATION, 1, 11),
            Token("else", TokenType.CONDITIONAL, 1, 12),
            Token("{", TokenType.PUNCTUATION, 1, 13),
            Token("println", TokenType.IDENTIFIER, 1, 14),
            Token("(", TokenType.PUNCTUATION, 1, 15),
            Token("\"else branch\"", TokenType.STRING_LITERAL, 1, 16),
            Token(")", TokenType.PUNCTUATION, 1, 17),
            Token(";", TokenType.PUNCTUATION, 1, 18),
            Token("}", TokenType.PUNCTUATION, 1, 19),
        )

        val result = parser.parse(tokens.map { Result.success(it) })
        val actualJson = result.toJson()

        val expectedJson = """
        [
          {
            "type": "IfDeclaration",
            "value": "if",
            "children": [
              {
                "type": "BooleanLiteral",
                "value": "false"
              }
            ],
            "onSuccess": [
              {
                "type": "FunctionCallAst",
                "value": "println",
                "children": [
                  {
                    "type": "StringLiteral",
                    "value": "then branch"
                  }
                ]
              }
            ],
            "onFailure": [
              {
                "type": "FunctionCallAst",
                "value": "println",
                "children": [
                  {
                    "type": "StringLiteral",
                    "value": "else branch"
                  }
                ]
              }
            ]
          }
        ]
        """.trimIndent()

        println("\n🔍 IF WITH ELSE TEST")
        printJsonComparison(expectedJson, actualJson)
        assertJsonSimilar(expectedJson, actualJson)
    }

    private fun printJsonComparison(expected: String, actual: String) {
        println("=" * 60)
        println("📋 EXPECTED:")
        println(expected)
        println("-" * 60)
        println("🔍 ACTUAL:")
        println(actual)
        println("=" * 60)
    }

    private fun assertJsonSimilar(expected: String, actual: String) {
        val expectedNormalized = normalizeJson(expected)
        val actualNormalized = normalizeJson(actual)
        assert(expectedNormalized == actualNormalized) {
            "JSON structures do not match!\nExpected: $expectedNormalized\nActual: $actualNormalized"
        }
    }

    private fun normalizeJson(json: String): String {
        return json.replace("\\s+".toRegex(), " ").trim()
    }

    private operator fun String.times(n: Int): String = this.repeat(n)
}
