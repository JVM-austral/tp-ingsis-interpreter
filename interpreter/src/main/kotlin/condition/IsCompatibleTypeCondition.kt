import ast.BinaryOperation
import ast.BooleanBinaryOperation
import ast.VarDefinition
import kotlin.reflect.KClass

class IsCompatibleTypeCondition(
    private val mapOfCondition: Map<String, KClass<*>>,
) : Condition {
    private var evaluatedValue: Any? = null

    fun setEvaluatedValue(value: Any?) {
        evaluatedValue = value
    }

    override fun evaluate(
        statement: Result<ast.Ast>,
        heap: MutableMap<String, interpreter.VariableInfo>,
    ): String? {
        val ast = statement.getOrNull() ?: return "Error: AST nulo"
        val row = ast.getRow()
        val column = ast.getColumn()
        val type =
            if (
                ast is VarDefinition &&
                (ast.getListOfChildren()[1] is BinaryOperation || ast.getListOfChildren()[1] is BooleanBinaryOperation)
            ) {
                val key = ast.getListOfChildren()[0].getValue() as? String
                heap[key]?.type
            } else {
                ast.getListOfChildren()[1].getValue()
            }
        val expectedType = mapOfCondition[type]
        if (evaluatedValue == null) return "Error: valor evaluado nulo (rw: $row, col: $column)"
        return if (expectedType != null && expectedType.isInstance(evaluatedValue)) {
            null
        } else {
            val actualType = evaluatedValue?.let { it::class.simpleName } ?: "null"
            "Tipo incompatible: se esperaba $type pero se obtuvo $actualType at row $row and column $column"
        }
    }
}
