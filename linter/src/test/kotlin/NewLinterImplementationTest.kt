import ast.BinaryOperation
import ast.FunctionCallAst
import ast.IfDeclaration
import ast.StringLiteral
import ast.TypeDeclaration
import ast.VarDeclaration
import ast.VariableIdentifier
import newanalyzers.ConcatenationInReadInputAnalyzer
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class NewLinterImplementationTest {

    @Test
    fun `test linter with readInputRules that should be ok`() {
        val linter = LinterImplementation(listOf(ConcatenationInReadInputAnalyzer()))
        val validVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("validName", 1, 5),
            TypeDeclaration("String", 1, 16),
            FunctionCallAst(
                "readInput",
                listOf(StringLiteral("Enter your name: ", 1, 30)),
                1,
                20,
            ),
            1,
            1,
        )
        val statements = listOf(Result.success(validVarDeclaration))

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `test linter with readInputRules that should not be ok`() {
        val linter = LinterImplementation(listOf(ConcatenationInReadInputAnalyzer()))
        val validVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("validName", 1, 5),
            TypeDeclaration("String", 1, 16),
            FunctionCallAst(
                "readInput",
                listOf(
                    BinaryOperation(
                        "+",
                        StringLiteral("Enter your name: ", 1, 30),
                        StringLiteral("!", 1, 50),
                        1,
                        40,
                    ),
                ),
                1,
                20,
            ),
            1,
            1,
        )
        val statements = listOf(Result.success(validVarDeclaration))

        val errors = linter.lint(statements)

        assertTrue(errors.isNotEmpty())
    }

    @Test
    fun `test linter with readInputRules can accept variable `() {
        val linter = LinterImplementation(listOf(ConcatenationInReadInputAnalyzer()))
        val validVarDeclaration = VarDeclaration(
            "let",
            StringLiteral("validName", 1, 5),
            TypeDeclaration("String", 1, 16),
            FunctionCallAst(
                "readInput",
                listOf(VariableIdentifier("hola ", 1, 30)),
                1,
                20,
            ),
            1,
            1,
        )
        val statements = listOf(Result.success(validVarDeclaration))

        val errors = linter.lint(statements)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `should detect errors in ifs`() {
        val linter = LinterImplementation(listOf(ConcatenationInReadInputAnalyzer()))

        val validIfBlock = IfDeclaration(
            "if",
            VariableIdentifier("condition", 2, 3),
            listOf(
                Result.success(
                    VarDeclaration(
                        "let",
                        StringLiteral("validName", 3, 5),
                        TypeDeclaration("String", 3, 16),
                        FunctionCallAst(
                            "readInput",
                            listOf(
                                BinaryOperation(
                                    "+",
                                    StringLiteral("Enter your name: ", 3, 30),
                                    StringLiteral("!", 3, 50),
                                    3,
                                    40,
                                ),
                            ),
                            3,
                            20,
                        ),
                        3,
                        1,
                    ),

                ),
            ),
            listOf(),
            2,
            1,
        )

        val statements = listOf(Result.success(validIfBlock))

        val errors = linter.lint(statements)

        assertFalse(errors.isEmpty())
    }
}
