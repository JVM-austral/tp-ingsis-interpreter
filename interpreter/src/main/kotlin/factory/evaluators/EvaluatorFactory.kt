package factory.evaluators

import evaluator.AstEvaluator
import mock.OutputHandler

class EvaluatorFactory {
    fun createEvaluationEngineV1(outputHandler: OutputHandler): AstEvaluator {
        return AstEvaluationEngineV1(outputHandler)
    }
    fun createEvaluationEngineV2(outputHandler: OutputHandler): AstEvaluator {
        return AstEvaluationEngineV2(outputHandler)
    }
}
