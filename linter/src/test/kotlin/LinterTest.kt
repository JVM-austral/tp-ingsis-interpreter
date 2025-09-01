import analyzers.CamelCaseAnalyzer
import analyzers.PrintLnWithOutBinaryOperationAnalyzer
import analyzers.SnakeCaseAnalyzer
import ast.Assigment
import ast.BinaryOperation
import ast.FunctionCallAst
import ast.StringLiteral
import ast.TypeDeclaration
import ast.VarDeclaration
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LinterTest {

    @Test
    fun `CamelCaseAnalyzer detects non-camelCase variable name`() {
        val varDecl = VarDeclaration(
            "let",
            StringLiteral("not_camel_case"),
            TypeDeclaration("Int"),
            StringLiteral("42"),
        )
        val analyzer = CamelCaseAnalyzer()
        val result = analyzer.analyze(varDecl)
        assertTrue(result.isPresent)
        result.get().message?.let { assertTrue(it.contains("not in camelCase")) }
    }

    @Test
    fun `SnakeCaseAnalyzer detects non-snake_case variable name`() {
        val assigment = Assigment(
            "let",
            StringLiteral("NotSnakeCase"),
            StringLiteral("42"),
        )
        val analyzer = SnakeCaseAnalyzer()
        val result = analyzer.analyze(assigment)
        assertTrue(result.isPresent)
        result.get().message?.let { assertTrue(it.contains("not in snake_case")) }
    }

    @Test
    fun `PrintLnWithOutBinaryOperationAnalyzer detects binary operation as println parameter`() {
        val left = VarDeclaration(
            "let",
            StringLiteral("a"),
            TypeDeclaration("Int"),
            StringLiteral("1"),
        )
        val right = VarDeclaration(
            "let",
            StringLiteral("b"),
            TypeDeclaration("Int"),
            StringLiteral("2"),
        )
        val binaryOp = BinaryOperation("+", left, right)
        val printlnCall = FunctionCallAst("println", listOf(binaryOp))
        val analyzer = PrintLnWithOutBinaryOperationAnalyzer()
        val result = analyzer.analyze(printlnCall)
        assertTrue(result.isPresent)
        result.get().message?.let { assertTrue(it.contains("println should`nt have a binary operation")) }
    }
}
