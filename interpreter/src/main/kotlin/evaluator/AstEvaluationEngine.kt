package evaluator

import ast.Ast
import ast.BinaryOperation
import ast.FunctionCallAst
import ast.NumberLiteral
import ast.StringLiteral
import ast.VariableIdentifier
import interpreter.VariableInfo
import mock.OutputHandler
import mock.StdOutputHandler

class AstEvaluationEngine(private val outputHandler: OutputHandler = StdOutputHandler()) {
    private val evaluators: Map<Class<out Ast>, AstEvaluator> = mapOf(
        NumberLiteral::class.java to NumberLiteralEvaluator(),
        StringLiteral::class.java to StringLiteralEvaluator(),
        VariableIdentifier::class.java to VariableIdentifierEvaluator(),
        BinaryOperation::class.java to BinaryOperationEvaluator(this),
        FunctionCallAst::class.java to FunctionCallEvaluator(this, outputHandler),
    )

    fun evaluate(ast: Ast, heap: MutableMap<String, VariableInfo>): Any {
        val evaluator = evaluators[ast::class.java]
            ?: throw Exception("Tipo de AST no soportado: ${ast::class.simpleName}")
        return evaluator.evaluate(ast, heap)
    }
}
