import analyzers.CamelCaseAnalyzer
import analyzers.PrintLnWithOutBinaryOperationAnalyzer
import analyzers.SnakeCaseAnalyzer
import ast.*
import error.LinterError
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LinterImplementationTest {

    @Test
    fun `test linter with empty rules list returns no errors`() {
        val linter = LinterImplementation(emptyList())
        val validVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("validName"),
            TypeDeclaration("String"),
            StringLiteral("value")
        )
        val statements = listOf(Result.success(validVarDeclaration))

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test linter with empty statements list returns no errors`() {
        val rules = listOf(CamelCaseAnalyzer(), SnakeCaseAnalyzer())
        val linter = LinterImplementation(rules)

        val errors = linter.lint(emptyList())

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test camelCase analyzer with valid camelCase variable name`() {
        val rules = listOf(CamelCaseAnalyzer())
        val linter = LinterImplementation(rules)
        val validVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("validCamelCase"),
            TypeDeclaration("String"),
            StringLiteral("value")
        )
        val statements = listOf(Result.success(validVarDeclaration))

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test camelCase analyzer with invalid snake_case variable name`() {
        val rules = listOf(CamelCaseAnalyzer())
        val linter = LinterImplementation(rules)
        val invalidVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("invalid_snake_case"),
            TypeDeclaration("String"),
            StringLiteral("value")
        )
        val statements = listOf(Result.success(invalidVarDeclaration))

        val errors = linter.lint(statements)

        assertEquals(1, errors.size)
        assertEquals("Variable name 'invalid_snake_case' is not in camelCase format", errors[0].message)
    }

    @Test
    fun `test camelCase analyzer with invalid PascalCase variable name`() {
        val rules = listOf(CamelCaseAnalyzer())
        val linter = LinterImplementation(rules)
        val invalidVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("InvalidPascalCase"),
            TypeDeclaration("String"),
            StringLiteral("value")
        )
        val statements = listOf(Result.success(invalidVarDeclaration))

        val errors = linter.lint(statements)

        assertEquals(1, errors.size)
        assertEquals("Variable name 'InvalidPascalCase' is not in camelCase format", errors[0].message)
    }

    @Test
    fun `test snakeCase analyzer with valid snake_case variable name`() {
        val rules = listOf(SnakeCaseAnalyzer())
        val linter = LinterImplementation(rules)
        val validVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("valid_snake_case"),
            TypeDeclaration("String"),
            StringLiteral("value")
        )
        val statements = listOf(Result.success(validVarDeclaration))

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test snakeCase analyzer with single word variable name`() {
        val rules = listOf(SnakeCaseAnalyzer())
        val linter = LinterImplementation(rules)
        val validVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("valid"),
            TypeDeclaration("String"),
            StringLiteral("value")
        )
        val statements = listOf(Result.success(validVarDeclaration))

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test snakeCase analyzer with invalid camelCase variable name`() {
        val rules = listOf(SnakeCaseAnalyzer())
        val linter = LinterImplementation(rules)
        val invalidVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("invalidCamelCase"),
            TypeDeclaration("String"),
            StringLiteral("value")
        )
        val statements = listOf(Result.success(invalidVarDeclaration))

        val errors = linter.lint(statements)

        assertEquals(1, errors.size)
        assertEquals("Variable name 'invalidCamelCase' is not in snake_case format", errors[0].message)
    }

    @Test
    fun `test println analyzer with valid string literal parameter`() {
        val rules = listOf(PrintLnWithOutBinaryOperationAnalyzer())
        val linter = LinterImplementation(rules)
        val validFunctionCall = FunctionCallAst(
            "println",
            listOf(StringLiteral("Hello World"))
        )
        val statements = listOf(Result.success(validFunctionCall))

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test println analyzer with valid variable identifier parameter`() {
        val rules = listOf(PrintLnWithOutBinaryOperationAnalyzer())
        val linter = LinterImplementation(rules)
        val validFunctionCall = FunctionCallAst(
            "println",
            listOf(VariableIdentifier("myVar"))
        )
        val statements = listOf(Result.success(validFunctionCall))

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test println analyzer with invalid binary operation parameter`() {
        val rules = listOf(PrintLnWithOutBinaryOperationAnalyzer())
        val linter = LinterImplementation(rules)
        val binaryOperation = BinaryOperation(
            "+",
            NumberLiteral("5"),
            NumberLiteral("3")
        )
        val invalidFunctionCall = FunctionCallAst(
            "println",
            listOf(binaryOperation)
        )
        val statements = listOf(Result.success(invalidFunctionCall))

        val errors = linter.lint(statements)

        assertEquals(1, errors.size)
        assertEquals("println should`nt have a binary operation as parameter", errors[0].message)
    }

    @Test
    fun `test println analyzer ignores other function calls`() {
        val rules = listOf(PrintLnWithOutBinaryOperationAnalyzer())
        val linter = LinterImplementation(rules)
        val binaryOperation = BinaryOperation(
            "+",
            NumberLiteral("5"),
            NumberLiteral("3")
        )
        val otherFunctionCall = FunctionCallAst(
            "print",
            listOf(binaryOperation)
        )
        val statements = listOf(Result.success(otherFunctionCall))

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test println analyzer with multiple parameters ignores binary operation check`() {
        val rules = listOf(PrintLnWithOutBinaryOperationAnalyzer())
        val linter = LinterImplementation(rules)
        val binaryOperation = BinaryOperation(
            "+",
            NumberLiteral("5"),
            NumberLiteral("3")
        )
        val functionCallWithMultipleParams = FunctionCallAst(
            "println",
            listOf(StringLiteral("Result: "), binaryOperation)
        )
        val statements = listOf(Result.success(functionCallWithMultipleParams))

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test type declaration with camelCase analyzer`() {
        val rules = listOf(CamelCaseAnalyzer())
        val linter = LinterImplementation(rules)
        val validTypeDeclaration = TypeDeclaration("validCamelCase")
        val statements = listOf(Result.success(validTypeDeclaration))

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test multiple rules with conflicting requirements`() {
        val rules = listOf(CamelCaseAnalyzer(), SnakeCaseAnalyzer())
        val linter = LinterImplementation(rules)
        val varDeclaration = VarDeclaration(
            "let",
            StringLiteral("someVariable"),
            TypeDeclaration("String"),
            StringLiteral("value")
        )
        val statements = listOf(Result.success(varDeclaration))

        val errors = linter.lint(statements)

        // Should return error from first rule that fails (SnakeCaseAnalyzer in this case)
        assertEquals(1, errors.size)
        assertEquals("Variable name 'someVariable' is not in snake_case format", errors[0].message)
    }

    @Test
    fun `test multiple statements with mixed errors`() {
        val rules = listOf(CamelCaseAnalyzer(), PrintLnWithOutBinaryOperationAnalyzer())
        val linter = LinterImplementation(rules)

        val invalidVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("invalid_name"),
            TypeDeclaration("String"),
            StringLiteral("value")
        )

        val binaryOperation = BinaryOperation("+", NumberLiteral("1"), NumberLiteral("2"))
        val invalidPrintln = FunctionCallAst("println", listOf(binaryOperation))

        val validVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("validName"),
            TypeDeclaration("String"),
            StringLiteral("value")
        )

        val statements = listOf(
            Result.success(invalidVarDeclaration),
            Result.success(invalidPrintln),
            Result.success(validVarDeclaration)
        )

        val errors = linter.lint(statements)

        assertEquals(2, errors.size)
        assertEquals("Variable name 'invalid_name' is not in camelCase format", errors[0].message)
        assertEquals("println should`nt have a binary operation as parameter", errors[1].message)
    }

    @Test
    fun `test analyzers ignore irrelevant AST types`() {
        val rules = listOf(CamelCaseAnalyzer(), SnakeCaseAnalyzer())
        val linter = LinterImplementation(rules)

        val statements = listOf(
            Result.success(StringLiteral("some string")),
            Result.success(NumberLiteral("42")),
            Result.success(VariableIdentifier("someVar")),
            Result.success(BinaryOperation("+", NumberLiteral("1"), NumberLiteral("2"))),
            Result.success(ScapeAst())
        )

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test linter throws exception on failed result`() {
        val rules = listOf(CamelCaseAnalyzer())
        val linter = LinterImplementation(rules)
        val statements = listOf(Result.failure<Ast>(RuntimeException("Parse error")))

        val exception = assertThrows<Exception> {
            linter.lint(statements)
        }

        assertEquals("Statement is not formatted correctly", exception.message)
    }

    @Test
    fun `test edge case with single lowercase letter variable name`() {
        val rules = listOf(CamelCaseAnalyzer())
        val linter = LinterImplementation(rules)
        val varDeclaration = VarDeclaration(
            "let",
            StringLiteral("a"),
            TypeDeclaration("String"),
            StringLiteral("value")
        )
        val statements = listOf(Result.success(varDeclaration))

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test edge case with variable name containing numbers`() {
        val rules = listOf(CamelCaseAnalyzer())
        val linter = LinterImplementation(rules)
        val varDeclaration = VarDeclaration(
            "let",
            StringLiteral("variable123"),
            TypeDeclaration("String"),
            StringLiteral("value")
        )
        val statements = listOf(Result.success(varDeclaration))

        val errors = linter.lint(statements)

        assertEquals(1, errors.size)
        assertEquals("Variable name 'variable123' is not in camelCase format", errors[0].message)
    }

    @Test
    fun `test snake_case analyzer with variable containing uppercase`() {
        val rules = listOf(SnakeCaseAnalyzer())
        val linter = LinterImplementation(rules)
        val varDeclaration = VarDeclaration(
            "let",
            StringLiteral("Invalid_Case"),
            TypeDeclaration("String"),
            StringLiteral("value")
        )
        val statements = listOf(Result.success(varDeclaration))

        val errors = linter.lint(statements)

        assertEquals(1, errors.size)
        assertEquals("Variable name 'Invalid_Case' is not in snake_case format", errors[0].message)
    }

    @Test
    fun `test println analyzer with no parameters`() {
        val rules = listOf(PrintLnWithOutBinaryOperationAnalyzer())
        val linter = LinterImplementation(rules)
        val printlnWithNoParams = FunctionCallAst("println", emptyList())
        val statements = listOf(Result.success(printlnWithNoParams))

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test comprehensive scenario with all analyzers and various AST types`() {
        val rules = listOf(
            CamelCaseAnalyzer(),
            PrintLnWithOutBinaryOperationAnalyzer()
        )
        val linter = LinterImplementation(rules)

        val validCamelCaseVar = VarDeclaration(
            "let",
            StringLiteral("validName"),
            TypeDeclaration("String"),
            StringLiteral("value")
        )

        val invalidVar = VarDeclaration(
            "let",
            StringLiteral("Invalid_Name"),
            TypeDeclaration("String"),
            StringLiteral("value")
        )

        val validPrintln = FunctionCallAst("println", listOf(StringLiteral("Hello")))
        val invalidPrintln = FunctionCallAst(
            "println",
            listOf(BinaryOperation("+", StringLiteral("Hello "), StringLiteral("World")))
        )

        val statements = listOf(
            Result.success(validCamelCaseVar),
            Result.success(invalidVar),
            Result.success(validPrintln),
            Result.success(invalidPrintln)
        )

        val errors = linter.lint(statements)

        // Should have 2 errors: one from camelCase rule, one from println rule
        assertEquals(2, errors.size)
        assertTrue(errors.any { it.message.contains("not in camelCase format") })
        assertTrue(errors.any { it.message.contains("println should`nt have a binary operation") })
    }
}
