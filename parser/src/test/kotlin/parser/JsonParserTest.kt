package parser

import analyzer.BinaryNumberOperatorAnalyzer
import analyzer.FunctionAnalyzer
import analyzer.LetVariableDeclarationAnalyzer
import analyzer.LetVariableDeclarationWithNumberAssignmentAnalyzer
import analyzer.LetVariableDeclarationWithStringAssignmentAnalyzer
import analyzer.StringConcatenationAnalyzer
import analyzer.VariableDefinitionAnalyzer
import dsl.AstJsonDsl.toJson
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import token.Token
import token.TokenType

class JsonParserTest {

    private lateinit var parser: ParserImplementation
    private lateinit var letAnalyzer: LetVariableDeclarationAnalyzer
    private lateinit var letWithStringAssignmentAnalyzer: LetVariableDeclarationWithStringAssignmentAnalyzer
    private lateinit var letWithNumberAssignmentAnalyzer: LetVariableDeclarationWithNumberAssignmentAnalyzer
    private lateinit var variableDefinitionAnalyzer: VariableDefinitionAnalyzer
    private lateinit var binaryNumberAnalyzer: BinaryNumberOperatorAnalyzer
    private lateinit var stringConcatenationAnalyzer: StringConcatenationAnalyzer

    @BeforeEach
    fun setUp() {
        letAnalyzer = LetVariableDeclarationAnalyzer(listOf("number", "string"), listOf("let"))
        letWithStringAssignmentAnalyzer = LetVariableDeclarationWithStringAssignmentAnalyzer(listOf("number", "string"), listOf("let"))
        letWithNumberAssignmentAnalyzer = LetVariableDeclarationWithNumberAssignmentAnalyzer(listOf("number", "string"), listOf("let"))
        variableDefinitionAnalyzer = VariableDefinitionAnalyzer()
        binaryNumberAnalyzer = BinaryNumberOperatorAnalyzer()
        stringConcatenationAnalyzer = StringConcatenationAnalyzer()

        parser =
            ParserImplementation(
                listOf(letAnalyzer, letWithNumberAssignmentAnalyzer, letWithStringAssignmentAnalyzer, variableDefinitionAnalyzer, FunctionAnalyzer()),
            )
    }

    @Test
    fun `test simple let declaration to JSON`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("userName", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token(";", TokenType.PUNCTUATION, 1, 5),
        )

        val result = parser.parse(tokens.map { Result.success(it) })
        val actualJson = result.toJson()

        val expectedJson = """
        [
          {
            "type": "VarDeclaration",
            "value": "let",
            "children": [
              {
                "type": "StringLiteral",
                "value": "userName"
              },
              {
                "type": "TypeDeclaration",
                "value": "string"
              },
              {
                "type": "ScapeAst",
                "value": ""
              }
            ]
          }
        ]
        """.trimIndent()

