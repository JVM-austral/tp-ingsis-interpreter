package factory.analyzers

import ConditionMessageHandler
import IsCompatibleTypeCondition
import MissMatchNumberCondition
import MissMatchStringCondition
import MissMatchTypeCondition
import PriorityDeclarationCondition
import VarDefinitionBinaryStructureCondition
import analyzer.IfDeclarationAnalyzer
import analyzer.InterpreterAnalyzer
import analyzer.PrintLnAnalyzer
import analyzer.TypeDeclarationAnalyzer
import analyzer.VarDeclarationWithAssigmentBinaryAnalyzer
import analyzer.VarDeclarationWithAssigmentUnaryAnalyzer
import analyzer.VarDefinitionBinaryAnalyzer
import analyzer.VarDefinitionUnaryAnalyzer
import condition.ConstDefinitionCondition
import condition.MissMatchBooleanCondition
import factory.evaluators.EvaluatorFactory
import mock.OutputHandler

class AnalyzerFactory {
    private val evaluatorFactory = EvaluatorFactory()
    fun createAnalyzerV1(outputHandler: OutputHandler): List<InterpreterAnalyzer> {
        val engineV1 = evaluatorFactory.createEvaluationEngineV1(outputHandler)
        return listOf(
            PrintLnAnalyzer(outputHandler, engineV1),
            TypeDeclarationAnalyzer(),
            VarDeclarationWithAssigmentUnaryAnalyzer(ConditionMessageHandler(listOf(ConstDefinitionCondition()))),
            VarDeclarationWithAssigmentBinaryAnalyzer(
                engineV1,
                IsCompatibleTypeCondition(
                    mapOf(
                        "number" to Number::class,
                        "string" to String::class,
                    ),
                ),
                ConstDefinitionCondition(),
            ),
            VarDefinitionUnaryAnalyzer(
                ConditionMessageHandler(
                    listOf(
                        MissMatchTypeCondition(
                            listOf(
                                MissMatchNumberCondition(),
                                MissMatchStringCondition(),
                            ),
                        ),
                        PriorityDeclarationCondition(),
                        ConstDefinitionCondition(),
                    ),
                ),
            ),
            VarDefinitionBinaryAnalyzer(
                engineV1,
                IsCompatibleTypeCondition(
                    mapOf("number" to Number::class, "string" to String::class),
                ),
                VarDefinitionBinaryStructureCondition(),
                PriorityDeclarationCondition(),
                ConstDefinitionCondition(),
            ),
        )
    }
    fun createAnalyzerV2(outputHandler: OutputHandler): List<InterpreterAnalyzer> {
        val engineV2 = evaluatorFactory.createEvaluationEngineV2(outputHandler)
        return listOf(
            IfDeclarationAnalyzer(engineV2, outputHandler),
            PrintLnAnalyzer(outputHandler, engineV2),
            TypeDeclarationAnalyzer(),
            VarDeclarationWithAssigmentUnaryAnalyzer(ConditionMessageHandler(listOf(ConstDefinitionCondition()))),
            VarDeclarationWithAssigmentBinaryAnalyzer(
                engineV2,
                IsCompatibleTypeCondition(
                    mapOf(
                        "number" to Number::class,
                        "string" to String::class,
                        "boolean" to Boolean::class,
                    ),
                ),
                ConstDefinitionCondition(),
            ),
            VarDefinitionUnaryAnalyzer(
                ConditionMessageHandler(
                    listOf(
                        MissMatchTypeCondition(
                            listOf(
                                MissMatchNumberCondition(),
                                MissMatchStringCondition(),
                                MissMatchBooleanCondition(),
                            ),
                        ),
                        PriorityDeclarationCondition(),
                    ),
                ),
            ),
            VarDefinitionBinaryAnalyzer(
                engineV2,
                IsCompatibleTypeCondition(
                    mapOf(
                        "number" to Number::class,
                        "string" to String::class,
                        "boolean" to Boolean::class,
                    ),
                ),
                VarDefinitionBinaryStructureCondition(),
                PriorityDeclarationCondition(),
                ConstDefinitionCondition(),
            ),
        )
    }
}
