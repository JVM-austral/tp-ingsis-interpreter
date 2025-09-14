package factory.evaluators

import ast.Ast
import ast.BinaryOperation
import ast.BooleanBinaryOperation
import ast.BooleanLiteral
import ast.FunctionCallAst
import ast.NumberLiteral
import ast.StringLiteral
import ast.VariableIdentifier
import evaluator.AstEvaluator
import evaluator.BinaryOperationEvaluator
import evaluator.BooleanBinaryOperationEvaluator
import evaluator.BooleanLiteralEvaluator
import evaluator.FunctionCallEvaluator
import evaluator.NumberLiteralEvaluator
import evaluator.StringLiteralEvaluator
import evaluator.VariableIdentifierEvaluator
import evaluator.binarystrategy.AdditionStrategy
import evaluator.binarystrategy.DivisionStrategy
import evaluator.binarystrategy.MultiplicationStrategy
import evaluator.binarystrategy.SubtractionStrategy
import evaluator.booleanstrategy.EqualsStrategy
import evaluator.typeconversionstrategy.BooleanTypeStrategy
import evaluator.typeconversionstrategy.NumberTypeStrategy
import evaluator.typeconversionstrategy.StringTypeStrategy
import interpreter.VariableInfo
import mock.OutputHandler
import mock.StdOutputHandler

class AstEvaluationEngineV2(private val outputHandler: OutputHandler = StdOutputHandler()) : AstEvaluator {
    private val evaluators: Map<Class<out Ast>, AstEvaluator> = mapOf(
        NumberLiteral::class.java to NumberLiteralEvaluator(),
        StringLiteral::class.java to StringLiteralEvaluator(),
        VariableIdentifier::class.java to VariableIdentifierEvaluator(
            listOf(
                NumberTypeStrategy(), StringTypeStrategy(),
                BooleanTypeStrategy(),
            ),
        ),
        BinaryOperation::class.java to BinaryOperationEvaluator(this, listOf(AdditionStrategy(), SubtractionStrategy(), MultiplicationStrategy(), DivisionStrategy())),
        FunctionCallAst::class.java to FunctionCallEvaluator(this, outputHandler),
        BooleanLiteral::class.java to BooleanLiteralEvaluator(),
        BooleanBinaryOperation::class.java to BooleanBinaryOperationEvaluator(this, listOf(EqualsStrategy())),

    )

    override fun evaluate(ast: Ast, heap: MutableMap<String, VariableInfo>, env: MutableMap<String, String>): Any {
        val evaluator = evaluators[ast::class.java]
            ?: throw Exception("Tipo de AST no soportado: ${ast::class.simpleName}")
        return evaluator.evaluate(ast, heap, env)
    }
}
