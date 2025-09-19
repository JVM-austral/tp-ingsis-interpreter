package evaluator

import ast.Ast
import ast.VariableIdentifier
import evaluator.typeconversionstrategy.TypeConversionStrategy
import interpreter.VariableInfo

class VariableIdentifierEvaluator(
    private val converter: List<TypeConversionStrategy>,
) : AstEvaluator {
    override fun evaluate(
        ast: Ast,
        heap: MutableMap<String, VariableInfo>,
        env: MutableMap<String, Ast>,
    ): Any {
        val node = ast as VariableIdentifier
        val varInfo = heap[node.getValue()] ?: throw Exception("Variable no encontrada: ${node.getValue()}")
        for (strategy in converter) {
            if (strategy.canConvert(varInfo.type)) {
                return strategy.convert(varInfo.value)
            }
        }
        throw Exception("No se pudo convertir el tipo de la variable: ${varInfo.type}")
    }
}
