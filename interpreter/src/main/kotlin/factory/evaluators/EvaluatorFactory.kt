package factory.evaluators

import evaluator.AstEvaluator
import evaluator.input.InputProvider
import evaluator.input.LiteralConverter
import mock.OutputHandler

class EvaluatorFactory(
    private val canPrint: Boolean? = true,
) {
    fun createEvaluationEngineV1(outputHandler: OutputHandler): AstEvaluator = AstEvaluationEngineV1(outputHandler)

    fun createEvaluationEngineV2(
        outputHandler: OutputHandler,
        inputProvider: InputProvider,
        converter: LiteralConverter,
    ): AstEvaluator = AstEvaluationEngineV2(outputHandler, inputProvider, converter, canPrint)
}
