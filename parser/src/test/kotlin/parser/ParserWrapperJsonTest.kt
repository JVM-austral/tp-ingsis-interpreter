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
import wrapper.IteratorWrapper
import wrapper.ParserWrapperImplementation

class ParserWrapperJsonTest {

    private lateinit var parser: ParserImplementation
    private lateinit var letAnalyzer: LetVariableDeclarationAnalyzer
    private lateinit var letWithStringAssignmentAnalyzer: LetVariableDeclarationWithStringAssignmentAnalyzer
    private lateinit var letWithNumberAssignmentAnalyzer: LetVariableDeclarationWithNumberAssignmentAnalyzer
    private lateinit var variableDefinitionAnalyzer: VariableDefinitionAnalyzer
    private lateinit var binaryNumberAnalyzer: BinaryNumberOperatorAnalyzer
    private lateinit var stringConcatenationAnalyzer: StringConcatenationAnalyzer
    private lateinit var functionAnalyzer: FunctionAnalyzer

    @BeforeEach
    fun setUp() {
        letAnalyzer = LetVariableDeclarationAnalyzer(listOf("number", "string"), listOf("let"))
        letWithStringAssignmentAnalyzer = LetVariableDeclarationWithStringAssignmentAnalyzer(listOf("number", "string"), listOf("let"))
        letWithNumberAssignmentAnalyzer = LetVariableDeclarationWithNumberAssignmentAnalyzer(listOf("number", "string"), listOf("let"))
        variableDefinitionAnalyzer = VariableDefinitionAnalyzer()
        binaryNumberAnalyzer = BinaryNumberOperatorAnalyzer()
        stringConcatenationAnalyzer = StringConcatenationAnalyzer()
        functionAnalyzer = FunctionAnalyzer()

        parser =
            ParserImplementation(
                listOf(functionAnalyzer, letAnalyzer, letWithNumberAssignmentAnalyzer, letWithStringAssignmentAnalyzer, variableDefinitionAnalyzer),
            )
    }

    @Test
    fun `test simple let declaration to JSON using wrapper`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("userName", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("string", TokenType.IDENTIFIER, 1, 4),
            Token(";", TokenType.PUNCTUATION, 1, 5),
        )
        val tokenResults = tokens.map { Result.success(it) }
        val lexerWrapper = object : IteratorWrapper<Result<Token>> {
            private var index = 0
            override fun hasNext(): Boolean = index < tokenResults.size
            override fun next(): Result<Token> = tokenResults[index++]
        }
        val wrapper = ParserWrapperImplementation(lexerWrapper, parser)
        val astResults = mutableListOf<Result<ast.Ast>>()
        while (wrapper.hasNext()) {
            astResults.add(wrapper.next())
        }
        val actualJson = astResults.toJson()
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
        println("\n🔍 LET DECLARATION WRAPPER TEST")
        printJsonComparison(expectedJson, actualJson)
        assertJsonSimilar(expectedJson, actualJson)
    }

    @Test
    fun `test let with number assignment to JSON using wrapper`() {
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
        val tokenResults = tokens.map { Result.success(it) }
        val lexerWrapper = object : IteratorWrapper<Result<Token>> {
            private var index = 0
            override fun hasNext(): Boolean = index < tokenResults.size
            override fun next(): Result<Token> = tokenResults[index++]
        }
        val wrapper = ParserWrapperImplementation(lexerWrapper, parser)
        val astResults = mutableListOf<Result<ast.Ast>>()
        while (wrapper.hasNext()) {
            astResults.add(wrapper.next())
        }
        val actualJson = astResults.toJson()
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
        println("\n🔍 LET WITH NUMBER ASSIGNMENT WRAPPER TEST")
        printJsonComparison(expectedJson, actualJson)
        assertJsonSimilar(expectedJson, actualJson)
    }

    @Test
    fun `test let with string assignment to JSON using wrapper`() {
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
        val tokenResults = tokens.map { Result.success(it) }
        val lexerWrapper = object : IteratorWrapper<Result<Token>> {
            private var index = 0
            override fun hasNext(): Boolean = index < tokenResults.size
            override fun next(): Result<Token> = tokenResults[index++]
        }
        val wrapper = ParserWrapperImplementation(lexerWrapper, parser)
        val astResults = mutableListOf<Result<ast.Ast>>()
        while (wrapper.hasNext()) {
            astResults.add(wrapper.next())
        }
        val actualJson = astResults.toJson()
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
        println("\n🔍 LET WITH STRING ASSIGNMENT WRAPPER TEST")
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

    @Test
    fun `test let pi declaration, assignment and println to JSON using wrapper`() {
        val tokens = listOf(
            Token("let", TokenType.KEYWORD, 1, 1),
            Token("pi", TokenType.IDENTIFIER, 1, 2),
            Token(":", TokenType.PUNCTUATION, 1, 3),
            Token("number", TokenType.IDENTIFIER, 1, 4),
            Token(";", TokenType.PUNCTUATION, 1, 5),
            Token("pi", TokenType.IDENTIFIER, 1, 6),
            Token("=", TokenType.OPERATOR, 1, 7),
            Token("3.14", TokenType.NUMBER_LITERAL, 1, 8),
            Token(";", TokenType.PUNCTUATION, 1, 9),
            Token("println", TokenType.IDENTIFIER, 1, 10),
            Token("(", TokenType.PUNCTUATION, 1, 11),
            Token("pi", TokenType.IDENTIFIER, 1, 12),
            Token("/", TokenType.OPERATOR, 1, 13),
            Token("2", TokenType.NUMBER_LITERAL, 1, 14),
            Token(")", TokenType.PUNCTUATION, 1, 15),
            Token(";", TokenType.PUNCTUATION, 1, 16),
        )
        val tokenResults = tokens.map { Result.success(it) }
        val lexerWrapper = object : IteratorWrapper<Result<Token>> {
            private var index = 0
            override fun hasNext(): Boolean = index < tokenResults.size
            override fun next(): Result<Token> = tokenResults[index++]
        }
        val wrapper = ParserWrapperImplementation(lexerWrapper, parser)
        val astResults = mutableListOf<Result<ast.Ast>>()
        while (wrapper.hasNext()) {
            astResults.add(wrapper.next())
        }
        val actualJson = astResults.toJson()
        val expectedJson = """
    [
  {
    "type": "VarDeclaration",
    "value": "let",
    "children": [
      {
        "type": "StringLiteral",
        "value": "pi"
      },
      {
        "type": "TypeDeclaration",
        "value": "number"
      },
      {
        "type": "ScapeAst",
        "value": ""
      }
    ]
  },
  {
    "type": "VarDefinition",
    "value": "=",
    "children": [
      {
        "type": "StringLiteral",
        "value": "pi"
      },
      {
        "type": "NumberLiteral",
        "value": "3.14"
      }
    ]
  },
  {
    "type": "FunctionCallAst",
    "value": "println",
    "children": [
      {
        "type": "BinaryOperation",
        "value": "/",
        "children": [
          {
            "type": "VariableIdentifier",
            "value": "pi"
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
        println("\n🔍 LET PI DECLARATION, ASSIGNMENT AND PRINTLN WRAPPER TEST")
        printJsonComparison(expectedJson, actualJson)
        assertJsonSimilar(expectedJson, actualJson)
    }
}
