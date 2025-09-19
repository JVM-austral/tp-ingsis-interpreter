package evaluator

import ast.Ast

class ReadEnvEvaluator(
    private val engine: AstEvaluator,
) : AstEvaluator {
    override fun evaluate(
        ast: ast.Ast,
        heap: MutableMap<String, interpreter.VariableInfo>,
        env: MutableMap<String, Ast>,
    ): Any {
        val rawName = ast.getListOfChildren()[0].getValue()
        val varName = rawName.trim('"')
        return env[varName]?.let { engine.evaluate(it, heap, env) } ?: throw Exception("La variable de entorno '$varName' no existe")
    }
}
