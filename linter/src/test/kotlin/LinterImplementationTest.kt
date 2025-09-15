import analyzers.CamelCaseAnalyzer
import analyzers.PrintLnWithOutBinaryOperationAnalyzer
import analyzers.SnakeCaseAnalyzer
import ast.Ast
import ast.BinaryOperation
import ast.FunctionCallAst
import ast.NumberLiteral
import ast.ScapeAst
import ast.StringLiteral
import ast.TypeDeclaration
import ast.VarDeclaration
import ast.VariableIdentifier
import linter.LinterImplementation
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
            StringLiteral("validName", 1, 5),
            TypeDeclaration("String", 1, 16),
            StringLiteral("value", 1, 25),
            1,
            1,
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
            StringLiteral("validCamelCase", 1, 5),
            TypeDeclaration("String", 1, 20),
            StringLiteral("value", 1, 29),
            1,
            1,
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
            StringLiteral("invalid_snake_case", 1, 5),
            TypeDeclaration("String", 1, 25),
            StringLiteral("value", 1, 34),
            1,
            1,
        )
        val statements = listOf(Result.success(invalidVarDeclaration))

        val errors = linter.lint(statements)

        assertEquals(1, errors.size)
        assertEquals("Variable name 'invalid_snake_case' is not in camelCase format: ", errors[0].message)
        assertEquals(1, errors[0].line)
        assertEquals(5, errors[0].column)
    }

    @Test
    fun `test camelCase analyzer with invalid PascalCase variable name`() {
        val rules = listOf(CamelCaseAnalyzer())
        val linter = LinterImplementation(rules)
        val invalidVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("InvalidPascalCase", 1, 5),
            TypeDeclaration("String", 1, 24),
            StringLiteral("value", 1, 33),
            1,
            1,
        )
        val statements = listOf(Result.success(invalidVarDeclaration))

        val errors = linter.lint(statements)

        assertEquals(1, errors.size)
        assertEquals(1, errors[0].line)
        assertEquals(5, errors[0].column)
        assertEquals("Variable name 'InvalidPascalCase' is not in camelCase format: ", errors[0].message)
    }

    @Test
    fun `test snakeCase analyzer with valid snake_case variable name`() {
        val rules = listOf(SnakeCaseAnalyzer())
        val linter = LinterImplementation(rules)
        val validVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("valid_snake_case", 1, 5),
            TypeDeclaration("String", 1, 23),
            StringLiteral("value", 1, 32),
            1,
            1,
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
            StringLiteral("valid", 1, 5),
            TypeDeclaration("String", 1, 12),
            StringLiteral("value", 1, 21),
            1,
            1,
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
            StringLiteral("invalidCamelCase", 1, 5),
            TypeDeclaration("String", 1, 23),
            StringLiteral("value", 1, 32),
            1,
            1,
        )
        val statements = listOf(Result.success(invalidVarDeclaration))

        val errors = linter.lint(statements)

        assertEquals(1, errors.size)
        assertEquals("Variable name 'invalidCamelCase' is not in snake_case format: ", errors[0].message)
    }

    @Test
    fun `test println analyzer with valid string literal parameter`() {
        val rules = listOf(PrintLnWithOutBinaryOperationAnalyzer())
        val linter = LinterImplementation(rules)
        val validFunctionCall = FunctionCallAst(
            "println",
            listOf(StringLiteral("Hello World", 1, 9)),
            1,
            1,
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
            listOf(VariableIdentifier("myVar", 1, 9)),
            1,
            1,
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
            NumberLiteral("5", 1, 9),
            NumberLiteral("3", 1, 13),
            1,
            7,
        )
        val invalidFunctionCall = FunctionCallAst(
            "println",
            listOf(binaryOperation),
            1,
            1,
        )
        val statements = listOf(Result.success(invalidFunctionCall))

        val errors = linter.lint(statements)

        assertEquals(1, errors.size)
        assertEquals("println should`nt have a binary operation as parameter", errors[0].message)
        assertEquals(1, errors[0].line)
        assertEquals(1, errors[0].column)
    }

    @Test
    fun `test println analyzer ignores other function calls`() {
        val rules = listOf(PrintLnWithOutBinaryOperationAnalyzer())
        val linter = LinterImplementation(rules)
        val binaryOperation = BinaryOperation(
            "+",
            NumberLiteral("5", 1, 7),
            NumberLiteral("3", 1, 11),
            1,
            5,
        )
        val otherFunctionCall = FunctionCallAst(
            "print",
            listOf(binaryOperation),
            1,
            1,
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
            NumberLiteral("5", 1, 22),
            NumberLiteral("3", 1, 26),
            1,
            20,
        )
        val functionCallWithMultipleParams = FunctionCallAst(
            "println",
            listOf(StringLiteral("Result: ", 1, 9), binaryOperation),
            1,
            1,
        )
        val statements = listOf(Result.success(functionCallWithMultipleParams))

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test type declaration with camelCase analyzer`() {
        val rules = listOf(CamelCaseAnalyzer())
        val linter = LinterImplementation(rules)
        val validTypeDeclaration = TypeDeclaration("validCamelCase", 1, 1)
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
            StringLiteral("someVariable", 1, 5),
            TypeDeclaration("String", 1, 19),
            StringLiteral("value", 1, 28),
            1,
            1,
        )
        val statements = listOf(Result.success(varDeclaration))

        val errors = linter.lint(statements)

        // Should return error from first rule that fails (SnakeCaseAnalyzer in this case)
        assertEquals(1, errors.size)
        assertEquals("Variable name 'someVariable' is not in snake_case format: ", errors[0].message)
    }

    @Test
    fun `test multiple statements with mixed errors`() {
        val rules = listOf(CamelCaseAnalyzer(), PrintLnWithOutBinaryOperationAnalyzer())
        val linter = LinterImplementation(rules)

        val invalidVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("invalid_name", 1, 5),
            TypeDeclaration("String", 1, 19),
            StringLiteral("value", 1, 28),
            1,
            1,
        )

        val binaryOperation = BinaryOperation("+", NumberLiteral("1", 2, 9), NumberLiteral("2", 2, 13), 2, 7)
        val invalidPrintln = FunctionCallAst("println", listOf(binaryOperation), 2, 1)

        val validVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("validName", 3, 5),
            TypeDeclaration("String", 3, 16),
            StringLiteral("value", 3, 25),
            3,
            1,
        )

        val statements = listOf(
            Result.success(invalidVarDeclaration),
            Result.success(invalidPrintln),
            Result.success(validVarDeclaration),
        )

        val errors = linter.lint(statements)

        assertEquals(2, errors.size)
        assertEquals("Variable name 'invalid_name' is not in camelCase format: ", errors[0].message)
        assertEquals("println should`nt have a binary operation as parameter", errors[1].message)
    }

    @Test
    fun `test analyzers ignore irrelevant AST types`() {
        val rules = listOf(CamelCaseAnalyzer(), SnakeCaseAnalyzer())
        val linter = LinterImplementation(rules)

        val statements = listOf(
            Result.success(StringLiteral("some string", 1, 1)),
            Result.success(NumberLiteral("42", 2, 1)),
            Result.success(VariableIdentifier("someVar", 3, 1)),
            Result.success(BinaryOperation("+", NumberLiteral("1", 4, 1), NumberLiteral("2", 4, 5), 4, 1)),
            Result.success(ScapeAst()),
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

        assertEquals("Statement is not formatted correctly Parse error", exception.message)
    }

    @Test
    fun `test edge case with single lowercase letter variable name`() {
        val rules = listOf(CamelCaseAnalyzer())
        val linter = LinterImplementation(rules)
        val varDeclaration = VarDeclaration(
            "let",
            StringLiteral("a", 1, 5),
            TypeDeclaration("String", 1, 8),
            StringLiteral("value", 1, 17),
            1,
            1,
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
            StringLiteral("variable123", 1, 5),
            TypeDeclaration("String", 1, 18),
            StringLiteral("value", 1, 27),
            1,
            1,
        )
        val statements = listOf(Result.success(varDeclaration))

        val errors = linter.lint(statements)

        assertEquals(1, errors.size)
        assertEquals("Variable name 'variable123' is not in camelCase format: ", errors[0].message)
    }

    @Test
    fun `test snake_case analyzer with variable containing uppercase`() {
        val rules = listOf(SnakeCaseAnalyzer())
        val linter = LinterImplementation(rules)
        val varDeclaration = VarDeclaration(
            "let",
            StringLiteral("Invalid_Case", 1, 5),
            TypeDeclaration("String", 1, 19),
            StringLiteral("value", 1, 28),
            1,
            1,
        )
        val statements = listOf(Result.success(varDeclaration))

        val errors = linter.lint(statements)

        assertEquals(1, errors.size)
        assertEquals("Variable name 'Invalid_Case' is not in snake_case format: ", errors[0].message)
    }

    @Test
    fun `test println analyzer with no parameters`() {
        val rules = listOf(PrintLnWithOutBinaryOperationAnalyzer())
        val linter = LinterImplementation(rules)
        val printlnWithNoParams = FunctionCallAst("println", emptyList(), 1, 1)
        val statements = listOf(Result.success(printlnWithNoParams))

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test comprehensive scenario with all analyzers and various AST types`() {
        val rules = listOf(
            CamelCaseAnalyzer(),
            PrintLnWithOutBinaryOperationAnalyzer(),
        )
        val linter = LinterImplementation(rules)

        val validCamelCaseVar = VarDeclaration(
            "let",
            StringLiteral("validName", 1, 5),
            TypeDeclaration("String", 1, 16),
            StringLiteral("value", 1, 25),
            1,
            1,
        )

        val invalidVar = VarDeclaration(
            "let",
            StringLiteral("Invalid_Name", 2, 5),
            TypeDeclaration("String", 2, 19),
            StringLiteral("value", 2, 28),
            2,
            1,
        )

        val validPrintln = FunctionCallAst("println", listOf(StringLiteral("Hello", 3, 9)), 3, 1)
        val invalidPrintln = FunctionCallAst(
            "println",
            listOf(BinaryOperation("+", StringLiteral("Hello ", 4, 9), StringLiteral("World", 4, 19), 4, 7)),
            4,
            1,
        )

        val statements = listOf(
            Result.success(validCamelCaseVar),
            Result.success(invalidVar),
            Result.success(validPrintln),
            Result.success(invalidPrintln),
        )

        val errors = linter.lint(statements)

        // Should have 2 errors: one from camelCase rule, one from println rule
        assertEquals(2, errors.size)
        assertTrue(errors.any { it.message.contains("not in camelCase format") })
        assertTrue(errors.any { it.message.contains("println should`nt have a binary operation") })
    }
}
