package evaluator.input

class LiteralConverter {
    fun convert(value: Any, col: Int, row: Int): ast.Ast {
        return when (value) {
            is Int, is Double, is Float -> ast.NumberLiteral(value.toString(), col, row)
            is Boolean -> ast.BooleanLiteral(value.toString(), col, row)
            is String -> {
                if (value == "true" || value == "false") {
                    ast.BooleanLiteral(value, col, row)
                } else {
                    ast.StringLiteral(value, col, row)
                }
            }
            else -> throw IllegalArgumentException("Tipo no soportado")
        }
    }
}