        println("\n🔍 LET DECLARATION TEST")
        printJsonComparison(expectedJson, actualJson)
        assertJsonSimilar(expectedJson, actualJson)
    }

    @Test
    fun `test let with number assignment to JSON`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("result", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("number", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("5", TokenType.NUMBER_LITERAL, 1, 6),
            Token("+", TokenType.OPERATOR, 1, 7),
            Token("3", TokenType.NUMBER_LITERAL, 1, 8),
            Token("+", TokenType.OPERATOR, 1, 7),
            Token("3", TokenType.NUMBER_LITERAL, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        val result = parser.parse(tokens.map { Result.success(it) })
        val actualJson = result.toJson()

        val expectedJson = """
[
  {
    "type": "VarDeclaration",
    "value": "let",
    "children": [
      {
        "type": "StringLiteral",
        "value": "result"
      },
      {
        "type": "TypeDeclaration",
        "value": "number"
      },
      {
        "type": "BinaryOperation",
        "value": "+",
        "children": [
          {
            "type": "BinaryOperation",
            "value": "+",
            "children": [
              {
                "type": "NumberLiteral",
                "value": "5"
              },
              {
                "type": "NumberLiteral",
                "value": "3"
              }
            ]
          },
          {
            "type": "NumberLiteral",
            "value": "3"
          }
        ]
      }
    ]
  }
]
        """.trimIndent()

        println("\n🔍 LET WITH NUMBER ASSIGNMENT TEST")
        printJsonComparison(expectedJson, actualJson)
        assertJsonSimilar(expectedJson, actualJson)
    }

    @Test
    fun `test let with string assignment to JSON`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("message", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token("=", TokenType.OPERATOR, 1, 5),
            Token("\"hello\"", TokenType.STRING_LITERAL, 1, 6),
            Token("+", TokenType.OPERATOR, 1, 7),
            Token("\"world\"", TokenType.STRING_LITERAL, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
        )

        val result = parser.parse(tokens.map { Result.success(it) })
        val actualJson = result.toJson()

        val expectedJson = """
        [
          {
            "type": "VarDeclaration",
            "value": "let",
            "children": [
              {
                "type": "StringLiteral",
                "value": "message"
              },
              {
                "type": "TypeDeclaration", 
                "value": "string"
              },
              {
                "type": "BinaryOperation",
                "value": "+",
                "children": [
                  {
                    "type": "StringLiteral",
                    "value": "hello"
                  },
                  {
                    "type": "StringLiteral",
                    "value": "world"
                  }
                ]
              }
            ]
          }
        ]
        """.trimIndent()

        println("\n🔍 LET WITH STRING ASSIGNMENT TEST")
        printJsonComparison(expectedJson, actualJson)
        assertJsonSimilar(expectedJson, actualJson)
    }

    @Test
    fun `test variable definition to JSON`() {
        val tokens = listOf(
            Token("userName", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("\"John\"", TokenType.STRING_LITERAL, 1, 3),
            Token(";", TokenType.PUNCTUATION, 1, 4),
        )

        val result = parser.parse(tokens.map { Result.success(it) })
        val actualJson = result.toJson()

        val expectedJson = """
        [
          {
            "type": "VarDefinition",
            "value": "=",
            "children": [
              {
                "type": "StringLiteral",
                "value": "userName"
              },
              {
                "type": "StringLiteral",
                "value": "John"
              }
            ]
          }
        ]
        """.trimIndent()

        println("\n🔍 VARIABLE DEFINITION TEST")
        printJsonComparison(expectedJson, actualJson)
        assertJsonSimilar(expectedJson, actualJson)
    }

    @Test
    fun `test variable definition with arithmetic to JSON`() {
        val tokens = listOf(
            Token("result", TokenType.IDENTIFIER, 1, 1),
            Token("=", TokenType.OPERATOR, 1, 2),
            Token("10", TokenType.NUMBER_LITERAL, 1, 3),
            Token("*", TokenType.OPERATOR, 1, 4),
            Token("2", TokenType.NUMBER_LITERAL, 1, 5),
            Token(";", TokenType.PUNCTUATION, 1, 6),
        )

        val result = parser.parse(tokens.map { Result.success(it) })
        val actualJson = result.toJson()

        val expectedJson = """
        [
          {
            "type": "VarDefinition",
            "value": "=",
            "children": [
              {
                "type": "StringLiteral",
                "value": "result"
              },
              {
                "type": "BinaryOperation",
                "value": "*",
                "children": [
                  {
                    "type": "NumberLiteral",
                    "value": "10"
                  },
                  {
                    "type": "NumberLiteral",
                    "value": "2"
                  }
                ]
              }
            ]
          }
        ]
        """.trimIndent()

        println("\n🔍 VARIABLE DEFINITION WITH ARITHMETIC TEST")
        printJsonComparison(expectedJson, actualJson)
        assertJsonSimilar(expectedJson, actualJson)
    }

    @Test
    fun `test println function call to JSON`() {
        val tokens = listOf(
            Token("println", TokenType.IDENTIFIER, 1, 1),
            Token("(", TokenType.PUNCTUATION, 1, 2),
            Token("\"hello\"", TokenType.STRING_LITERAL, 1, 3),
            Token(")", TokenType.PUNCTUATION, 1, 4),
            Token(";", TokenType.PUNCTUATION, 1, 5),
        )

        val result = parser.parse(tokens.map { Result.success(it) })
        val actualJson = result.toJson()

        val expectedJson = """
        [
          {
            "type": "FunctionCallAst",
            "value": "println",
            "children": [
              {
                "type": "StringLiteral",
                "value": "hello"
              }
            ]
          }
        ]
        """.trimIndent()

        println("\n🔍 PRINTLN FUNCTION CALL TEST")
        printJsonComparison(expectedJson, actualJson)
        assertJsonSimilar(expectedJson, actualJson)
    }

    @Test
    fun `test println with variable to JSON`() {
        val tokens = listOf(
            Token("println", TokenType.IDENTIFIER, 1, 1),
            Token("(", TokenType.PUNCTUATION, 1, 2),
            Token("userName", TokenType.IDENTIFIER, 1, 3),
            Token(")", TokenType.PUNCTUATION, 1, 4),
            Token(";", TokenType.PUNCTUATION, 1, 5),
        )

        val result = parser.parse(tokens.map { Result.success(it) })
        val actualJson = result.toJson()

        val expectedJson = """
        [
          {
            "type": "FunctionCallAst",
            "value": "println",
            "children": [
              {
                "type": "VariableIdentifier",
                "value": "userName"
              }
            ]
          }
        ]
        """.trimIndent()

        println("\n🔍 PRINTLN WITH VARIABLE TEST")
        printJsonComparison(expectedJson, actualJson)
        assertJsonSimilar(expectedJson, actualJson)
    }

    @Test
    fun `test println with arithmetic expression to JSON`() {
        val tokens = listOf(
            Token("println", TokenType.IDENTIFIER, 1, 1),
            Token("(", TokenType.PUNCTUATION, 1, 2),
            Token("5", TokenType.NUMBER_LITERAL, 1, 3),
            Token("+", TokenType.OPERATOR, 1, 4),
            Token("3", TokenType.NUMBER_LITERAL, 1, 5),
            Token(")", TokenType.PUNCTUATION, 1, 6),
            Token(";", TokenType.PUNCTUATION, 1, 7),
        )

        val result = parser.parse(tokens.map { Result.success(it) })
        val actualJson = result.toJson()

        val expectedJson = """
        [
          {
            "type": "FunctionCallAst",
            "value": "println",
            "children": [
              {
                "type": "BinaryOperation",
                "value": "+",
                "children": [
                  {
                    "type": "NumberLiteral",
                    "value": "5"
                  },
                  {
                    "type": "NumberLiteral",
                    "value": "3"
                  }
                ]
              }
            ]
          }
        ]
        """.trimIndent()

        println("\n🔍 PRINTLN WITH ARITHMETIC TEST")
        printJsonComparison(expectedJson, actualJson)
        assertJsonSimilar(expectedJson, actualJson)
    }

    @Test
    fun `test invalid syntax to JSON`() {
        val tokens = listOf(
            Token("invalid", TokenType.KEYWORD, 1, 1),
            Token("syntax", TokenType.IDENTIFIER, 1, 2),
        )

        val result = parser.parse(tokens.map { Result.success(it) })
        val actualJson = result.toJson()

        println("\n🔍 ERROR HANDLING TEST")
        println("Generated JSON for invalid syntax:")
        println(actualJson)

        assert(actualJson.contains("ERROR") || actualJson.contains("Exception") || actualJson.isEmpty())
    }

    @Test
    fun `test multiple statements to JSON`() {
        val letTokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("x", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("number", TokenType.IDENTIFIER, 1, 4),
            Token(";", TokenType.PUNCTUATION, 1, 5),
        )

        val result = parser.parse(letTokens.map { Result.success(it) })
        val actualJson = result.toJson()

        println("\n🔍 MULTIPLE STATEMENTS TEST")
        println("Generated JSON:")
        println(actualJson)

        assert(actualJson.contains("VarDeclaration"))
    }

    private fun printJsonComparison(expected: String, actual: String) {
        println("=" * 60)
        println("📋 EXPECTED:")
        println(expected)
        println("-" * 60)
        println("🔍 ACTUAL:")
        println(actual)
        println("=" * 60)

        if (normalizeJson(expected) == normalizeJson(actual)) {
            println("✅ JSONs MATCH!")
        } else {
            println("❌ JSONs DO NOT MATCH")
            printDifferences(expected, actual)
        }
        println()
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

    private fun printDifferences(expected: String, actual: String) {
        val expectedLines = expected.lines()
        val actualLines = actual.lines()

        println("🔍 DIFFERENCES:")
        for (i in 0 until maxOf(expectedLines.size, actualLines.size)) {
            val expectedLine = expectedLines.getOrNull(i)?.trim() ?: ""
            val actualLine = actualLines.getOrNull(i)?.trim() ?: ""

            if (expectedLine != actualLine) {
                println("Line ${i + 1}:")
                println("  Expected: '$expectedLine'")
                println("  Actual  : '$actualLine'")
            }
        }
    }
}

operator fun String.times(n: Int): String {
    return this.repeat(n)
}
