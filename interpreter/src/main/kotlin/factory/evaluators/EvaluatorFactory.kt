package factory.evaluators

import evaluator.AstEvaluator
import evaluator.input.InputProvider
import evaluator.input.LiteralConverter
import mock.OutputHandler

class EvaluatorFactory {
    fun createEvaluationEngineV1(outputHandler: OutputHandler): AstEvaluator {
        return AstEvaluationEngineV1(outputHandler)
    }
    fun createEvaluationEngineV2(
        outputHandler: OutputHandler,
        inputProvider: InputProvider,
        converter: LiteralConverter,
    ): AstEvaluator {
        return AstEvaluationEngineV2(outputHandler, inputProvider, converter)
    }
}
