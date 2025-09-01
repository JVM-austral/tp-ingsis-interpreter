import ast.BinaryOperation
import ast.FunctionCallAst
import ast.StringLiteral
import ast.TypeDeclaration
import ast.VarDeclaration
import linterconfig.ConfigurableAnalyzer
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ConfigurableAnalyzerTest {

    @Test
    fun `ConfigurableAnalyzer should load rules from JSON file`() {
        // Path to the example rules file
        val rulesFilePath = "src/main/kotlin/linterconfig/exampleOfRules.json"

        // Create analyzer instance with the path to JSON file
        val analyzer = ConfigurableAnalyzer(rulesFilePath)

        // Test with a variable that doesn't follow camelCase (since our config uses camelCase)
        val varDecl = VarDeclaration(
            "let",
            StringLiteral("not_camel_case"),
            TypeDeclaration("Int"),
            StringLiteral("42"),
        )

        // Analyze should find an error since the name doesn't follow camelCase
        val result = analyzer.analyze(varDecl)
        assertTrue(result.isPresent)
        result.get().message?.let { assertTrue(it.contains("not in camelCase")) }
    }

    @Test
    fun `ConfigurableAnalyzer should detect println with binary operation`() {
        // Path to the example rules file
        val rulesFilePath = "src/main/kotlin/linterconfig/exampleOfRules.json"

        // Create analyzer instance with the path to JSON file
        val analyzer = ConfigurableAnalyzer(rulesFilePath)

        // Create a binary operation
        val binaryOp = BinaryOperation(
            "+",
            StringLiteral("10"),
            StringLiteral("5"),
        )

        // Create a println function call with the binary operation as parameter
        val printlnCall = FunctionCallAst(
            "println",
            listOf(binaryOp),
        )

        // Analyze should find an error since we're using println with a binary operation
        val result = analyzer.analyze(printlnCall)
        assertTrue(result.isPresent)
        result.get().message?.let { assertTrue(it.contains("println should`nt have a binary operation as parameter")) }
    }

    @Test
    fun `ConfigurableAnalyzer should not detect issues with valid code`() {
        // Path to the example rules file
        val rulesFilePath = "src/main/kotlin/linterconfig/exampleOfRules.json"

        // Create analyzer instance with the path to JSON file
        val analyzer = ConfigurableAnalyzer(rulesFilePath)

        // Create a valid variable declaration with camelCase
        val varDecl = VarDeclaration(
            "let",
            StringLiteral("validCamelCase"),
            TypeDeclaration("Int"),
            StringLiteral("42"),
        )

        // Analyze should not find an error
        val result = analyzer.analyze(varDecl)
        assertFalse(result.isPresent)
    }

    @Test
    fun `ConfigurableAnalyzer should handle file creation from string JSON`() {
        // Create temporary rules as string
        val jsonRules = """
        {
          "namingConvention": "snake_case",
          "usePrintlnAnalyzer": false
        }
        """.trimIndent()

        // Use the fromJson factory method to create analyzer from string
        val analyzer = ConfigurableAnalyzer.fromJson(jsonRules)

        // Test with snake_case variable (should pass)
        val validSnakeCase = VarDeclaration(
            "let",
            StringLiteral("valid_snake_case"),
            TypeDeclaration("Int"),
            StringLiteral("42"),
        )

        val result = analyzer.analyze(validSnakeCase)
        assertFalse(result.isPresent)

        // Test with camelCase variable (should fail)
        val invalidCamelCase = VarDeclaration(
            "let",
            StringLiteral("invalidCamelCase"),
            TypeDeclaration("Int"),
            StringLiteral("42"),
        )

        val errorResult = analyzer.analyze(invalidCamelCase)
        assertTrue(errorResult.isPresent)
        errorResult.get().message?.let { assertTrue(it.contains("not in snake_case")) }
    }
}
