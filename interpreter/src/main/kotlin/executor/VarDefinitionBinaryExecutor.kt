package executor

import IsCompatibleTypeCondition
import PriorityDeclarationCondition
import VarDefinitionBinaryStructureCondition
import ast.Ast
import condition.ConstDefinitionCondition
import evaluator.AstEvaluator
import interpreter.VariableInfo

class VarDefinitionBinaryExecutor(
    private val engine: AstEvaluator,
    private val isCompatibleTypeCondition: IsCompatibleTypeCondition,
    private val structureCondition: VarDefinitionBinaryStructureCondition,
    private val declarationCondition: PriorityDeclarationCondition,
    private val constCondition: ConstDefinitionCondition,
) : InterpreterExecutor {

    override fun execute(
        statement: Result<Ast>,
        heap: MutableMap<String, VariableInfo>,env:MutableMap<String,String>
    ): Result<Ast> {
        val structureError = structureCondition.evaluate(statement, heap)
        if (structureError != null) return errorResult(structureError)

        val ast = statement.getOrNull() ?: return errorResult("AST is null")
        val variableName = obtenerNombreVariable(ast)
        val binaryOperationAst = obtenerOperacionBinaria(ast)

        return try {
            val evaluatedValue = engine.evaluate(binaryOperationAst, heap,env)

            val declarationError = declarationCondition.evaluate(statement, heap)
            if (declarationError != null) return errorResult(declarationError)
            val constError = constCondition.evaluate(statement, heap)
            if (constError != null) return errorResult(constError)

            isCompatibleTypeCondition.setEvaluatedValue(evaluatedValue)
            val typeError = isCompatibleTypeCondition.evaluate(statement, heap)
            if (typeError != null) return errorResult(typeError)

            val type = heap[variableName]?.type
                ?: return errorResult("Variable '$variableName' has no type information")

            heap[variableName] = VariableInfo(type, evaluatedValue.toString())
            Result.success(ast)
        } catch (e: Exception) {
            errorResult(e.message)
        }
    }

    private fun obtenerNombreVariable(ast: Ast) = ast.getListOfChildren()[0].getValue()
    private fun obtenerOperacionBinaria(ast: Ast) = ast.getListOfChildren()[1]
    private fun errorResult(message: String?): Result<Ast> = Result.failure(Exception(message))
}
