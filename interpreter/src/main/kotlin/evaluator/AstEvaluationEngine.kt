package evaluator

import ast.Ast
import ast.BinaryOperation
import ast.FunctionCallAst
import ast.NumberLiteral
import ast.StringLiteral
import ast.VariableIdentifier
import evaluator.binarystrategy.AdditionStrategy
import evaluator.binarystrategy.DivisionStrategy
import evaluator.binarystrategy.MultiplicationStrategy
import evaluator.binarystrategy.SubtractionStrategy
import evaluator.typeconversionstrategy.NumberTypeStrategy
import evaluator.typeconversionstrategy.StringTypeStrategy
import interpreter.VariableInfo
import mock.MockOutputHandler
import mock.OutputHandler

class AstEvaluationEngine(private val outputHandler: OutputHandler = MockOutputHandler()) {
    private val evaluators: Map<Class<out Ast>, AstEvaluator> = mapOf(
        NumberLiteral::class.java to NumberLiteralEvaluator(),
        StringLiteral::class.java to StringLiteralEvaluator(),
        VariableIdentifier::class.java to VariableIdentifierEvaluator(listOf(NumberTypeStrategy(), StringTypeStrategy())),
        BinaryOperation::class.java to BinaryOperationEvaluator(this, listOf(AdditionStrategy(), SubtractionStrategy(), MultiplicationStrategy(), DivisionStrategy())),
        FunctionCallAst::class.java to PrintLnEvaluator(this, outputHandler),
    )

    fun evaluate(ast: Ast, heap: MutableMap<String, VariableInfo>): Any {
        val evaluator = evaluators[ast::class.java]
            ?: throw Exception("Tipo de AST no soportado: ${ast::class.simpleName}")
        return evaluator.evaluate(ast, heap)
    }
}
